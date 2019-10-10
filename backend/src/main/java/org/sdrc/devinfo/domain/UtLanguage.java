package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_language database table.
 * 
 */
@Entity
@Table(name="ut_language")
@NamedQuery(name="UtLanguage.findAll", query="SELECT u FROM UtLanguage u")
public class UtLanguage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int language_NId;

	private String language_Code;

	private byte language_Default;

	private byte language_GlobalLock;

	private String language_Name;

	public UtLanguage() {
	}

	public int getLanguage_NId() {
		return this.language_NId;
	}

	public void setLanguage_NId(int language_NId) {
		this.language_NId = language_NId;
	}

	public String getLanguage_Code() {
		return this.language_Code;
	}

	public void setLanguage_Code(String language_Code) {
		this.language_Code = language_Code;
	}

	public byte getLanguage_Default() {
		return this.language_Default;
	}

	public void setLanguage_Default(byte language_Default) {
		this.language_Default = language_Default;
	}

	public byte getLanguage_GlobalLock() {
		return this.language_GlobalLock;
	}

	public void setLanguage_GlobalLock(byte language_GlobalLock) {
		this.language_GlobalLock = language_GlobalLock;
	}

	public String getLanguage_Name() {
		return this.language_Name;
	}

	public void setLanguage_Name(String language_Name) {
		this.language_Name = language_Name;
	}

}