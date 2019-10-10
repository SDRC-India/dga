package org.sdrc.dga.model;

import java.util.Date;
/**
 * @author Harsh(harsh@sdrc.co.in)
 *
 */
public class FacilityPlanningModel 
{
	private int facilityPlannedId;
	private int facilityPlanned;
	private Date startDate;
	private Date endDate;
	private int xformId;
	private AreaModel areaModel;
	public int getFacilityPlannedId() {
		return facilityPlannedId;
	}
	public void setFacilityPlannedId(int facilityPlannedId) {
		this.facilityPlannedId = facilityPlannedId;
	}
	public int getFacilityPlanned() {
		return facilityPlanned;
	}
	public void setFacilityPlanned(int facilityPlanned) {
		this.facilityPlanned = facilityPlanned;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getXformId() {
		return xformId;
	}
	public void setXformId(int xformId) {
		this.xformId = xformId;
	}
	public AreaModel getAreaModel() {
		return areaModel;
	}
	public void setAreaModel(AreaModel areaModel) {
		this.areaModel = areaModel;
	}
	
	

}
