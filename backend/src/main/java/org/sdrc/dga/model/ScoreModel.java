package org.sdrc.dga.model;

public class ScoreModel {

	private String name;
	private Integer formXpathScoreId;
	private Integer parentXpathScoreId;
	private Double score;
	private Double maxScore;
	private String percentScore;
	
	public String getPercentScore() {
		return percentScore;
	}
	public void setPercentScore(String percentScore) {
		this.percentScore = percentScore;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFormXpathScoreId() {
		return formXpathScoreId;
	}
	public void setFormXpathScoreId(Integer formXpathScoreId) {
		this.formXpathScoreId = formXpathScoreId;
	}
	public Integer getParentXpathScoreId() {
		return parentXpathScoreId;
	}
	public void setParentXpathScoreId(Integer parentXpathScoreId) {
		this.parentXpathScoreId = parentXpathScoreId;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Double getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
	}
	
	
}
