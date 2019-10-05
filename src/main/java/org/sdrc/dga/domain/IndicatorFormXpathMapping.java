/**
 * 
 */
package org.sdrc.dga.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@Entity
public class IndicatorFormXpathMapping {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int indicatorFormXpathMappingId;

	private String dhXpath;

	private String chcXpath;

	private String phcXpath;
	
	private String hscXpath;

	public String getHscXpath() {
		return hscXpath;
	}

	public void setHscXpath(String hscXpath) {
		this.hscXpath = hscXpath;
	}

	@Column(nullable = false)
	private String label;
	
	@Column(nullable = false)
	private String sector;
	
	private String subSector;
	
	@Column(nullable = false)
	private String subGroup;
	
	@Column(nullable = false)
	private String type;

	public int getIndicatorFormXpathMappingId() {
		return indicatorFormXpathMappingId;
	}

	public void setIndicatorFormXpathMappingId(int indicatorFormXpathMappingId) {
		this.indicatorFormXpathMappingId = indicatorFormXpathMappingId;
	}

	public String getDhXpath() {
		return dhXpath;
	}

	public void setDhXpath(String dhXpath) {
		this.dhXpath = dhXpath;
	}

	public String getChcXpath() {
		return chcXpath;
	}

	public void setChcXpath(String chcXpath) {
		this.chcXpath = chcXpath;
	}

	public String getPhcXpath() {
		return phcXpath;
	}

	public void setPhcXpath(String phcXpath) {
		this.phcXpath = phcXpath;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getSubSector() {
		return subSector;
	}

	public void setSubSector(String subSector) {
		this.subSector = subSector;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
