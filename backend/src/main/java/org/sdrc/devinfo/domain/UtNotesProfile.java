package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_notes_profile database table.
 * 
 */
@Entity
@Table(name="ut_notes_profile")
@NamedQuery(name="UtNotesProfile.findAll", query="SELECT u FROM UtNotesProfile u")
public class UtNotesProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int profile_NId;

	private String profile_Country;

	private String profile_EMail;

	private String profile_Name;

	private String profile_Org;

	private String profile_Org_Type;

	public UtNotesProfile() {
	}

	public int getProfile_NId() {
		return this.profile_NId;
	}

	public void setProfile_NId(int profile_NId) {
		this.profile_NId = profile_NId;
	}

	public String getProfile_Country() {
		return this.profile_Country;
	}

	public void setProfile_Country(String profile_Country) {
		this.profile_Country = profile_Country;
	}

	public String getProfile_EMail() {
		return this.profile_EMail;
	}

	public void setProfile_EMail(String profile_EMail) {
		this.profile_EMail = profile_EMail;
	}

	public String getProfile_Name() {
		return this.profile_Name;
	}

	public void setProfile_Name(String profile_Name) {
		this.profile_Name = profile_Name;
	}

	public String getProfile_Org() {
		return this.profile_Org;
	}

	public void setProfile_Org(String profile_Org) {
		this.profile_Org = profile_Org;
	}

	public String getProfile_Org_Type() {
		return this.profile_Org_Type;
	}

	public void setProfile_Org_Type(String profile_Org_Type) {
		this.profile_Org_Type = profile_Org_Type;
	}

}