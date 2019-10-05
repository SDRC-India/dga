/**
 * 
 */
package org.sdrc.dga.model;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * This model will contain all the selected data from crosstab page
 * 
 *
 */
public class CrossTabDataModel {
	
	private int colIndicatorFormXpathMappingId;

	private String colDhXpath;// will be zero in case of indicator is not applicable for DH

	private String colChcXpath;// will be zero in case of indicator is not applicable for CHC

	private String colPhcXpath;// will be zero in case of indicator is not applicable for PHC
	
	private String colHscXpath;// will be zero in case of indicator is not applicable for HSC

	private String coLabel;
	
	private int rowIndicatorFormXpathMappingId;

	private String rowDhXpath;// will be zero in case of indicator is not applicable for DH

	private String rowChcXpath;// will be zero in case of indicator is not applicable for CHC

	private String rowPhcXpath;// will be zero in case of indicator is not applicable for PHC
	
	private String rowHscXpath;// will be zero in case of indicator is not applicable for HSC

	private String rowLabel;
	
	private int facilityTypeId;// will be zero in case of Facility Type-All
	
	private int districtId;// will be zero in case of state selection
	
	private int timePeriodId;// will be zero in case of Timeperiod-All
	
	private String rowIndicatorFormXpathMappingType;
	
	private String colIndicatorFormXpathMappingType;

	public int getColIndicatorFormXpathMappingId() {
		return colIndicatorFormXpathMappingId;
	}

	public void setColIndicatorFormXpathMappingId(int colIndicatorFormXpathMappingId) {
		this.colIndicatorFormXpathMappingId = colIndicatorFormXpathMappingId;
	}

	public String getColDhXpath() {
		return colDhXpath;
	}

	public void setColDhXpath(String colDhXpath) {
		this.colDhXpath = colDhXpath;
	}

	public String getColChcXpath() {
		return colChcXpath;
	}

	public void setColChcXpath(String colChcXpath) {
		this.colChcXpath = colChcXpath;
	}

	public String getColPhcXpath() {
		return colPhcXpath;
	}

	public void setColPhcXpath(String colPhcXpath) {
		this.colPhcXpath = colPhcXpath;
	}

	public String getCoLabel() {
		return coLabel;
	}

	public void setCoLabel(String coLabel) {
		this.coLabel = coLabel;
	}

	public int getRowIndicatorFormXpathMappingId() {
		return rowIndicatorFormXpathMappingId;
	}

	public void setRowIndicatorFormXpathMappingId(int rowIndicatorFormXpathMappingId) {
		this.rowIndicatorFormXpathMappingId = rowIndicatorFormXpathMappingId;
	}

	public String getRowDhXpath() {
		return rowDhXpath;
	}

	public String getColHscXpath() {
		return colHscXpath;
	}

	public void setColHscXpath(String colHscXpath) {
		this.colHscXpath = colHscXpath;
	}

	
	public String getRowHscXpath() {
		return rowHscXpath;
	}

	public void setRowHscXpath(String rowHscXpath) {
		this.rowHscXpath = rowHscXpath;
	}

	public void setRowDhXpath(String rowDhXpath) {
		this.rowDhXpath = rowDhXpath;
	}

	public String getRowChcXpath() {
		return rowChcXpath;
	}

	public void setRowChcXpath(String rowChcXpath) {
		this.rowChcXpath = rowChcXpath;
	}

	public String getRowPhcXpath() {
		return rowPhcXpath;
	}

	public void setRowPhcXpath(String rowPhcXpath) {
		this.rowPhcXpath = rowPhcXpath;
	}

	public String getRowLabel() {
		return rowLabel;
	}

	public void setRowLabel(String rowLabel) {
		this.rowLabel = rowLabel;
	}

	public int getFacilityTypeId() {
		return facilityTypeId;
	}

	public void setFacilityTypeId(int facilityTypeId) {
		this.facilityTypeId = facilityTypeId;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public int getTimePeriodId() {
		return timePeriodId;
	}

	public void setTimePeriodId(int timePeriodId) {
		this.timePeriodId = timePeriodId;
	}

	public String getRowIndicatorFormXpathMappingType() {
		return rowIndicatorFormXpathMappingType;
	}

	public void setRowIndicatorFormXpathMappingType(
			String rowIndicatorFormXpathMappingType) {
		this.rowIndicatorFormXpathMappingType = rowIndicatorFormXpathMappingType;
	}

	public String getColIndicatorFormXpathMappingType() {
		return colIndicatorFormXpathMappingType;
	}

	public void setColIndicatorFormXpathMappingType(
			String colIndicatorFormXpathMappingType) {
		this.colIndicatorFormXpathMappingType = colIndicatorFormXpathMappingType;
	}
	
	

}
