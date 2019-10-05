package org.sdrc.dga.domain;

import java.sql.Date;
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

import org.springframework.format.annotation.DateTimeFormat;

/**
 * This Entity class will keep last visit data respect to particular xform of particular area
 * This Entity class will update, it is not the final one(temporary) 
 * 
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
@Entity
public class LastVisitData {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="LastVisitDataId")
	private Integer lastVisitDataId;
	
	@Column(name="SubmissionFileURL", nullable = false)
	private String submissionFileURL;
	
	@Column(name="SubmissionFileName", nullable = false)
	private String submissionFileName;
	
	@Column(name="DateOfVisit")
	private Date dateOfVisit;
	
	@Column(name="InstanceId", nullable = false)
	private String instanceId;
	
	@Column(name="SecondaryAreaName")
	private String secondaryAreaName;
	
	@Column(name="Latitude")
	private String latitude;
	
	@Column(name="Longitude")
	private String longitude;
	
	@Column(name="ImageFileNames")
	private String imageFileNames;
	
	@Column(name="RawDataExcelPath")
	private String rawDataExcelPath;
	
	@Column(name="IsLive", nullable = false)
	private boolean isLive;
	
	@Column(name="CreatedBy")
	private String createdBy;

	@Column(name="CreatedDate")
	private Timestamp createdDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;

	@Column(name="UpdatedDate")
	private Timestamp updatedDate;	
	
	@ManyToOne
	@JoinColumn(name="AreaId")
	private Area area;

	@ManyToOne
	@JoinColumn(name="FormId", nullable = false)
	private XForm xForm;
	
	@ManyToOne
	@JoinColumn(name="UserId", nullable = false)
	private CollectUser user;
	
	@DateTimeFormat(pattern="dd/MM/YYYY")
	@Column(name="MarkedAsCompleteDate")
	private Timestamp markedAsCompleteDate;
	
	@ManyToOne
	@JoinColumn(name="timePeriodId")
	private TimePeriod timPeriod;
	
	@OneToMany(mappedBy="lastVisitData")
	private List<RawDataScore> rawDataScore;
	
	public Timestamp getMarkedAsCompleteDate() {
		return markedAsCompleteDate;
	}

	public TimePeriod getTimPeriod() {
		return timPeriod;
	}

	public void setTimPeriod(TimePeriod timPeriod) {
		this.timPeriod = timPeriod;
	}

	public List<RawDataScore> getRawDataScore() {
		return rawDataScore;
	}

	public void setRawDataScore(List<RawDataScore> rawDataScore) {
		this.rawDataScore = rawDataScore;
	}

	public void setMarkedAsCompleteDate(Timestamp markedAsCompleteDate) {
		this.markedAsCompleteDate = markedAsCompleteDate;
	}

	@OneToMany(mappedBy="lastVisitData")
	private List<FacilityScore> facilityScores;
	
	public Integer getLastVisitDataId() {
		return lastVisitDataId;
	}

	public void setLastVisitDataId(Integer lastVisitDataId) {
		this.lastVisitDataId = lastVisitDataId;
	}

	public String getSubmissionFileURL() {
		return submissionFileURL;
	}

	public void setSubmissionFileURL(String submissionFileURL) {
		this.submissionFileURL = submissionFileURL;
	}

	public String getSubmissionFileName() {
		return submissionFileName;
	}

	public void setSubmissionFileName(String submissionFileName) {
		this.submissionFileName = submissionFileName;
	}

	public Date getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(Date dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getSecondaryAreaName() {
		return secondaryAreaName;
	}

	public void setSecondaryAreaName(String secondaryAreaName) {
		this.secondaryAreaName = secondaryAreaName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getImageFileNames() {
		return imageFileNames;
	}

	public void setImageFileNames(String imageFileNames) {
		this.imageFileNames = imageFileNames;
	}

	public String getRawDataExcelPath() {
		return rawDataExcelPath;
	}

	public void setRawDataExcelPath(String rawDataExcelPath) {
		this.rawDataExcelPath = rawDataExcelPath;
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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public XForm getxForm() {
		return xForm;
	}

	public void setxForm(XForm xForm) {
		this.xForm = xForm;
	}

	public CollectUser getUser() {
		return user;
	}

	public void setUser(CollectUser user) {
		this.user = user;
	}

	public List<FacilityScore> getFacilityScores() {
		return facilityScores;
	}

	public void setFacilityScores(List<FacilityScore> facilityScores) {
		this.facilityScores = facilityScores;
	}
}
