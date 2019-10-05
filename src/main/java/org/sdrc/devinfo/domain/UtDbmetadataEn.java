package org.sdrc.devinfo.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ut_dbmetadata_en database table.
 * 
 */
@Entity
@Table(name="ut_dbmetadata_en")
@NamedQuery(name="UtDbmetadataEn.findAll", query="SELECT u FROM UtDbmetadataEn u")
public class UtDbmetadataEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int DBMtd_NId;

	private BigDecimal DBMtd_AreaCnt;

	private BigDecimal DBMtd_DataCnt;

	@Lob
	private String DBMtd_Desc;

	private BigDecimal DBMtd_IndCnt;

	private BigDecimal DBMtd_IUSCnt;

	private String DBMtd_PubCountry;

	@Temporal(TemporalType.TIMESTAMP)
	private Date DBMtd_PubDate;

	private String DBMtd_PubName;

	private String DBMtd_PubOffice;

	private String DBMtd_PubRegion;

	private BigDecimal DBMtd_SrcCnt;

	private BigDecimal DBMtd_TimeCnt;

	public UtDbmetadataEn() {
	}

	public int getDBMtd_NId() {
		return this.DBMtd_NId;
	}

	public void setDBMtd_NId(int DBMtd_NId) {
		this.DBMtd_NId = DBMtd_NId;
	}

	public BigDecimal getDBMtd_AreaCnt() {
		return this.DBMtd_AreaCnt;
	}

	public void setDBMtd_AreaCnt(BigDecimal DBMtd_AreaCnt) {
		this.DBMtd_AreaCnt = DBMtd_AreaCnt;
	}

	public BigDecimal getDBMtd_DataCnt() {
		return this.DBMtd_DataCnt;
	}

	public void setDBMtd_DataCnt(BigDecimal DBMtd_DataCnt) {
		this.DBMtd_DataCnt = DBMtd_DataCnt;
	}

	public String getDBMtd_Desc() {
		return this.DBMtd_Desc;
	}

	public void setDBMtd_Desc(String DBMtd_Desc) {
		this.DBMtd_Desc = DBMtd_Desc;
	}

	public BigDecimal getDBMtd_IndCnt() {
		return this.DBMtd_IndCnt;
	}

	public void setDBMtd_IndCnt(BigDecimal DBMtd_IndCnt) {
		this.DBMtd_IndCnt = DBMtd_IndCnt;
	}

	public BigDecimal getDBMtd_IUSCnt() {
		return this.DBMtd_IUSCnt;
	}

	public void setDBMtd_IUSCnt(BigDecimal DBMtd_IUSCnt) {
		this.DBMtd_IUSCnt = DBMtd_IUSCnt;
	}

	public String getDBMtd_PubCountry() {
		return this.DBMtd_PubCountry;
	}

	public void setDBMtd_PubCountry(String DBMtd_PubCountry) {
		this.DBMtd_PubCountry = DBMtd_PubCountry;
	}

	public Date getDBMtd_PubDate() {
		return this.DBMtd_PubDate;
	}

	public void setDBMtd_PubDate(Date DBMtd_PubDate) {
		this.DBMtd_PubDate = DBMtd_PubDate;
	}

	public String getDBMtd_PubName() {
		return this.DBMtd_PubName;
	}

	public void setDBMtd_PubName(String DBMtd_PubName) {
		this.DBMtd_PubName = DBMtd_PubName;
	}

	public String getDBMtd_PubOffice() {
		return this.DBMtd_PubOffice;
	}

	public void setDBMtd_PubOffice(String DBMtd_PubOffice) {
		this.DBMtd_PubOffice = DBMtd_PubOffice;
	}

	public String getDBMtd_PubRegion() {
		return this.DBMtd_PubRegion;
	}

	public void setDBMtd_PubRegion(String DBMtd_PubRegion) {
		this.DBMtd_PubRegion = DBMtd_PubRegion;
	}

	public BigDecimal getDBMtd_SrcCnt() {
		return this.DBMtd_SrcCnt;
	}

	public void setDBMtd_SrcCnt(BigDecimal DBMtd_SrcCnt) {
		this.DBMtd_SrcCnt = DBMtd_SrcCnt;
	}

	public BigDecimal getDBMtd_TimeCnt() {
		return this.DBMtd_TimeCnt;
	}

	public void setDBMtd_TimeCnt(BigDecimal DBMtd_TimeCnt) {
		this.DBMtd_TimeCnt = DBMtd_TimeCnt;
	}

}