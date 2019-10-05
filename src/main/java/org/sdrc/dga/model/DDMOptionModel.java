package org.sdrc.dga.model;

/**
 * 
 * @author Rajanikanta Sahoo
 *
 */

public class DDMOptionModel {

	private int key;
	private String value;
	private int order;
	private boolean isSelected;
	private int parentKey;
	private boolean isLive = true;
	
	
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public int getParentKey() {
		return parentKey;
	}
	public void setParentKey(int parentKey) {
		this.parentKey = parentKey;
	}
	
}
