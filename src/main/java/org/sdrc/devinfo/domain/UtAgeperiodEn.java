package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_ageperiod_en database table.
 * 
 */
@Entity
@Table(name="ut_ageperiod_en")
@NamedQuery(name="UtAgeperiodEn.findAll", query="SELECT u FROM UtAgeperiodEn u")
public class UtAgeperiodEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int agePeriod_NId;

	private String agePeriod;

	public UtAgeperiodEn() {
	}

	public int getAgePeriod_NId() {
		return this.agePeriod_NId;
	}

	public void setAgePeriod_NId(int agePeriod_NId) {
		this.agePeriod_NId = agePeriod_NId;
	}

	public String getAgePeriod() {
		return this.agePeriod;
	}

	public void setAgePeriod(String agePeriod) {
		this.agePeriod = agePeriod;
	}

}