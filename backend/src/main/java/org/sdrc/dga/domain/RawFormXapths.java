package org.sdrc.dga.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This persistent class will hold the xpaths of all the forms
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@Entity
@Table(name="RawFormXpaths")
public class RawFormXapths 
{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer xPathId;
	
	private String xpath;
	
	@Column(length=700)
	private String label;
	
	private String type;
	
	private String calXpaths;
	
	@ManyToOne
	@JoinColumn(name="formId")
	private XForm form; 
	

	
	@OneToMany(mappedBy="rawFormXapths")
	private List<RawDataScore> rawDataScore;
	
	// getter setters
	
	public String getCalXpaths() {
		return calXpaths;
	}

	public void setCalXpaths(String calXpaths) {
		this.calXpaths = calXpaths;
	}

	
	public List<RawDataScore> getRawDataScore() {
		return rawDataScore;
	}

	public void setRawDataScore(List<RawDataScore> rawDataScore) {
		this.rawDataScore = rawDataScore;
	}

	public XForm getForm() {
		return form;
	}

	public void setForm(XForm form) {
		this.form = form;
	}

	public Integer getxPathId() {
		return xPathId;
	}

	public void setxPathId(Integer xPathId) {
		this.xPathId = xPathId;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
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
	
	
}
