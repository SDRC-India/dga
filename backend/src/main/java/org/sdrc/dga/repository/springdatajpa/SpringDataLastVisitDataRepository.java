package org.sdrc.dga.repository.springdatajpa;

import java.util.List;
import java.util.Set;

import org.sdrc.dga.domain.LastVisitData;
import org.sdrc.dga.repository.LastVisitDataRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataLastVisitDataRepository extends
		LastVisitDataRepository, Repository<LastVisitData, Integer> {
	
	@Override
	@Query("SELECT lastVisitData, score, xForm, formScore,(score.score / score.maxScore)*100 FROM LastVisitData lastVisitData,"
			+" FacilityScore score, XForm xForm, FormXpathScoreMapping formScore "
			+" WHERE lastVisitData.isLive IS TRUE " 
			+" AND lastVisitData.lastVisitDataId = score.lastVisitData.lastVisitDataId "
			+" AND lastVisitData.xForm.formId = xForm.formId "
			+ "AND formScore.formXpathScoreId =:sectorId"
			+" AND score.formXpathScoreMapping.formXpathScoreId = formScore.formXpathScoreId "
			+" AND xForm.formId = :formId"
			+ " AND lastVisitData.timPeriod.timePeriodId = :timeperiodId")
	List<Object[]> getDataByFormId(@Param("formId") Integer formId,@Param("sectorId")Integer sectorId ,@Param("timeperiodId") int timePeriod) throws DataAccessException;

	@Override
	@Query("SELECT lastVisitData, score, formScore, (score.score/score.maxScore)*100 FROM LastVisitData lastVisitData, FacilityScore score, FormXpathScoreMapping formScore "
	+" WHERE lastVisitData.isLive IS TRUE " 
	+" AND lastVisitData.lastVisitDataId = score.lastVisitData.lastVisitDataId "
	+" AND score.formXpathScoreMapping.formXpathScoreId = formScore.formXpathScoreId "
	+" AND lastVisitData.lastVisitDataId = :lastVisitDataId")
	List<Object[]> getByLastVisitData(@Param("lastVisitDataId") Integer lastVisitDataId) throws DataAccessException;
	
	
	@Override
	@Query("SELECT lastVisitData, score, xForm, formScore,(score.score / score.maxScore)*100 FROM LastVisitData lastVisitData,"
			+" FacilityScore score, XForm xForm, FormXpathScoreMapping formScore "
			+" WHERE lastVisitData.isLive IS TRUE "
			+ "AND lastVisitData.area.parentAreaId=:areaId " 
			+" AND score.lastVisitData.lastVisitDataId = lastVisitData.lastVisitDataId "
			+" AND lastVisitData.xForm.formId = xForm.formId "
			+ "AND formScore.formXpathScoreId =:sectorId"
			+" AND score.formXpathScoreMapping.formXpathScoreId = formScore.formXpathScoreId "
			+" AND xForm.formId = :formId"
			+ " AND lastVisitData.timPeriod.timePeriodId = :timeperiodId"
			
			)
	public List<Object[]> getDataByFormIdAndAreaId(@Param("formId") Integer formId,
			@Param("sectorId")Integer sectorId,@Param("areaId") Integer areaId ,@Param("timeperiodId") int timePeriod) throws DataAccessException;
	
	
	@Override
	@Query("SELECT lastVisitData, score, xForm, formScore,(score.score / score.maxScore)*100 FROM LastVisitData lastVisitData,"
			+" FacilityScore score, XForm xForm, FormXpathScoreMapping formScore,Area area "
			+" WHERE lastVisitData.isLive IS TRUE "
			+ " AND area.parentAreaId=:areaId " 
			+ "AND lastVisitData.area.parentAreaId =area.areaId "
			+" AND score.lastVisitData.lastVisitDataId = lastVisitData.lastVisitDataId "
			+" AND lastVisitData.xForm.formId = xForm.formId "
			+ "AND formScore.formXpathScoreId =:sectorId"
			+" AND score.formXpathScoreMapping.formXpathScoreId = formScore.formXpathScoreId "
			+" AND xForm.formId = :formId"
			+ " AND lastVisitData.timPeriod.timePeriodId = :timeperiodId"
			)
	public List<Object[]> getDataByFormIdAndDistrictAreaId(@Param("formId") Integer formId,
			@Param("sectorId")Integer sectorId,@Param("areaId") Integer areaId,@Param("timeperiodId") int timePeriod);
	
	@Override
	@Query("SELECT formScore.label,(score.score / score.maxScore)*100 FROM LastVisitData lastVisitData,"
			+" FacilityScore score, XForm xForm, FormXpathScoreMapping formScore "
			+" WHERE lastVisitData.isLive IS TRUE " 
			+ "AND lastVisitData.area.areaId =:areaId "
			+" AND score.lastVisitData.lastVisitDataId = lastVisitData.lastVisitDataId "
			+" AND lastVisitData.xForm.formId = xForm.formId "
			+ "AND formScore.formXpathScoreId =:sectorId"
			+" AND score.formXpathScoreMapping.formXpathScoreId = formScore.formXpathScoreId "
			+ "AND lastVisitData.timPeriod.timePeriodId= :timeperiodId")
	public List<Object[]> getDataBySectorIdIdAndDistrictAreaId(
			@Param("sectorId")Integer sectorId,@Param("areaId") Integer areaId,@Param("timeperiodId")int timeperiodId);
	
	@Override
	@Query("SELECT formScore.label,score.score,score.maxScore,formScore.formXpathScoreId ,formScore.type "
			+ "FROM LastVisitData lastVisitData,"
			+" FacilityScore score, XForm xForm, FormXpathScoreMapping formScore"
			+" WHERE lastVisitData.isLive IS TRUE " 
			+ "AND lastVisitData.area.areaId =:areaId "
			+" AND score.lastVisitData.lastVisitDataId = lastVisitData.lastVisitDataId "
			+" AND lastVisitData.xForm.formId = xForm.formId "
			+ "AND formScore.parentXpathId =:sectorId"
			+" AND score.formXpathScoreMapping.formXpathScoreId = formScore.formXpathScoreId "
			+ "AND lastVisitData.timPeriod.timePeriodId= :timeperiodId")
	public List<Object[]> getDataByparentSectorIdIdAndDistrictAreaId(
			@Param("sectorId")Integer sectorId,@Param("areaId") Integer areaId,@Param("timeperiodId")int timeperiodId);
	
	
	@Override
	@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MIN(lvd.timPeriod.timePeriodId)"
			+ " FROM LastVisitData lvd "
			+ " WHERE lvd.isLive IS TRUE"
			+ " AND lvd.area.parentAreaId = :areaId"
			+"  AND lvd.xForm.xform_meta_id = :formMetaId "
			+ " GROUP BY lvd.area.parentAreaId")
	public List<Object[]> findMaxMinTimePeriodIdForADistrict(@Param("areaId")Integer areaId,@Param("formMetaId") Integer formMetaId);
	
	
	@Override
	@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MIN(lvd.timPeriod.timePeriodId)"
			+ " FROM LastVisitData lvd ,Area ar"
			+ " WHERE lvd.isLive IS TRUE"
			+ " AND ar.parentAreaId=:areaId"
			+ " AND lvd.area.parentAreaId = ar.areaId"
			+"  AND lvd.xForm.xform_meta_id = :formMetaId "
			+ " GROUP BY ar.parentAreaId")
	public List<Object[]> findMaxMinTimePeriodIdForADistrictPHCCHC(@Param("areaId")Integer areaId,@Param("formMetaId") Integer formMetaId);
	
	
	@Override
	@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MIN(lvd.timPeriod.timePeriodId)"
			+ " FROM LastVisitData lvd "
			+ " WHERE lvd.isLive IS TRUE"
			+ " AND lvd.area.areaId =(SELECT lvd1.area.areaId from LastVisitData lvd1 where lvd1.lastVisitDataId = :lastVisitId)"
			+ " GROUP BY lvd.area.areaId")
	public List<Object[]> findMaxMinTimePeriodIdForAFacility(
			@Param("lastVisitId")Integer lastVisitDataId);
	
	@Override
	@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MIN(lvd.timPeriod.timePeriodId)"
			+ " FROM LastVisitData lvd "
			+ " WHERE lvd.isLive IS TRUE "
			+	" AND lvd.xForm.xform_meta_id = :formMetaId "
			+ " AND lvd.xForm.state.areaId = :stateId"
			)
	public List<Object[]> findMaxMinTimePeriodIdForState(@Param("formMetaId") Integer formMetaId , @Param("stateId") Integer stateId);
	
	
	@Override
	@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MIN(lvd.timPeriod.timePeriodId)"
			+ " FROM LastVisitData lvd "
			+ " WHERE lvd.isLive IS TRUE "
			+	" AND lvd.xForm.xform_meta_id = :formMetaId "
			+ " AND lvd.xForm.state.areaId = :stateId"
			)
	public List<Object[]> findMaxMinTimePeriodIdForHwcState(@Param("formMetaId") Integer formMetaId , @Param("stateId") Integer stateId);
	

		@Override
		@Query("SELECT lvd FROM LastVisitData lvd "
			+ " WHERE lvd.isLive IS TRUE AND lvd.xForm.formId IN :formId")
		public List<LastVisitData> findByIsLiveTrueAndxFormFormIdIn(@Param("formId")Set<Integer> ids);

		@Override
		@Query("SELECT lvd "
				+ " FROM LastVisitData lvd "
				+ " WHERE lvd.isLive IS TRUE "
				+	" AND lvd.xForm.xform_meta_id = :formMetaId "
				+ " AND lvd.area.areaCode = :areaCode"
				+ " ORDER BY lvd.timPeriod.timePeriodId Asc"
				)
		List<LastVisitData> findByAreaAreaCodeAndIsLiveTrueAndXFormMetaIdOrderByTimPeriodTimePeriodIdAsc(@Param("areaCode") String areaCode,
				@Param("formMetaId")	int formMetaId);
		
		@Override
		@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MAX(lvd.timPeriod.timePeriodId)-1,MIN(lvd.timPeriod.timePeriodId)"
				+ " FROM LastVisitData lvd "
				+ " WHERE lvd.isLive IS TRUE"
				+ " AND lvd.area.parentAreaId = :areaId"
				+"  AND lvd.xForm.xform_meta_id = :formMetaId "
				+ " GROUP BY lvd.area.parentAreaId")
		public List<Object[]> findAllTimePeriodIdForADistrict(@Param("areaId")Integer areaId,@Param("formMetaId") Integer formMetaId);
		
		@Override
		@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MIN(lvd.timPeriod.timePeriodId)"
				+ " FROM LastVisitData lvd "
				+ " WHERE lvd.isLive IS TRUE"
				+ " AND lvd.area.parentAreaId = :areaId"
				+"  AND lvd.xForm.xform_meta_id = :formMetaId "
				+ " GROUP BY lvd.area.parentAreaId")
		public List<Object[]> findAllTimePeriodIdForADistrictv3(@Param("areaId")Integer areaId,@Param("formMetaId") Integer formMetaId);
		
		
		@Override
		@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MAX(lvd.timPeriod.timePeriodId)-1,MIN(lvd.timPeriod.timePeriodId)"
				+ " FROM LastVisitData lvd ,Area ar"
				+ " WHERE lvd.isLive IS TRUE"
				+ " AND ar.parentAreaId=:areaId"
				+ " AND lvd.area.parentAreaId = ar.areaId"
				+"  AND lvd.xForm.xform_meta_id = :formMetaId "
				+ " GROUP BY ar.parentAreaId")
		public List<Object[]> findAllTimePeriodIdForADistrictPHCCHC(@Param("areaId")Integer areaId,@Param("formMetaId") Integer formMetaId);
		
		
		@Override
		@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MAX(lvd.timPeriod.timePeriodId)-1,MIN(lvd.timPeriod.timePeriodId)"
				+ " FROM LastVisitData lvd "
				+ " WHERE lvd.isLive IS TRUE"
				+ " AND lvd.area.areaId =(SELECT lvd1.area.areaId from LastVisitData lvd1 where lvd1.lastVisitDataId = :lastVisitId)"
				+ " GROUP BY lvd.area.areaId")
		public List<Object[]> findAllTimePeriodIdForAFacility(
				@Param("lastVisitId")Integer lastVisitDataId);
		
		@Override
		@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MAX(lvd.timPeriod.timePeriodId)-1,MIN(lvd.timPeriod.timePeriodId)"
				+ " FROM LastVisitData lvd "
				+ " WHERE lvd.isLive IS TRUE "
				+	" AND lvd.xForm.xform_meta_id = :formMetaId "
				+ " AND lvd.xForm.state.areaId = :stateId"
				)
		public List<Object[]> findAllTimePeriodIdForState(@Param("formMetaId") Integer formMetaId , @Param("stateId") Integer stateId);
		
		@Override
		@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MIN(lvd.timPeriod.timePeriodId)"
				+ " FROM LastVisitData lvd ,Area ar"
				+ " WHERE lvd.isLive IS TRUE"
				+ " AND ar.parentAreaId=:areaId"
				+ " AND lvd.area.parentAreaId = ar.areaId"
				+"  AND lvd.xForm.xform_meta_id = :formMetaId "
				+ " GROUP BY ar.parentAreaId")
		public List<Object[]> findAllTimePeriodIdForADistrictPHCCHCForUhcAndHwc(@Param("areaId")Integer areaId,@Param("formMetaId") Integer formMetaId);
		
		@Override
		@Query("SELECT MAX(lvd.timPeriod.timePeriodId),MIN(lvd.timPeriod.timePeriodId)"
				+ " FROM LastVisitData lvd "
				+ " WHERE lvd.isLive IS TRUE"
				+ " AND lvd.area.areaId =(SELECT lvd1.area.areaId from LastVisitData lvd1 where lvd1.lastVisitDataId = :lastVisitId)"
				+ " GROUP BY lvd.area.areaId")
		public List<Object[]> findAllTimePeriodIdForAFacilityOfUhc(
				@Param("lastVisitId")Integer lastVisitDataId);
		
}
