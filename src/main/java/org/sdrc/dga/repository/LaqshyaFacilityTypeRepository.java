package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.LaqshyaFacilityType;

public interface LaqshyaFacilityTypeRepository {

	List<LaqshyaFacilityType> findAll();
	
	List<LaqshyaFacilityType> findByIsLiveTrue();
	
	LaqshyaFacilityType findByFacilityTypeName(String facilityName);
}
