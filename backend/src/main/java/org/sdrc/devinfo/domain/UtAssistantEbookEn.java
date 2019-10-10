package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_assistant_ebook_en database table.
 * 
 */
@Entity
@Table(name="ut_assistant_ebook_en")
@NamedQuery(name="UtAssistantEbookEn.findAll", query="SELECT u FROM UtAssistantEbookEn u")
public class UtAssistantEbookEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int ebook_NId;

	@Lob
	private byte[] EBook;

	public UtAssistantEbookEn() {
	}

	public int getEbook_NId() {
		return this.ebook_NId;
	}

	public void setEbook_NId(int ebook_NId) {
		this.ebook_NId = ebook_NId;
	}

	public byte[] getEBook() {
		return this.EBook;
	}

	public void setEBook(byte[] EBook) {
		this.EBook = EBook;
	}

}