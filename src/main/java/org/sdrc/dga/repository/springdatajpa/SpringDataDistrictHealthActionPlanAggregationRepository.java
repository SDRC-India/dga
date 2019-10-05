/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 10-Sep-2019 3:23:10 PM
 */
package org.sdrc.dga.repository.springdatajpa;

import java.util.List;

import org.sdrc.dga.domain.DistrictHealthActionPlanAggregation;
import org.sdrc.dga.repository.DistrictHealthActionPlanAggregationRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataDistrictHealthActionPlanAggregationRepository extends
		DistrictHealthActionPlanAggregationRepository, Repository<DistrictHealthActionPlanAggregation, Integer> {

	@Override
	@Query(value = "SELECT res.district,res.block,res.facility,res.score,res.gap from(SELECT a.AreaName AS facility, "
			+ "(SELECT areaName FROM Area WHERE AreaId=a.ParentAreaId) AS block, "
			+ "(SELECT areaName FROM Area Where AreaId=(SELECT ParentAreaId FROM Area WHERE AreaId=a.ParentAreaId)) AS district, "
			+ "rds.score,(CASE WHEN :formula='lessThan' THEN cast(:requiredScore as int) -score "
			+ "WHEN :formula='NO&BLANK' THEN (CASE WHEN score='no' THEN cast(:requiredScore as int) WHEN score='' THEN cast(:requiredScore as int) "
			+ "WHEN score is NULL THEN cast(:requiredScore as int) WHEN score='2' THEN cast(:requiredScore as int) ELSE 0 END) "
			+ "WHEN :formula='NO' THEN (CASE WHEN score='no' THEN 1 WHEN score='2' THEN 1 ELSE 0 END)"
			+ "WHEN :formula='scoreHasId' THEN 1"
			+ "WHEN :formula='NO&LessThan' THEN (CASE WHEN score='no' THEN cast(:requiredScore as int) WHEN score='2' THEN cast(:requiredScore as int) ELSE 0 END) END) as gap FROM LastVisitData lvd "
			+ "JOIN Area a ON a.AreaId=lvd.AreaId "
			+ "JOIN RawDataScore rds ON rds.lastVisitDataId = lvd.LastVisitDataId "
			+ "WHERE lvd.LastVisitDataId in "
			+ "(SELECT lastVisitDataId FROM RawDataScore WHERE xPathId=:xPathId) "
			+ "AND lvd.FormId=:formId AND lvd.timePeriodId=:timeperiodId AND rds.xPathId=:xPathId) as res WHERE res.gap>0", nativeQuery = true)
	List<Object[]> getAggregatedData(@Param("requiredScore") String requiredScore, @Param("formId") Integer formId,
			@Param("timeperiodId") Integer timeperiodId, @Param("xPathId") Integer xPathId, @Param("formula") String formula);
	
	
	@Override
	@Query(value="SELECT res.district,res.block,res.facility,res.score,res.gap from(SELECT a.AreaName AS facility, "
			+ "(SELECT areaName FROM Area WHERE AreaId=a.ParentAreaId) AS block, "
			+ "(SELECT areaName FROM Area Where AreaId=(SELECT ParentAreaId FROM Area WHERE AreaId=a.ParentAreaId)) AS district, "
			+ "sum(cast(rds.score as int)) as score,cast(:requiredScore as int)-sum(cast(rds.score as int)) as gap FROM LastVisitData lvd "
			+ "JOIN Area a ON a.AreaId=lvd.AreaId "
			+ "JOIN RawDataScore rds ON rds.lastVisitDataId = lvd.LastVisitDataId "
			+ "WHERE lvd.LastVisitDataId in "
			+ "(SELECT lastVisitDataId FROM RawDataScore WHERE xPathId in :xPathIds) "
			+ "AND lvd.FormId=:formId AND lvd.timePeriodId=:timeperiodId AND rds.xPathId in :xPathIds "
			+ "group by a.AreaName, a.ParentAreaId) as res WHERE gap>0 "
			+ "group by res.district,res.block,res.facility,res.score,res.gap", nativeQuery=true)
	List<Object[]> getAggregatedDataForSumOfMultiple(@Param("requiredScore") String requiredScore, @Param("formId") Integer formId,
			@Param("timeperiodId") Integer timeperiodId, @Param("xPathIds") List<Integer> xPathIds);
	
	@Override
	@Query(value="SELECT DISTINCT sector FROM DistrictHealthActionPlanAggregation")
	List<String> findByDistinctSector();
	
}
