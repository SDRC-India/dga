package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtAreaEn;
import org.sdrc.devinfo.repository.UtAreaEnRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DevInfoUtAreaEnRepository extends
		JpaRepository<UtAreaEn, Integer>, UtAreaEnRepository {

	@Override
	@Query("Select area from UtAreaEn area where area.area_Level=:level")
	public List<UtAreaEn> getAllAreaByLevel(@Param("level") Integer level);

	@Override
	@Query("Select area from UtAreaEn area where area.area_Parent_NId IN"
			+ "(Select area.area_NId from UtAreaEn area where area.area_Parent_NId=:parentId)")
	public List<UtAreaEn> findByArea_Parent_NId(
			@Param("parentId") int parentId);

}
