package org.sdrc.dga.model;

public class AreaDataModel {
	private AreaModel area;
	private Integer areaId;
	private Integer districtId;
	private String areaCode;
	private String districtName;
	private Integer parentAreaId;
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public Integer getParentAreaId() {
		return parentAreaId;
	}
	public void setParentAreaId(Integer parentAreaId) {
		this.parentAreaId = parentAreaId;
	}
	
	public AreaModel getArea() {
		return area;
	}
	public void setArea(AreaModel area) {
		this.area = area;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	

}
