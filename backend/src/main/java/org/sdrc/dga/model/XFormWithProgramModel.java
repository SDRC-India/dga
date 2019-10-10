package org.sdrc.dga.model;

/**
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class XFormWithProgramModel {
	
	private Integer id;
	private String xFormId;
	private String xformTitle;
	private String areaXPath;
	private String secondaryAreaXPath;
	private String dateOfVisitXPath;
	private String locationXPath;
	private String ccEmailIds;
	private boolean sendRawData;
	private Integer areaLevelId;
	private String username;
	private String password;
	private String odkServerURL;
	private Boolean isLive;
	private Integer programId;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProgramId() {
		return programId;
	}
	public void setProgramId(Integer programId) {
		this.programId = programId;
	}
	public String getxFormId() {
		return xFormId;
	}
	public void setxFormId(String xFormId) {
		this.xFormId = xFormId;
	}
	public String getXformTitle() {
		return xformTitle;
	}
	public void setXformTitle(String xformTitle) {
		this.xformTitle = xformTitle;
	}
	public String getAreaXPath() {
		return areaXPath;
	}
	public void setAreaXPath(String areaXPath) {
		this.areaXPath = areaXPath;
	}
	public String getSecondaryAreaXPath() {
		return secondaryAreaXPath;
	}
	public void setSecondaryAreaXPath(String secondaryAreaXPath) {
		this.secondaryAreaXPath = secondaryAreaXPath;
	}
	public String getDateOfVisitXPath() {
		return dateOfVisitXPath;
	}
	public void setDateOfVisitXPath(String dateOfVisitXPath) {
		this.dateOfVisitXPath = dateOfVisitXPath;
	}
	public String getLocationXPath() {
		return locationXPath;
	}
	public void setLocationXPath(String locationXPath) {
		this.locationXPath = locationXPath;
	}
	public String getCcEmailIds() {
		return ccEmailIds;
	}
	public void setCcEmailIds(String ccEmailIds) {
		this.ccEmailIds = ccEmailIds;
	}
	public boolean isSendRawData() {
		return sendRawData;
	}
	public void setSendRawData(boolean sendRawData) {
		this.sendRawData = sendRawData;
	}
	public Integer getAreaLevelId() {
		return areaLevelId;
	}
	public void setAreaLevelId(Integer areaLevelId) {
		this.areaLevelId = areaLevelId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOdkServerURL() {
		return odkServerURL;
	}
	public void setOdkServerURL(String odkServerURL) {
		this.odkServerURL = odkServerURL;
	}
	public Boolean getIsLive() {
		return isLive;
	}
	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}
	@Override
	public String toString() {
		return "XFormWithProgramModel [xFormId=" + xFormId + ", username=" + username + ", password=" + password
				+ ", odkServerURL=" + odkServerURL + ", isLive=" + isLive + ", programId=" + programId + "]";
	}
	

}
