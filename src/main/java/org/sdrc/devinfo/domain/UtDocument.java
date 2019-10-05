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
 * The persistent class for the ut_document database table.
 * 
 */
@Entity
@Table(name="ut_document")
@NamedQuery(name="UtDocument.findAll", query="SELECT u FROM UtDocument u")
public class UtDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int document_NId;

	private String document_Type;

	@Lob
	private byte[] element_Document;

	private int element_NId;

	private String element_Type;

	public UtDocument() {
	}

	public int getDocument_NId() {
		return this.document_NId;
	}

	public void setDocument_NId(int document_NId) {
		this.document_NId = document_NId;
	}

	public String getDocument_Type() {
		return this.document_Type;
	}

	public void setDocument_Type(String document_Type) {
		this.document_Type = document_Type;
	}

	public byte[] getElement_Document() {
		return this.element_Document;
	}

	public void setElement_Document(byte[] element_Document) {
		this.element_Document = element_Document;
	}

	public int getElement_NId() {
		return this.element_NId;
	}

	public void setElement_NId(int element_NId) {
		this.element_NId = element_NId;
	}

	public String getElement_Type() {
		return this.element_Type;
	}

	public void setElement_Type(String element_Type) {
		this.element_Type = element_Type;
	}

}