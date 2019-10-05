package org.sdrc.dga.model;


public class FormXpathScoreMappingModel 
{
	
	private int formXpathScoreId;
	
	private String xPath;
	

	private Integer formId;
	
	private Double maxScore;
	
	private String label;
	
	private String type;
	
	private int parentXpathId;
	
	private int form_meta_id;
	
	public String getMarkerClass() {
		return markerClass;
	}

	public void setMarkerClass(String markerClass) {
		this.markerClass = markerClass;
	}

	private String markerClass;
	
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

	public Integer getFormId() {
		return formId;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
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

	public int getParentXpathId() {
		return parentXpathId;
	}

	public void setParentXpathId(int parentXpathId) {
		this.parentXpathId = parentXpathId;
	}

	public int getForm_meta_id() {
		return form_meta_id;
	}

	public void setForm_meta_id(int form_meta_id) {
		this.form_meta_id = form_meta_id;
	}


}
