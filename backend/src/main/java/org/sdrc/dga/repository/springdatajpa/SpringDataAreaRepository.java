package org.sdrc.dga.repository.springdatajpa;

import java.util.List;

import org.sdrc.dga.domain.Area;
import org.sdrc.dga.repository.AreaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataAreaRepository extends AreaRepository,
		JpaRepository<Area, Integer> {

	@Override
	@Query("SELECT area.areaId FROM Area area WHERE area.parentAreaId=:paremtAreaID")
	public List<Integer> findAreaIdByParentAreaId(@Param("paremtAreaID")Integer paremtAreaID);
}
