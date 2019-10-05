package org.sdrc.dga.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
/**
 * 
 * @author Harsh(harsh@sdrc.co.in)
 *
 */
@Entity
public class FacilityPlanning 
{
	@Column
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int facilityPlannedId;
	
	@Column
	private int facilityPlanned;
	
	@Column
	private int timeperiodNId;
	
	@ManyToOne
	@JoinColumn(name="AreaId")
	private Area area;
	
	@ManyToOne
	@JoinColumn(name="FormId")
	private XForm xForm;
	
	@ManyToOne
	@JoinColumn(name="timePeriodId",columnDefinition = "int default 1")
	private TimePeriod timPeriod;

	public TimePeriod getTimPeriod() {
		return timPeriod;
	}

	public void setTimPeriod(TimePeriod timPeriod) {
		this.timPeriod = timPeriod;
	}

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

	public int getTimeperiodNId() {
		return timeperiodNId;
	}

	public void setTimeperiodNId(int timeperiodNId) {
		this.timeperiodNId = timeperiodNId;
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
	
	
	
}
