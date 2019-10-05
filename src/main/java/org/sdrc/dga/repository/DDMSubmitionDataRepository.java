package org.sdrc.dga.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.sdrc.dga.domain.DDMSubmitionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DDMSubmitionDataRepository  {

	@Transactional
	DDMSubmitionData save(DDMSubmitionData submitionData);
	
	DDMSubmitionData findByIsLiveTrueAndSubmitionId(Integer id);
	
	List<DDMSubmitionData> findByIsLiveTrue();
	
	DDMSubmitionData findByIsLiveTrueAndCreatedBy(Integer id);
}
