package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_icons database table.
 * 
 */
@Entity
@Table(name="ut_icons")
@NamedQuery(name="UtIcon.findAll", query="SELECT u FROM UtIcon u")
public class UtIcon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int icon_NId;

	@Lob
	private byte[] element_Icon;

	private int element_NId;

	private String element_Type;

	private short icon_Dim_H;

	private short icon_Dim_W;

	private String icon_Type;

	public UtIcon() {
	}

	public int getIcon_NId() {
		return this.icon_NId;
	}

	public void setIcon_NId(int icon_NId) {
		this.icon_NId = icon_NId;
	}

	public byte[] getElement_Icon() {
		return this.element_Icon;
	}

	public void setElement_Icon(byte[] element_Icon) {
		this.element_Icon = element_Icon;
	}

	public int getElement_NId() {
		return this.element_NId;
	}

	public void setElement_NId(int element_NId) {
		this.element_NId = element_NId;
	}

	public String getElement_Type() {
		return this.element_Type;
	}

	public void setElement_Type(String element_Type) {
		this.element_Type = element_Type;
	}

	public short getIcon_Dim_H() {
		return this.icon_Dim_H;
	}

	public void setIcon_Dim_H(short icon_Dim_H) {
		this.icon_Dim_H = icon_Dim_H;
	}

	public short getIcon_Dim_W() {
		return this.icon_Dim_W;
	}

	public void setIcon_Dim_W(short icon_Dim_W) {
		this.icon_Dim_W = icon_Dim_W;
	}

	public String getIcon_Type() {
		return this.icon_Type;
	}

	public void setIcon_Type(String icon_Type) {
		this.icon_Type = icon_Type;
	}

}