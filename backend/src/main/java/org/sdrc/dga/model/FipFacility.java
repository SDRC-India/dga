package org.sdrc.dga.model;

public class FipFacility {

	private String facilityName;
	private String facilityCode;
	private int areaLevelId;
	private int areaId;
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String fasilityName) {
		this.facilityName = fasilityName;
	}
	public String getFacilityCode() {
		return facilityCode;
	}
	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}
	public int getAreaLevelId() {
		return areaLevelId;
	}
	public void setAreaLevelId(int areaLevelId) {
		this.areaLevelId = areaLevelId;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
}
