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
 * The persistent class for the ut_xslt database table.
 * 
 */
@Entity
@Table(name="ut_xslt")
@NamedQuery(name="UtXslt.findAll", query="SELECT u FROM UtXslt u")
public class UtXslt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int XSLT_NId;

	private String XSLT_File;

	@Lob
	private String XSLT_Text;

	public UtXslt() {
	}

	public int getXSLT_NId() {
		return this.XSLT_NId;
	}

	public void setXSLT_NId(int XSLT_NId) {
		this.XSLT_NId = XSLT_NId;
	}

	public String getXSLT_File() {
		return this.XSLT_File;
	}

	public void setXSLT_File(String XSLT_File) {
		this.XSLT_File = XSLT_File;
	}

	public String getXSLT_Text() {
		return this.XSLT_Text;
	}

	public void setXSLT_Text(String XSLT_Text) {
		this.XSLT_Text = XSLT_Text;
	}

}