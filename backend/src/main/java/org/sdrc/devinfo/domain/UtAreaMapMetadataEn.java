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
 * The persistent class for the ut_area_map_metadata_en database table.
 * 
 */
@Entity
@Table(name="ut_area_map_metadata_en")
@NamedQuery(name="UtAreaMapMetadataEn.findAll", query="SELECT u FROM UtAreaMapMetadataEn u")
public class UtAreaMapMetadataEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int metadata_NId;

	private String layer_Name;

	private int layer_NId;

	@Lob
	private String metadata_Text;

	public UtAreaMapMetadataEn() {
	}

	public int getMetadata_NId() {
		return this.metadata_NId;
	}

	public void setMetadata_NId(int metadata_NId) {
		this.metadata_NId = metadata_NId;
	}

	public String getLayer_Name() {
		return this.layer_Name;
	}

	public void setLayer_Name(String layer_Name) {
		this.layer_Name = layer_Name;
	}

	public int getLayer_NId() {
		return this.layer_NId;
	}

	public void setLayer_NId(int layer_NId) {
		this.layer_NId = layer_NId;
	}

	public String getMetadata_Text() {
		return this.metadata_Text;
	}

	public void setMetadata_Text(String metadata_Text) {
		this.metadata_Text = metadata_Text;
	}

}