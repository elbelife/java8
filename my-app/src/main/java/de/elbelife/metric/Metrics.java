package de.elbelife.metric;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class Metrics {

    private static final Logger log = Logger.getLogger(Metrics.class.getName());
    private static final Metrics instance = new Metrics();
    private Map<String, Metric> metrics = new HashMap<String, Metric>();

    public static Metrics instance() {
        return instance;
    }

    private Metrics() {
    }

    public Metrics register(String name, Metric metric) {
        metrics.put(name, metric);
        return this;
    }

    public void createMBean() {
        MetricsMBean mbean = new MetricsMBean(metrics);
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            final String name = MetricsMBean.class.getPackage().getName() +
                                ":type=" +
                                MetricsMBean.class.getSimpleName();
            
            log.log(Level.INFO, "Registering MBean: {}", name);
            server.registerMBean(mbean, new ObjectName(name));
        } catch (Exception e) {
            log.log(Level.INFO, "Error registering trafree metrics mbean", e);
        }
    }

}
