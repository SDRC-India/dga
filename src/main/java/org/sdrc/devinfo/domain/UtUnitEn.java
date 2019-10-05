package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_unit_en database table.
 * 
 */
@Entity
@Table(name="ut_unit_en")
@NamedQuery(name="UtUnitEn.findAll", query="SELECT u FROM UtUnitEn u")
public class UtUnitEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int unit_NId;

	private String unit_GId;

	private byte unit_Global;

	private String unit_Name;

	public UtUnitEn() {
	}

	public int getUnit_NId() {
		return this.unit_NId;
	}

	public void setUnit_NId(int unit_NId) {
		this.unit_NId = unit_NId;
	}

	public String getUnit_GId() {
		return this.unit_GId;
	}

	public void setUnit_GId(String unit_GId) {
		this.unit_GId = unit_GId;
	}

	public byte getUnit_Global() {
		return this.unit_Global;
	}

	public void setUnit_Global(byte unit_Global) {
		this.unit_Global = unit_Global;
	}

	public String getUnit_Name() {
		return this.unit_Name;
	}

	public void setUnit_Name(String unit_Name) {
		this.unit_Name = unit_Name;
	}

}