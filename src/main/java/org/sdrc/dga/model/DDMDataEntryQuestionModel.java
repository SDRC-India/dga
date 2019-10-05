package org.sdrc.dga.model;

import java.util.List;
/**
 * 
 * @author Rajanikanta Sahoo
 *
 */

public class DDMDataEntryQuestionModel {

	private int id;
	private String name;
	private List<DDMQuestionModel> questions;
	private boolean isRejected;
	private int sectionOrder;
	private String rejectedRemark;
	private boolean disabled;
	private boolean submitDisabled=false;
	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRejectedRemark() {
		return rejectedRemark;
	}
	public void setRejectedRemark(String rejectedRemark) {
		this.rejectedRemark = rejectedRemark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DDMQuestionModel> getQuestions() {
		return questions;
	}
	public void setQuestions(List<DDMQuestionModel> questions) {
		this.questions = questions;
	}
	public boolean isRejected() {
		return isRejected;
	}
	public void setRejected(boolean isRejected) {
		this.isRejected = isRejected;
	}
	public int getSectionOrder() {
		return sectionOrder;
	}
	public void setSectionOrder(int sectionOrder) {
		this.sectionOrder = sectionOrder;
	}
	/**
	 * @return the submitDisabled
	 */
	public boolean isSubmitDisabled() {
		return submitDisabled;
	}
	/**
	 * @param submitDisabled the submitDisabled to set
	 */
	public void setSubmitDisabled(boolean submitDisabled) {
		this.submitDisabled = submitDisabled;
	}

}
