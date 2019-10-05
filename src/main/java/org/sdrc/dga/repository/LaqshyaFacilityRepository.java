package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.LaqshyaFacility;

public interface LaqshyaFacilityRepository {

	List<LaqshyaFacility> findAll();
	
	List<LaqshyaFacility> findByIsLiveTrue();
	
	List<LaqshyaFacility> findByFacilityTypeAndIsLiveTrue(String type);
}
