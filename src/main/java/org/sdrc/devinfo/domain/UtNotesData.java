package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_notes_data database table.
 * 
 */
@Entity
@Table(name="ut_notes_data")
@NamedQuery(name="UtNotesData.findAll", query="SELECT u FROM UtNotesData u")
public class UtNotesData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int notes_Data_NId;

	private int data_NId;

	private int notes_NId;

	public UtNotesData() {
	}

	public int getNotes_Data_NId() {
		return this.notes_Data_NId;
	}

	public void setNotes_Data_NId(int notes_Data_NId) {
		this.notes_Data_NId = notes_Data_NId;
	}

	public int getData_NId() {
		return this.data_NId;
	}

	public void setData_NId(int data_NId) {
		this.data_NId = data_NId;
	}

	public int getNotes_NId() {
		return this.notes_NId;
	}

	public void setNotes_NId(int notes_NId) {
		this.notes_NId = notes_NId;
	}

}