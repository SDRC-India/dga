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
 * The persistent class for the ut_assistant_topic_en database table.
 * 
 */
@Entity
@Table(name="ut_assistant_topic_en")
@NamedQuery(name="UtAssistantTopicEn.findAll", query="SELECT u FROM UtAssistantTopicEn u")
public class UtAssistantTopicEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int topic_NId;

	private String indicator_GID;

	@Lob
	private String topic_Intro;

	private String topic_Name;

	public UtAssistantTopicEn() {
	}

	public int getTopic_NId() {
		return this.topic_NId;
	}

	public void setTopic_NId(int topic_NId) {
		this.topic_NId = topic_NId;
	}

	public String getIndicator_GID() {
		return this.indicator_GID;
	}

	public void setIndicator_GID(String indicator_GID) {
		this.indicator_GID = indicator_GID;
	}

	public String getTopic_Intro() {
		return this.topic_Intro;
	}

	public void setTopic_Intro(String topic_Intro) {
		this.topic_Intro = topic_Intro;
	}

	public String getTopic_Name() {
		return this.topic_Name;
	}

	public void setTopic_Name(String topic_Name) {
		this.topic_Name = topic_Name;
	}

}