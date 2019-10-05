/**
 * 
 */
package org.sdrc.dga.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Entity
@Table
public class ChoicesDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int choiceDetailId;
	
	
	String choicName;
	
	String label;
	
	String choiceValue;
	
	@ManyToOne
	@JoinColumn(name="formId")
	private XForm form; 
	

	public XForm getForm() {
		return form;
	}

	public void setForm(XForm form) {
		this.form = form;
	}

	public String getChoiceValue() {
		return choiceValue;
	}

	public void setChoiceValue(String choiceValue) {
		this.choiceValue = choiceValue;
	}

	public int getChoiceDetailId() {
		return choiceDetailId;
	}

	public void setChoiceDetailId(int choiceDetailId) {
		this.choiceDetailId = choiceDetailId;
	}

	public String getChoicName() {
		return choicName;
	}

	public void setChoicName(String choicName) {
		this.choicName = choicName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
