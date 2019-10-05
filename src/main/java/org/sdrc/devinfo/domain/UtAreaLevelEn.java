package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_area_level_en database table.
 * 
 */
@Entity
@Table(name="ut_area_level_en")
@NamedQuery(name="UtAreaLevelEn.findAll", query="SELECT u FROM UtAreaLevelEn u")
public class UtAreaLevelEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int level_NId;

	private int area_Level;

	private String area_Level_Name;

	public UtAreaLevelEn() {
	}

	public int getLevel_NId() {
		return this.level_NId;
	}

	public void setLevel_NId(int level_NId) {
		this.level_NId = level_NId;
	}

	public int getArea_Level() {
		return this.area_Level;
	}

	public void setArea_Level(int area_Level) {
		this.area_Level = area_Level;
	}

	public String getArea_Level_Name() {
		return this.area_Level_Name;
	}

	public void setArea_Level_Name(String area_Level_Name) {
		this.area_Level_Name = area_Level_Name;
	}

}