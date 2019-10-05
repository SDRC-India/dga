package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_area_feature_type_en database table.
 * 
 */
@Entity
@Table(name="ut_area_feature_type_en")
@NamedQuery(name="UtAreaFeatureTypeEn.findAll", query="SELECT u FROM UtAreaFeatureTypeEn u")
public class UtAreaFeatureTypeEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int feature_Type_NId;

	private String feature_Type;

	public UtAreaFeatureTypeEn() {
	}

	public int getFeature_Type_NId() {
		return this.feature_Type_NId;
	}

	public void setFeature_Type_NId(int feature_Type_NId) {
		this.feature_Type_NId = feature_Type_NId;
	}

	public String getFeature_Type() {
		return this.feature_Type;
	}

	public void setFeature_Type(String feature_Type) {
		this.feature_Type = feature_Type;
	}

}