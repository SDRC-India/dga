package org.sdrc.dga.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


/**
 * This Entity class will keep all the area level available in our system
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
@Entity
public class AreaLevel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "AreaLevelId")
	private Integer areaLevelId;
	
	@Column(name = "AreaLevel", nullable = false, unique = true)
	private Integer areaLevel;
	
	@Column(name = "AreaLevelName", nullable = false)
	private String areaLevelName;
	
	@Column(name = "ParentAreaLevel", nullable = false)
	private Integer parentAreaLevel;
	
	@OneToMany(mappedBy="areaLevel")
	private List<XForm> xForms;
	
	@OneToMany(mappedBy="areaLevel")
	private List<Area> areas;

	public Integer getAreaLevelId() {
		return areaLevelId;
	}

	public void setAreaLevelId(Integer areaLevelId) {
		this.areaLevelId = areaLevelId;
	}

	public Integer getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(Integer areaLevel) {
		this.areaLevel = areaLevel;
	}

	public String getAreaLevelName() {
		return areaLevelName;
	}

	public void setAreaLevelName(String areaLevelName) {
		this.areaLevelName = areaLevelName;
	}

	public Integer getParentAreaLevel() {
		return parentAreaLevel;
	}

	public void setParentAreaLevel(Integer parentAreaLevel) {
		this.parentAreaLevel = parentAreaLevel;
	}

	public List<XForm> getxForms() {
		return xForms;
	}

	public void setxForms(List<XForm> xForms) {
		this.xForms = xForms;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public AreaLevel() {
		super();
	}

	public AreaLevel(Integer areaLevelId) {
		super();
		this.areaLevelId = areaLevelId;
	}
	
}
