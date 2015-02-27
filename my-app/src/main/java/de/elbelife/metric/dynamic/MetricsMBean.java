package de.elbelife.metric.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

public class MetricsMBean implements DynamicMBean {

	private final Map<String, Metric> metrics;

	public MetricsMBean(Map<String, Metric> metrics) {
        this.metrics = new HashMap<String, Metric>(metrics);
    }

	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		Metric metric = metrics.get(attribute);
		if (metric == null) {
			throw new AttributeNotFoundException("Attribute " + attribute
					+ " not found");
		}
		return metric.getValue();
	}

	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		throw new UnsupportedOperationException(
				"Setting attribute is not supported");
	}

	public AttributeList getAttributes(String[] attributes) {
		AttributeList attrList = new AttributeList();
		for (String attr : attributes) {
			Metric metric = metrics.get(attr);
			if (metric != null)
				attrList.add(new Attribute(attr, metric.getValue()));
		}
		return attrList;
	}

	public AttributeList setAttributes(AttributeList attributes) {
		throw new UnsupportedOperationException(
				"Setting attribute is not supported");
	}

	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		throw new UnsupportedOperationException("Invoking is not supported");
	}

	public MBeanInfo getMBeanInfo() {
        SortedSet<String> names = new TreeSet<String>(metrics.keySet());
        List<MBeanAttributeInfo> attrInfos = new ArrayList<MBeanAttributeInfo>(names.size());
        for (String name : names) {
            attrInfos.add(new MBeanAttributeInfo(name,
                                                 "long",
                                                 "Metric " + name,
                                                 true,
                                                 false,
                                                 false));
        }
        return new MBeanInfo(getClass().getName(),
                             "Application Metrics",
                             attrInfos.toArray(new MBeanAttributeInfo[attrInfos.size()]),
                             null,
                             null,
                             null);
    }
}