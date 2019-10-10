package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the db_version database table.
 * 
 */
@Entity
@Table(name="db_version")
@NamedQuery(name="DbVersion.findAll", query="SELECT d FROM DbVersion d")
public class DbVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int version_NId;

	private String version_Change_Date;

	private String version_Comments;

	private String version_Number;

	public DbVersion() {
	}

	public int getVersion_NId() {
		return this.version_NId;
	}

	public void setVersion_NId(int version_NId) {
		this.version_NId = version_NId;
	}

	public String getVersion_Change_Date() {
		return this.version_Change_Date;
	}

	public void setVersion_Change_Date(String version_Change_Date) {
		this.version_Change_Date = version_Change_Date;
	}

	public String getVersion_Comments() {
		return this.version_Comments;
	}

	public void setVersion_Comments(String version_Comments) {
		this.version_Comments = version_Comments;
	}

	public String getVersion_Number() {
		return this.version_Number;
	}

	public void setVersion_Number(String version_Number) {
		this.version_Number = version_Number;
	}

}