package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_area_map database table.
 * 
 */
@Entity
@Table(name="ut_area_map")
@NamedQuery(name="UtAreaMap.findAll", query="SELECT u FROM UtAreaMap u")
public class UtAreaMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int area_Map_NId;

	private int area_NId;

	private byte feature_Layer;

	private int feature_Type_NId;

	private int layer_NId;

	public UtAreaMap() {
	}

	public int getArea_Map_NId() {
		return this.area_Map_NId;
	}

	public void setArea_Map_NId(int area_Map_NId) {
		this.area_Map_NId = area_Map_NId;
	}

	public int getArea_NId() {
		return this.area_NId;
	}

	public void setArea_NId(int area_NId) {
		this.area_NId = area_NId;
	}

	public byte getFeature_Layer() {
		return this.feature_Layer;
	}

	public void setFeature_Layer(byte feature_Layer) {
		this.feature_Layer = feature_Layer;
	}

	public int getFeature_Type_NId() {
		return this.feature_Type_NId;
	}

	public void setFeature_Type_NId(int feature_Type_NId) {
		this.feature_Type_NId = feature_Type_NId;
	}

	public int getLayer_NId() {
		return this.layer_NId;
	}

	public void setLayer_NId(int layer_NId) {
		this.layer_NId = layer_NId;
	}

}