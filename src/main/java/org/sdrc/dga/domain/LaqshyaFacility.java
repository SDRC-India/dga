package org.sdrc.dga.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class LaqshyaFacility {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int laqshyaFacilityId;
	
	@ManyToOne
	@JoinColumn(name="AreaId")
	private Area districtId;
	
	String facilityName;
	
	String facilityType;
	
	boolean isLive;
}
