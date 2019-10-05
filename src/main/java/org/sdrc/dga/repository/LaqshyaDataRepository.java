package org.sdrc.dga.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.LaqshyaData;

public interface LaqshyaDataRepository {

	List<LaqshyaData> findAll();

	@Transactional
	LaqshyaData save(LaqshyaData data);
	
	@Transactional
	<S extends LaqshyaData> List<S> save (Iterable<S> datas);
	
	List<LaqshyaData> findByDistrictIdAndIsLiveTrue(Area id);
	
	LaqshyaData findById(int id);
	
	List<LaqshyaData> findByIsLiveTrue();
	
}
