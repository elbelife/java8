package de.elbelife.metric.standard;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class StandardMain {
	public static void main(String[] args) throws Exception {
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName("agent:name=test");
		Standard testMBean = new Standard();
		mBeanServer.registerMBean(testMBean, name);
		Thread.sleep(5000000);
	}
}
