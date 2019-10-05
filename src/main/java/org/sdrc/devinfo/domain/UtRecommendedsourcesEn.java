package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_recommendedsources_en database table.
 * 
 */
@Entity
@Table(name="ut_recommendedsources_en")
@NamedQuery(name="UtRecommendedsourcesEn.findAll", query="SELECT u FROM UtRecommendedsourcesEn u")
public class UtRecommendedsourcesEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int RSRC_NId;

	private int data_NId;

	private String IC_IUS_Label;

	public UtRecommendedsourcesEn() {
	}

	public int getRSRC_NId() {
		return this.RSRC_NId;
	}

	public void setRSRC_NId(int RSRC_NId) {
		this.RSRC_NId = RSRC_NId;
	}

	public int getData_NId() {
		return this.data_NId;
	}

	public void setData_NId(int data_NId) {
		this.data_NId = data_NId;
	}

	public String getIC_IUS_Label() {
		return this.IC_IUS_Label;
	}

	public void setIC_IUS_Label(String IC_IUS_Label) {
		this.IC_IUS_Label = IC_IUS_Label;
	}

}