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
 * The persistent class for the ut_metadatareport_en database table.
 * 
 */
@Entity
@Table(name="ut_metadatareport_en")
@NamedQuery(name="UtMetadatareportEn.findAll", query="SELECT u FROM UtMetadatareportEn u")
public class UtMetadatareportEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int metadataReport_Nid;

	private int category_Nid;

	@Lob
	private String metadata;

	private int target_Nid;

	public UtMetadatareportEn() {
	}

	public int getMetadataReport_Nid() {
		return this.metadataReport_Nid;
	}

	public void setMetadataReport_Nid(int metadataReport_Nid) {
		this.metadataReport_Nid = metadataReport_Nid;
	}

	public int getCategory_Nid() {
		return this.category_Nid;
	}

	public void setCategory_Nid(int category_Nid) {
		this.category_Nid = category_Nid;
	}

	public String getMetadata() {
		return this.metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public int getTarget_Nid() {
		return this.target_Nid;
	}

	public void setTarget_Nid(int target_Nid) {
		this.target_Nid = target_Nid;
	}

}