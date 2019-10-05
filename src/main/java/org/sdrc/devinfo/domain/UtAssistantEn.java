package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_assistant_en database table.
 * 
 */
@Entity
@Table(name="ut_assistant_en")
@NamedQuery(name="UtAssistantEn.findAll", query="SELECT u FROM UtAssistantEn u")
public class UtAssistantEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int assistant_NId;

	@Lob
	private String assistant;

	private short assistant_Order;

	private String assistant_Type;

	private int topic_NId;

	public UtAssistantEn() {
	}

	public int getAssistant_NId() {
		return this.assistant_NId;
	}

	public void setAssistant_NId(int assistant_NId) {
		this.assistant_NId = assistant_NId;
	}

	public String getAssistant() {
		return this.assistant;
	}

	public void setAssistant(String assistant) {
		this.assistant = assistant;
	}

	public short getAssistant_Order() {
		return this.assistant_Order;
	}

	public void setAssistant_Order(short assistant_Order) {
		this.assistant_Order = assistant_Order;
	}

	public String getAssistant_Type() {
		return this.assistant_Type;
	}

	public void setAssistant_Type(String assistant_Type) {
		this.assistant_Type = assistant_Type;
	}

	public int getTopic_NId() {
		return this.topic_NId;
	}

	public void setTopic_NId(int topic_NId) {
		this.topic_NId = topic_NId;
	}

}