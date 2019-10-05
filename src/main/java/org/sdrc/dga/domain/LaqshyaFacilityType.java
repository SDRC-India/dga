package org.sdrc.dga.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LaqshyaFacilityType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int laqshyaFacilityTypeId;
	
	String facilityTypeName;
	
	boolean isLive;
}
