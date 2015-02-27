package de.elbelife.metric;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AverageMetric extends TimeWindowSupport implements Metric {

    final AtomicReference<Value> currentValue = new AtomicReference<Value>();
    private volatile Value lastValue = null;

    public AverageMetric(long timeWindow) {
        super(timeWindow);
    }

    public AverageMetric() {
        super(TimeUnit.MINUTES.toMillis(1));
    }

    public Value getLastValue() {
        long slot = currentSlot();
        while(true) {
            Value curValue = currentValue.get();
            if (curValue != null && slot != curValue.slot) {
                if (currentValue.compareAndSet(curValue, Value.create(slot))) {
                    lastValue = curValue;
                    break;
                }
            } else {
                break;
            }
        }
        return lastValue;
    }

    public void log(long value) {
        long slot = currentSlot();
        while (true) {
            Value curValue = currentValue.get();
            if (curValue == null) {
                if (currentValue.compareAndSet(null, Value.create(slot, value)))
                    return;
            } else if (slot == curValue.slot) {
                if (currentValue.compareAndSet(curValue, curValue.add(value)))
                    return;
            } else {
                if (currentValue.compareAndSet(curValue, Value.create(slot, value))) {
                    lastValue = curValue;
                    return;
                }
            }
        }
    }

    /**
     * 基于同样的数据，创建一个计数度量，其返回值是过去的单位时间内的log事件发生次数
     *
     * @return 返回计数度量
     */
    public Metric createCountMetric() {
        return new Metric() {
            public long getValue() {
                Value val = getLastValue();
                if (val != null)
                    return (long) val.n;
                else
                    return 0L;
            }
        };
    }

    /**
     * 基于同样的数据，创建一个最大值度量，其返回值是过去的单位时间内记录的最大数值
     *
     * @return 返回最大值度量
     */
    public Metric createMaxValueMetric() {
        return new Metric() {
            public long getValue() {
                Value val = getLastValue();
                if (val != null)
                    return val.max;
                else
                    return 0L;
            }
        };
    }

    public long getValue() {
        Value lastValue =  getLastValue();
        long lastSlot = currentSlot() - 1;
        if (lastValue != null && lastValue.n != 0 && lastSlot == lastValue.slot)
            return lastValue.total / lastValue.n;
        else
            return 0L;
    }

    static class Value {
        final long slot;
        final int n;
        final long total;
        final long max;

        Value(long slot, int n, long total, long max) {
            this.slot = slot;
            this.n = n;
            this.total = total;
            this.max = max;
        }

        static Value create(long slot, long value) {
            return new Value(slot, 1, value, value);
        }

        static Value create(long slot) {
            return new Value(slot, 0, 0, 0);
        }

        Value add(long value) {
            return new Value(this.slot,
                             this.n + 1,
                             this.total + value,
                             (value > this.max) ? value : this.max);
        }
    }
}