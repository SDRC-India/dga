package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the v_esamiksha_area_mapper database table.
 * 
 */
@Entity
@Table(name="v_esamiksha_area_mapper")
@NamedQuery(name="VEsamikshaAreaMapper.findAll", query="SELECT v FROM VEsamikshaAreaMapper v")
public class VEsamikshaAreaMapper implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer act_Level;

	private String area_ID;

	private Integer area_Level;

	private String area_Name;

	private Integer area_NId;

	private Integer area_Parent_NId;

	private String child_Area;

	private String child_Area_ID;

	public VEsamikshaAreaMapper() {
	}

	public Integer getAct_Level() {
		return this.act_Level;
	}

	public void setAct_Level(Integer act_Level) {
		this.act_Level = act_Level;
	}

	public String getArea_ID() {
		return this.area_ID;
	}

	public void setArea_ID(String area_ID) {
		this.area_ID = area_ID;
	}

	public Integer getArea_Level() {
		return this.area_Level;
	}

	public void setArea_Level(Integer area_Level) {
		this.area_Level = area_Level;
	}

	public String getArea_Name() {
		return this.area_Name;
	}

	public void setArea_Name(String area_Name) {
		this.area_Name = area_Name;
	}

	@Id
    @Column(name = "area_NId", nullable = false)
	public Integer getArea_NId() {
		return this.area_NId;
	}

	public void setArea_NId(Integer area_NId) {
		this.area_NId = area_NId;
	}

	public Integer getArea_Parent_NId() {
		return this.area_Parent_NId;
	}

	public void setArea_Parent_NId(Integer area_Parent_NId) {
		this.area_Parent_NId = area_Parent_NId;
	}

	public String getChild_Area() {
		return this.child_Area;
	}

	public void setChild_Area(String child_Area) {
		this.child_Area = child_Area;
	}

	public String getChild_Area_ID() {
		return this.child_Area_ID;
	}

	public void setChild_Area_ID(String child_Area_ID) {
		this.child_Area_ID = child_Area_ID;
	}

}