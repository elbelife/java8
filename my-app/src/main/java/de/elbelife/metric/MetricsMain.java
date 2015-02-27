package de.elbelife.metric;

public class MetricsMain {

	public static void main(String[] args) throws InterruptedException {
			long startTime = System.currentTimeMillis();
			Thread.sleep(10000);
			long timeCost = System.currentTimeMillis() - startTime;

			MetricLoggers.searchTime.log(timeCost);
			
			Metrics.instance()
		       .register("SearchAvgTime", MetricLoggers.searchTime)
		       .register("SearchMaxTime", MetricLoggers.searchTime.createMaxValueMetric())
		       .register("SearchCount", MetricLoggers.searchTime.createCountMetric())
		       .createMBean();
			
			 Thread.sleep(5000000);
	}
}
