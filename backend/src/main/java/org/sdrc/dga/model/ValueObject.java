package org.sdrc.dga.model;

public class ValueObject {

	private String key;
	private String value;
	private String description;
	private String groupName;
	private String shortNmae;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ValueObject() {
		super();
	}
	public ValueObject(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getShortNmae() {
		return shortNmae;
	}
	public void setShortNmae(String shortNmae) {
		this.shortNmae = shortNmae;
	}
	
	public ValueObject(int key, String value) {
		this(new Integer(key).toString(),value);
	}
	public ValueObject(String key, String value, String description) {
		super();
		this.key = key;
		this.value = value;
		this.description = description;
	}

	
}
