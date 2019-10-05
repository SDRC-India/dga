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
 * The persistent class for the ut_database_log database table.
 * 
 */
@Entity
@Table(name="ut_database_log")
@NamedQuery(name="UtDatabaseLog.findAll", query="SELECT u FROM UtDatabaseLog u")
public class UtDatabaseLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int DB_NId;

	private String DB_Action;

	private String DB_Name;

	@Temporal(TemporalType.TIMESTAMP)
	private Date DB_TimeStamp;

	private String DB_User;

	public UtDatabaseLog() {
	}

	public int getDB_NId() {
		return this.DB_NId;
	}

	public void setDB_NId(int DB_NId) {
		this.DB_NId = DB_NId;
	}

	public String getDB_Action() {
		return this.DB_Action;
	}

	public void setDB_Action(String DB_Action) {
		this.DB_Action = DB_Action;
	}

	public String getDB_Name() {
		return this.DB_Name;
	}

	public void setDB_Name(String DB_Name) {
		this.DB_Name = DB_Name;
	}

	public Date getDB_TimeStamp() {
		return this.DB_TimeStamp;
	}

	public void setDB_TimeStamp(Date DB_TimeStamp) {
		this.DB_TimeStamp = DB_TimeStamp;
	}

	public String getDB_User() {
		return this.DB_User;
	}

	public void setDB_User(String DB_User) {
		this.DB_User = DB_User;
	}

}