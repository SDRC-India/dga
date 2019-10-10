package org.sdrc.dga.domain;

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

/**
 * This Entity class will keep all the area details
 * 
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
@Entity
public class Area {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AreaId", nullable = false)
	private Integer areaId;
	
	@Column(name="AreaName", nullable = false)
	private String areaName;
	
	@Column(name="AreaCode", nullable = false)
	private String areaCode;
	
	@Column(name="ParentAreaId", nullable = false)
	private Integer parentAreaId;
	
	@Column(name="IsLive", nullable = false)
	private Boolean isLive;
	
	@Column(name="CreatedBy")
	private String createdBy;

	@Column(name="CreatedDate")
	private Timestamp createdDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;

	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@OneToMany(mappedBy="area")
	private List<LastVisitData> lastVisitDatas;
	
	@ManyToOne
	@JoinColumn(name="AreaLevelId", nullable = false)
	private AreaLevel areaLevel;

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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getParentAreaId() {
		return parentAreaId;
	}

	public void setParentAreaId(Integer parentAreaId) {
		this.parentAreaId = parentAreaId;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
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

	public List<LastVisitData> getLastVisitDatas() {
		return lastVisitDatas;
	}

	public void setLastVisitDatas(List<LastVisitData> lastVisitDatas) {
		this.lastVisitDatas = lastVisitDatas;
	}

	public AreaLevel getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(AreaLevel areaLevel) {
		this.areaLevel = areaLevel;
	}
	public Area(int areaId)
	{
		this.areaId=areaId;
	}
	
	public Area()
	{
		super();
	}
	
}
