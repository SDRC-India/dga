package org.sdrc.devinfo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ut_template_log database table.
 * 
 */
@Entity
@Table(name="ut_template_log")
@NamedQuery(name="UtTemplateLog.findAll", query="SELECT u FROM UtTemplateLog u")
public class UtTemplateLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int TPL_NId;

	private String TPL_Action;

	private String TPL_Name;

	@Temporal(TemporalType.TIMESTAMP)
	private Date TPL_TimeStamp;

	private String TPL_User;

	public UtTemplateLog() {
	}

	public int getTPL_NId() {
		return this.TPL_NId;
	}

	public void setTPL_NId(int TPL_NId) {
		this.TPL_NId = TPL_NId;
	}

	public String getTPL_Action() {
		return this.TPL_Action;
	}

	public void setTPL_Action(String TPL_Action) {
		this.TPL_Action = TPL_Action;
	}

	public String getTPL_Name() {
		return this.TPL_Name;
	}

	public void setTPL_Name(String TPL_Name) {
		this.TPL_Name = TPL_Name;
	}

	public Date getTPL_TimeStamp() {
		return this.TPL_TimeStamp;
	}

	public void setTPL_TimeStamp(Date TPL_TimeStamp) {
		this.TPL_TimeStamp = TPL_TimeStamp;
	}

	public String getTPL_User() {
		return this.TPL_User;
	}

	public void setTPL_User(String TPL_User) {
		this.TPL_User = TPL_User;
	}

}