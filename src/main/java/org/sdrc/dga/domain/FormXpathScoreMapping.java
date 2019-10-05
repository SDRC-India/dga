package org.sdrc.dga.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This domain class or entity class will keep all the mappings for form and score xpaths.
 * @author Sarita
 * @since version 1.0.0.0
 *
 */

@Entity
@Table
public class FormXpathScoreMapping implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private int formXpathScoreId;
	
	private String xPath;
	
	@ManyToOne
	@JoinColumn(name="formId")
	private XForm form;
	
	private Double maxScore;
	
	private String label;
	
	private String type;
	
	@OneToMany(mappedBy="formXpathScoreMapping")
	private List<FacilityScore> facilityScores;
	
	private int parentXpathId;
	
	private String maxCalXpath;
	
	public int getFormXpathScoreId() {
		return formXpathScoreId;
	}

	public void setFormXpathScoreId(int formXpathScoreId) {
		this.formXpathScoreId = formXpathScoreId;
	}

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}

	public XForm getForm() {
		return form;
	}

	public void setForm(XForm form) {
		this.form = form;
	}

	public Double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FacilityScore> getFacilityScores() {
		return facilityScores;
	}

	public void setFacilityScores(List<FacilityScore> facilityScores) {
		this.facilityScores = facilityScores;
	}

	public int getParentXpathId() {
		return parentXpathId;
	}

	public void setParentXpathId(int parentXpathId) {
		this.parentXpathId = parentXpathId;
	}

	public String getMaxCalXpath() {
		return maxCalXpath;
	}

	public void setMaxCalXpath(String maxCalXpath) {
		this.maxCalXpath = maxCalXpath;
	}
	
	

}
