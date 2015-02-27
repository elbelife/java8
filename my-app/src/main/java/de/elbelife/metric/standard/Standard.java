package de.elbelife.metric.standard;

public class Standard implements StandardMBean {
	private String name;

	public void printHelloWorld() {
		System.out.println(name + ",welcome to this world.");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}