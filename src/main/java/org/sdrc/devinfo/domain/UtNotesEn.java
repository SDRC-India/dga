package org.sdrc.devinfo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ut_notes_en database table.
 * 
 */
@Entity
@Table(name="ut_notes_en")
@NamedQuery(name="UtNotesEn.findAll", query="SELECT u FROM UtNotesEn u")
public class UtNotesEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int notes_NId;

	private int classification_NId;

	@Lob
	private String notes;

	private byte notes_Approved;

	@Temporal(TemporalType.TIMESTAMP)
	private Date notes_DateTime;

	private int profile_NId;

	public UtNotesEn() {
	}

	public int getNotes_NId() {
		return this.notes_NId;
	}

	public void setNotes_NId(int notes_NId) {
		this.notes_NId = notes_NId;
	}

	public int getClassification_NId() {
		return this.classification_NId;
	}

	public void setClassification_NId(int classification_NId) {
		this.classification_NId = classification_NId;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public byte getNotes_Approved() {
		return this.notes_Approved;
	}

	public void setNotes_Approved(byte notes_Approved) {
		this.notes_Approved = notes_Approved;
	}

	public Date getNotes_DateTime() {
		return this.notes_DateTime;
	}

	public void setNotes_DateTime(Date notes_DateTime) {
		this.notes_DateTime = notes_DateTime;
	}

	public int getProfile_NId() {
		return this.profile_NId;
	}

	public void setProfile_NId(int profile_NId) {
		this.profile_NId = profile_NId;
	}

}