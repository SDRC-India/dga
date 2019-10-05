package org.sdrc.dga.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class DDMActionItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int actionItemId;

	@ManyToOne
	@JoinColumn(name = "submission_id_fk")
	private DDMSubmitionData ddmSubmitionData;

	private boolean isLive;
	
	@ManyToOne
	@JoinColumn(name = "q3", nullable = false)
	private Area q3;
	
	@ManyToOne
	@JoinColumn(name = "q4", nullable = false)
	private Area q4;
	
	private String q5;
	
	@ManyToOne
	@JoinColumn(name = "q6")
	private DDMOptions q6;
	
	private String q7;
	
	private int indexTrackNum;

	@Column( columnDefinition = "bit DEFAULT 1")
	private boolean removable = true;
	
	public String toString() {
		return actionItemId+"";
	}
}
