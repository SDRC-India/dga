package org.sdrc.dga.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class will hold all the answer to question in xform
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@Entity
@Table(indexes = {@Index(columnList = "xPathId,lastVisitDataId", name = "xpath_lastVisit_index"),@Index(columnList = "xPathId", name = "xpath_index")
,@Index(columnList = "lastVisitDataId", name = "lastVisit_index")})
public class RawDataScore {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rawDataScoreId;

	@ManyToOne
	@JoinColumn(name="xPathId")
	private RawFormXapths rawFormXapths;
	
	public int getRawDataScoreId() {
		return rawDataScoreId;
	}

	public void setRawDataScoreId(int rawDataScoreId) {
		this.rawDataScoreId = rawDataScoreId;
	}

	public RawFormXapths getRawFormXapths() {
		return rawFormXapths;
	}

	public void setRawFormXapths(RawFormXapths rawFormXapths) {
		this.rawFormXapths = rawFormXapths;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public LastVisitData getLastVisitData() {
		return lastVisitData;
	}

	public void setLastVisitData(LastVisitData lastVisitData) {
		this.lastVisitData = lastVisitData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String score;
	
	@ManyToOne
	@JoinColumn(name="lastVisitDataId")
	private LastVisitData lastVisitData;

}
