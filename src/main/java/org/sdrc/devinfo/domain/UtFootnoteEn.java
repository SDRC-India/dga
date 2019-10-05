package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_footnote_en database table.
 * 
 */
@Entity
@Table(name="ut_footnote_en")
@NamedQuery(name="UtFootnoteEn.findAll", query="SELECT u FROM UtFootnoteEn u")
public class UtFootnoteEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int footNote_NId;

	private String footNote;

	private String footNote_GId;

	public UtFootnoteEn() {
	}

	public int getFootNote_NId() {
		return this.footNote_NId;
	}

	public void setFootNote_NId(int footNote_NId) {
		this.footNote_NId = footNote_NId;
	}

	public String getFootNote() {
		return this.footNote;
	}

	public void setFootNote(String footNote) {
		this.footNote = footNote;
	}

	public String getFootNote_GId() {
		return this.footNote_GId;
	}

	public void setFootNote_GId(String footNote_GId) {
		this.footNote_GId = footNote_GId;
	}

}