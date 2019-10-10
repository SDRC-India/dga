package org.sdrc.dga.model;

/**
 * 
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @author Harsh Pratyush
 * @since version 1.0.0.0
 *
 */
public class AreaModel {
	
	private Integer areaId;
	private String areaName;
	private Integer parentAreaId;
	private String areaLevel;
	private Integer areaLevelId;
	private boolean isLive;
	private String areaCode;
	
	public String getAreaLevel() {
		return areaLevel;
	}
	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getAreaLevelId() {
		return areaLevelId;
	}
	public void setAreaLevelId(Integer areaLevelId) {
		this.areaLevelId = areaLevelId;
	}
	public Integer getParentAreaId() {
		return parentAreaId;
	}
	public void setParentAreaId(Integer parentAreaId) {
		this.parentAreaId = parentAreaId;
	}
	
	
}
