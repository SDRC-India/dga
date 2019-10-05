package org.sdrc.dga.model;
/**
 * 
 * @author Harsh (harsh@sdrc.co.in)
 *
 */

public class BubbleDataModel {
	private String name;
	private Double value;
	private Double weight;
	private Double size;
	private String color;
	private Integer areaId;
	private String rangeDistance;
	private String districtName;
	private int districtId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getRangeDistance() {
		return rangeDistance;
	}
	public void setRangeDistance(String rangeDistance) {
		this.rangeDistance = rangeDistance;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public int getDistrictId() {
		return districtId;
	}
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	
}

