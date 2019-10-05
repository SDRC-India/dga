package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtData;
import org.sdrc.devinfo.repository.Ut_Data_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DevInfoUt_Data_Repository extends JpaRepository<UtData, Integer>,
		Ut_Data_Repository {

	
	@Override
	@Query("SELECT ic.indicator_Name,data.data_Value,ius.subgroup_Val_NId,subgroup.subgroup_Name"
			+ " from "
			+ "UtData data,UtIndicatorEn ic,UtIcIus utIcIus,UtIndicatorUnitSubgroup ius"
			+ ",UtSubgroupEn subgroup "
			+ "where utIcIus.IC_NId=:iCNid "
			+ "AND data.IUSNId=utIcIus.IUSNId "
			+ "AND ius.IUSNId=data.IUSNId "
			+ "AND subgroup.subgroup_NId=ius.subgroup_NIds "
			+ "AND ic.indicator_NId=data.indicator_NId "
			+ "AND data.timePeriod_NId=:timeperiodNid "
			+ "AND data.source_NId=:sourceNid "
			+ "AND data.area_NId=:areaNid ")
	public List<Object[]> getSubmissionSummary(@Param("iCNid")Integer iCNid,@Param("timeperiodNid")
			Integer timeperiodNid,@Param("sourceNid")Integer sourceNid,@Param("areaNid")Integer areaNid);
	
	@Override
	@Query("SELECT ic.indicator_Name,data.data_Value,ius.subgroup_Val_NId,subgroup.subgroup_Name"
			+ " from "
			+ "UtData data,UtIndicatorEn ic,UtIcIus utIcIus,UtIndicatorUnitSubgroup ius"
			+ ",UtSubgroupEn subgroup "
			+ "where utIcIus.IC_NId=:iCNid "
			+ "AND data.IUSNId=utIcIus.IUSNId "
			+ "AND ius.IUSNId=data.IUSNId "
			+ "AND subgroup.subgroup_NId=ius.subgroup_NIds "
			+ "AND ic.indicator_NId=data.indicator_NId "
			+ "AND data.timePeriod_NId=:timeperiodNid "
			+ "AND data.source_NId in :sourceNid "
			+ "AND data.area_NId=:areaNid ")
	public List<Object[]> getSubmissionSummaryForUhc(@Param("iCNid")Integer iCNid,@Param("timeperiodNid")
			Integer timeperiodNid,@Param("sourceNid")List<Integer> sourceNid,@Param("areaNid")Integer areaNid);
	
	@Override
	@Query("SELECT ic.indicator_Name,data.data_Value from "
			+ "UtData data,UtIndicatorEn ic,UtIcIus utIcIus,UtIndicatorUnitSubgroup ius "
			+ "where utIcIus.IC_NId=:iCNid "
			+ "AND data.IUSNId=utIcIus.IUSNId "
			+ "AND ius.subgroup_NIds = 1"
			+ "AND ic.indicator_NId=data.indicator_NId "
			+ "AND data.timePeriod_NId=:timeperiodNid "
			+ "AND data.source_NId=:sourceNid "
			+ "AND data.area_NId=:areaNid ")
	public List<Object[]> getSubmissionSummaryForGeneralInfo(@Param("iCNid")Integer iCNid,@Param("timeperiodNid")
			Integer timeperiodNid,@Param("sourceNid")Integer sourceNid,@Param("areaNid")Integer areaNid);
	
	@Override
	@Query("SELECT ic.indicator_Name,data.data_Value,area.area_Name,subgroup.subgroup_Name,time.timePeriod  , time.timePeriod_NId,area.area_NId "
			+ " from "
			+ "UtData data,UtIndicatorEn ic,UtIcIus utIcIus,UtIndicatorUnitSubgroup ius,UtTimeperiod time"
			+ ",UtSubgroupEn subgroup,UtAreaEn area "
			+ "where utIcIus.IC_NId=:facilityId "
			+ "AND ius.subgroup_NIds = 1"
			+ "AND data.indicator_NId=:colId "
			+ "AND data.IUSNId=utIcIus.IUSNId "
			+ "AND ius.IUSNId=data.IUSNId "
			+ "AND subgroup.subgroup_NId=ius.subgroup_NIds "
			+ "AND ic.indicator_NId=data.indicator_NId "
			+ "AND data.source_NId=:sourceNid "
			+ "AND area.area_Level=5 "
			+ "AND data.area_NId=area.area_NId "
			+ "AND data.timePeriod_NId IN :timePeriods "
			+ " AND time.timePeriod_NId=data.timePeriod_NId"
			+ " ORDER BY ic.indicator_Name , subgroup.subgroup_Name ,data.timePeriod_NId")
	public List<Object[]> getCrossTabData(@Param("facilityId")int facilityId, @Param("colId")int colId,@Param("sourceNid")int sourceNid,@Param("timePeriods") List<Integer> timePeriods);
	
	@Override
	@Query("SELECT ic.indicator_Name,data.data_Value,area.area_Name,subgroup.subgroup_Name,time.timePeriod , time.timePeriod_NId,area.area_NId "
			+ " from "
			+ "UtData data,UtIndicatorEn ic,UtIcIus utIcIus,UtIndicatorUnitSubgroup ius,UtTimeperiod time"
			+ ",UtSubgroupEn subgroup,UtAreaEn area "
			+ "where utIcIus.IC_NId=:facilityId "
			+ "AND ius.subgroup_NIds = 1"
			+ "AND ius.indicator_NId=:colId "
			+ "AND data.IUSNId=utIcIus.IUSNId "
			+ "AND ius.IUSNId=data.IUSNId "
			+ "AND subgroup.subgroup_NId=ius.subgroup_NIds "
			+ "AND ic.indicator_NId=data.indicator_NId "
			+ "AND data.source_NId=:sourceNid "
			+ "AND area.area_Parent_NId IN (SELECT areaen.area_NId From UtAreaEn areaen WHERE areaen.area_Parent_NId=:areaid) "
			+ "AND data.area_NId=area.area_NId "
			+ "AND data.timePeriod_NId IN :timePeriods"
			+ " AND time.timePeriod_NId=data.timePeriod_NId"
			+ " ORDER BY data.timePeriod_NId ASC")
	public List<Object[]> getCrossTabDataForDistrict(@Param("facilityId")int facilityId, @Param("colId")int colId,
			@Param("sourceNid")int sourceNid,@Param("areaid") int areaid,@Param("timePeriods") List<Integer> timePeriods);
	
	@Override
	@Query("SELECT tp.timePeriod_NId ,utIcIus.IC_NId,tp.timePeriod FROM UtData data,UtIcIus utIcIus,UtTimeperiod tp"
			+ " WHERE utIcIus.IUSNId = data.IUSNId "
			+ " AND tp.timePeriod_NId= data.timePeriod_NId"
			+ " GROUP BY tp.timePeriod_NId,utIcIus.IC_NId , tp.timePeriod")
	public List<Object[]> findTimePeriodAndIus();
	
	@Override
	@Query("SELECT area.area_NId FROM UtData data,UtAreaEn area"
			+ " WHERE area.area_NId=data.area_NId "
			+ "AND area.area_Level = 5")
	public List<Integer> findAreaWithData();
	
	
	@Override
	@Query("SELECT MAX(data.timePeriod_NId),MIN(data.timePeriod_NId) FROM UtData data"
			+ " WHERE data.area_NId=:areaid ")
	public List<Object[]> findMaxMinTimePeriodForDistrict(@Param("areaid")int areaid);
	
	
	
	@Override
	@Query("SELECT MAX(data.timePeriod_NId),MIN(data.timePeriod_NId) FROM UtData data")
	public List<Object[]> findMaxMinTimePeriodForState();
}
