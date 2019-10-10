package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the db_available_databases database table.
 * 
 */
@Entity
@Table(name="db_available_databases")
@NamedQuery(name="DbAvailableDatabas.findAll", query="SELECT d FROM DbAvailableDatabas d")
public class DbAvailableDatabas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int avlDB_NId;

	private byte avlDB_Default;

	private String avlDB_Name;

	private String avlDB_Prefix;

	private String usr_Name;

	private String usr_Protection;

	private String usr_Pwd;

	public DbAvailableDatabas() {
	}

	public int getAvlDB_NId() {
		return this.avlDB_NId;
	}

	public void setAvlDB_NId(int avlDB_NId) {
		this.avlDB_NId = avlDB_NId;
	}

	public byte getAvlDB_Default() {
		return this.avlDB_Default;
	}

	public void setAvlDB_Default(byte avlDB_Default) {
		this.avlDB_Default = avlDB_Default;
	}

	public String getAvlDB_Name() {
		return this.avlDB_Name;
	}

	public void setAvlDB_Name(String avlDB_Name) {
		this.avlDB_Name = avlDB_Name;
	}

	public String getAvlDB_Prefix() {
		return this.avlDB_Prefix;
	}

	public void setAvlDB_Prefix(String avlDB_Prefix) {
		this.avlDB_Prefix = avlDB_Prefix;
	}

	public String getUsr_Name() {
		return this.usr_Name;
	}

	public void setUsr_Name(String usr_Name) {
		this.usr_Name = usr_Name;
	}

	public String getUsr_Protection() {
		return this.usr_Protection;
	}

	public void setUsr_Protection(String usr_Protection) {
		this.usr_Protection = usr_Protection;
	}

	public String getUsr_Pwd() {
		return this.usr_Pwd;
	}

	public void setUsr_Pwd(String usr_Pwd) {
		this.usr_Pwd = usr_Pwd;
	}

}