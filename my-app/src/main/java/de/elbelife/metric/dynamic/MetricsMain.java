package de.elbelife.metric.dynamic;

public class MetricsMain {

	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();
		System.out.println("Start time: " + startTime);
		Thread.sleep(10000);
		long timeCost = System.currentTimeMillis() - startTime;
		System.out.println("Time Cost: " + startTime);
		MetricLoggers.searchTime.log(timeCost);

		Metrics.instance()
				.register("SearchAvgTime", MetricLoggers.searchTime)
				.register("SearchMaxTime",MetricLoggers.searchTime.createMaxValueMetric())
				.register("SearchCount",MetricLoggers.searchTime.createCountMetric())
				.createMBean();

		Thread.sleep(500000);
	}
}
