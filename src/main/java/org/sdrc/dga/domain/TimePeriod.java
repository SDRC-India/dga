/**
 * 
 */
package org.sdrc.dga.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Harsh(harsh@sdrc.co.in)
 * This domain will have the timeperiod of the submission of data for indicators
 */
@Entity
public class TimePeriod implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int timePeriodId;
	
	@Column(nullable=false)
	private Date startDate;
	
	@Column(nullable=false)
	private Date endDate;
	
	@Column(nullable=false)
	private String timeperiod;
	
	@Column(nullable=false)
	private String shortName;
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getTimePeriodId() {
		return timePeriodId;
	}

	public void setTimePeriodId(int timePeriodId) {
		this.timePeriodId = timePeriodId;
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

	public String getTimeperiod() {
		return timeperiod;
	}

	public void setTimeperiod(String timeperiod) {
		this.timeperiod = timeperiod;
	}

	public int getPerodicity() {
		return perodicity;
	}

	public void setPerodicity(int perodicity) {
		this.perodicity = perodicity;
	}

	private int perodicity;
	
}
