package org.sdrc.dga.model;


/**
 * This class represent XForm model form UI work
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 */
public class XFormModel {

	private Integer formId;
	private String xFormId;
	private String xFormTitle;
	private String odkServerURL;
	private String username;
	private String password;
	private boolean isLive;
	private String areaXPath;
	private String secondaryAreaXPath;
	private String dateOfVisitXPath;
	private String locationXPath;
	private String toEmailIdsXPath;
	private String ccEmailIds;
	private AreaLevelModel areaLevelModel;
	private boolean sendRawData;
	private String xFormIdByODK;
	private String rootElement;
	private int xform_meta_id;
	private  int timeperiodId;
	
	public String getRootElement() {
		return rootElement;
	}
	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}
	public Integer getFormId() {
		return formId;
	}
	public void setFormId(Integer formId) {
		this.formId = formId;
	}
	public String getxFormId() {
		return xFormId;
	}
	public void setxFormId(String xFormId) {
		this.xFormId = xFormId;
	}
	public String getxFormTitle() {
		return xFormTitle;
	}
	public void setxFormTitle(String xFormTitle) {
		this.xFormTitle = xFormTitle;
	}
	public String getOdkServerURL() {
		return odkServerURL;
	}
	public void setOdkServerURL(String odkServerURL) {
		this.odkServerURL = odkServerURL;
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
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
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
	public String getToEmailIdsXPath() {
		return toEmailIdsXPath;
	}
	public void setToEmailIdsXPath(String toEmailIdsXPath) {
		this.toEmailIdsXPath = toEmailIdsXPath;
	}
	public String getCcEmailIds() {
		return ccEmailIds;
	}
	public void setCcEmailIds(String ccEmailIds) {
		this.ccEmailIds = ccEmailIds;
	}
	public AreaLevelModel getAreaLevelModel() {
		return areaLevelModel;
	}
	public void setAreaLevelModel(AreaLevelModel areaLevelModel) {
		this.areaLevelModel = areaLevelModel;
	}
	public boolean isSendRawData() {
		return sendRawData;
	}
	public void setSendRawData(boolean sendRawData) {
		this.sendRawData = sendRawData;
	}
	public String getxFormIdByODK() {
		return xFormIdByODK;
	}
	public void setxFormIdByODK(String xFormIdByODK) {
		this.xFormIdByODK = xFormIdByODK;
	}
	public int getXform_meta_id() {
		return xform_meta_id;
	}
	public void setXform_meta_id(int xform_meta_id) {
		this.xform_meta_id = xform_meta_id;
	}
	public int getTimeperiodId() {
		return timeperiodId;
	}
	public void setTimeperiodId(int timeperiodId) {
		this.timeperiodId = timeperiodId;
	}
	
		
		
}
