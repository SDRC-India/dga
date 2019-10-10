package org.sdrc.dga.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

/**
 * 
 * @author Rajanikanta Sahoo
 *
 */

@Entity
@Data
@Table(name = "ddm_submition_data")
public class DDMSubmitionData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int submitionId;

	@ManyToOne
	@JoinColumn(name = "q1", nullable = false)
	private Area q1;

	
	@OneToMany(mappedBy = "ddmSubmitionData")
	private List<DDMActionItem> actionItem;
	
	private Integer createdBy;

	@UpdateTimestamp
	private Date updatedDate;

	private Integer updatedBy;

	private boolean isLive;

	private byte isDeleted = 0;

	public String toString() {
		return submitionId + "";
	}

}
