package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_subgroup_vals_en database table.
 * 
 */
@Entity
@Table(name="ut_subgroup_vals_en")
@NamedQuery(name="UtSubgroupValsEn.findAll", query="SELECT u FROM UtSubgroupValsEn u")
public class UtSubgroupValsEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int subgroup_Val_NId;

	private String subgroup_Val;

	private String subgroup_Val_GId;

	private byte subgroup_Val_Global;

	private int subgroup_Val_Order;

	public UtSubgroupValsEn() {
	}

	public int getSubgroup_Val_NId() {
		return this.subgroup_Val_NId;
	}

	public void setSubgroup_Val_NId(int subgroup_Val_NId) {
		this.subgroup_Val_NId = subgroup_Val_NId;
	}

	public String getSubgroup_Val() {
		return this.subgroup_Val;
	}

	public void setSubgroup_Val(String subgroup_Val) {
		this.subgroup_Val = subgroup_Val;
	}

	public String getSubgroup_Val_GId() {
		return this.subgroup_Val_GId;
	}

	public void setSubgroup_Val_GId(String subgroup_Val_GId) {
		this.subgroup_Val_GId = subgroup_Val_GId;
	}

	public byte getSubgroup_Val_Global() {
		return this.subgroup_Val_Global;
	}

	public void setSubgroup_Val_Global(byte subgroup_Val_Global) {
		this.subgroup_Val_Global = subgroup_Val_Global;
	}

	public int getSubgroup_Val_Order() {
		return this.subgroup_Val_Order;
	}

	public void setSubgroup_Val_Order(int subgroup_Val_Order) {
		this.subgroup_Val_Order = subgroup_Val_Order;
	}

}