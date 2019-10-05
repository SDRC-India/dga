package org.sdrc.devinfo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ut_timeperiod database table.
 * 
 */
@Entity
@Table(name="ut_timeperiod")
@NamedQuery(name="UtTimeperiod.findAll", query="SELECT u FROM UtTimeperiod u")
public class UtTimeperiod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int timePeriod_NId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	private String periodicity;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	private String timePeriod;

	public UtTimeperiod() {
	}

	public int getTimePeriod_NId() {
		return this.timePeriod_NId;
	}

	public void setTimePeriod_NId(int timePeriod_NId) {
		this.timePeriod_NId = timePeriod_NId;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPeriodicity() {
		return this.periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getTimePeriod() {
		return this.timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

}