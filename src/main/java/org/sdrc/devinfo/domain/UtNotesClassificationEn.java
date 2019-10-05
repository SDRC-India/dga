package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_notes_classification_en database table.
 * 
 */
@Entity
@Table(name="ut_notes_classification_en")
@NamedQuery(name="UtNotesClassificationEn.findAll", query="SELECT u FROM UtNotesClassificationEn u")
public class UtNotesClassificationEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int classification_NId;

	private String classification_Name;

	public UtNotesClassificationEn() {
	}

	public int getClassification_NId() {
		return this.classification_NId;
	}

	public void setClassification_NId(int classification_NId) {
		this.classification_NId = classification_NId;
	}

	public String getClassification_Name() {
		return this.classification_Name;
	}

	public void setClassification_Name(String classification_Name) {
		this.classification_Name = classification_Name;
	}

}