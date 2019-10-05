package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_subgroup_vals_subgroup database table.
 * 
 */
@Entity
@Table(name="ut_subgroup_vals_subgroup")
@NamedQuery(name="UtSubgroupValsSubgroup.findAll", query="SELECT u FROM UtSubgroupValsSubgroup u")
public class UtSubgroupValsSubgroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int subgroup_Val_Subgroup_NId;

	private int subgroup_NId;

	private int subgroup_Val_NId;

	public UtSubgroupValsSubgroup() {
	}

	public int getSubgroup_Val_Subgroup_NId() {
		return this.subgroup_Val_Subgroup_NId;
	}

	public void setSubgroup_Val_Subgroup_NId(int subgroup_Val_Subgroup_NId) {
		this.subgroup_Val_Subgroup_NId = subgroup_Val_Subgroup_NId;
	}

	public int getSubgroup_NId() {
		return this.subgroup_NId;
	}

	public void setSubgroup_NId(int subgroup_NId) {
		this.subgroup_NId = subgroup_NId;
	}

	public int getSubgroup_Val_NId() {
		return this.subgroup_Val_NId;
	}

	public void setSubgroup_Val_NId(int subgroup_Val_NId) {
		this.subgroup_Val_NId = subgroup_Val_NId;
	}

}