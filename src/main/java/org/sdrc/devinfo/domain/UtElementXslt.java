package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_element_xslt database table.
 * 
 */
@Entity
@Table(name="ut_element_xslt")
@NamedQuery(name="UtElementXslt.findAll", query="SELECT u FROM UtElementXslt u")
public class UtElementXslt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int element_XSLT_NId;

	private int element_NId;

	private String element_Type;

	private int XSLT_NId;

	public UtElementXslt() {
	}

	public int getElement_XSLT_NId() {
		return this.element_XSLT_NId;
	}

	public void setElement_XSLT_NId(int element_XSLT_NId) {
		this.element_XSLT_NId = element_XSLT_NId;
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

	public int getXSLT_NId() {
		return this.XSLT_NId;
	}

	public void setXSLT_NId(int XSLT_NId) {
		this.XSLT_NId = XSLT_NId;
	}

}