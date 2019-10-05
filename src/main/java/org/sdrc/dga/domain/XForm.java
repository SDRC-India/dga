package org.sdrc.dga.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * This domain class or entity class will keep all the XForms.
 * 
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @author Sarita
 * @since version 1.0.0.0
 *
 */
@Entity
public class XForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FormId")
	private Integer formId;

	@Column(name = "XForm_Id", nullable = false)
	private String xFormId;

	@Column(name = "XForm_Title", nullable = false)
	private String xFormIdTitle;

	@Column(name = "ODK_Server_URL", nullable = false)
	private String odkServerURL;

	@Column(name = "ODK_Username", nullable = false)
	private String username;

	@Column(name = "ODK_Password", nullable = false)
	private String password;

	@Column(name = "AreaXPath", nullable = false)
	private String areaXPath;

	@Column(name = "SecondaryAreaXPath")
	private String secondaryAreaXPath;

	@Column(name = "DateOfVisitXPath", nullable = false)
	private String dateOfVisitXPath;

	@Column(name = "LocationXPath")
	private String locationXPath;

	@Column(name = "ToEmailIdsXPath")
	private String toEmailIdsXPath;

	@Column(name = "CcEmailIds")
	private String ccEmailIds;

	@Column(name = "SendRawData", nullable = false)
	private boolean sendRawData;

	@Column(name = "IsLive", nullable = false)
	private boolean isLive;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "CreatedDate")
	private Timestamp createdDate;

	@Column(name = "UpdatedBy")
	private String updatedBy;

	@Column(name = "UpdatedDate")
	private Timestamp updatedDate;

	@ManyToOne
	@JoinColumn(name = "timePeriodId")
	private TimePeriod timePeriod;

	@ManyToOne
	@JoinColumn(name = "stateId")
	private Area state;

	private String markerClass;
	
	@Column
	private int xform_meta_id;
	
	@Column(name = "isComplete")
	private boolean isComplete; //decides whether collection is completed or not 

	@OneToOne(mappedBy = "xForm")
	private Program_XForm_Mapping programXFormMapping;

	@OneToMany(mappedBy = "xForm")
	private List<LastVisitData> lastVisitDatas;

	@OneToMany(mappedBy = "form")
	private List<FormXpathScoreMapping> formXpathScoreMappings;

	@OneToMany(mappedBy = "form")
	private List<RawFormXapths> rawXpaths;

	public List<RawFormXapths> getRawXpaths() {
		return rawXpaths;
	}

	public void setRawXpaths(List<RawFormXapths> rawXpaths) {
		this.rawXpaths = rawXpaths;
	}

	@ManyToOne
	@JoinColumn(name = "AreaLevelId", nullable = false)
	private AreaLevel areaLevel;
	/**
	 * This column will keep the media path of the xForms.
	 * 
	 * @author Subhadarshani Patra (subhadarshani@sdrc.co.in)
	 * @since version 1.0.0.0
	 *
	 */
	@Column(name = "MediaPath")
	private String mediaPath;

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

	public String getxFormIdTitle() {
		return xFormIdTitle;
	}

	public void setxFormIdTitle(String xFormIdTitle) {
		this.xFormIdTitle = xFormIdTitle;
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

	public boolean isSendRawData() {
		return sendRawData;
	}

	public void setSendRawData(boolean sendRawData) {
		this.sendRawData = sendRawData;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Program_XForm_Mapping getProgram_XForm_Mapping() {
		return programXFormMapping;
	}

	public void setProgram_XForm_Mapping(Program_XForm_Mapping program_XForm_Mapping) {
		this.programXFormMapping = program_XForm_Mapping;
	}

	public List<LastVisitData> getLastVisitDatas() {
		return lastVisitDatas;
	}

	public void setLastVisitDatas(List<LastVisitData> lastVisitDatas) {
		this.lastVisitDatas = lastVisitDatas;
	}

	public List<FormXpathScoreMapping> getFormXpathScoreMappings() {
		return formXpathScoreMappings;
	}

	public void setFormXpathScoreMappings(List<FormXpathScoreMapping> formXpathScoreMappings) {
		this.formXpathScoreMappings = formXpathScoreMappings;
	}

	public AreaLevel getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(AreaLevel areaLevel) {
		this.areaLevel = areaLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Area getState() {
		return state;
	}

	public void setState(Area state) {
		this.state = state;
	}

	public String getMarkerClass() {
		return markerClass;
	}

	public void setMarkerClass(String markerClass) {
		this.markerClass = markerClass;
	}

	public int getXform_meta_id() {
		return xform_meta_id;
	}

	public void setXform_meta_id(int xform_meta_id) {
		this.xform_meta_id = xform_meta_id;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

}
