/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import java.util.List;
import java.util.Set;

import org.sdrc.dga.domain.RawDataScore;
import org.sdrc.dga.repository.RawDataScoreRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass = RawDataScore.class, idClass = Integer.class)
public interface SpringDataRawDataScoreRepository extends
		RawDataScoreRepository {

	@Override
	@Query(value="select sum(case when b.r1=b.r2 then 1 else 0 end) as sumval,b.ch1,b.ch2,b.tp "
			+ "from(select a.r1,a.r2,a.r2id,a.ch1,a.ch2,a.tp from "
			+ "(select rawdatasco0_.lastVisitDataId as r1, rawdatasco1_.lastVisitDataId as r2, "
			+ "choicesdet3_.choiceValue as ch1, choicesdet4_.choiceValue as ch2, timeperiod6_.timeperiod as tp,rawdatasco0_.rawDataScoreId as r2id "
			+ "from RawDataScore rawdatasco0_, LastVisitData lastvisitd5_, TimePeriod timeperiod6_ cross join RawDataScore rawdatasco1_ "
			+ "cross join Area area2_ cross join ChoicesDetails choicesdet3_ cross join ChoicesDetails choicesdet4_ "
			+ "cross join RawFormXpaths rawformxap7_ cross join RawFormXpaths rawformxap8_ cross join Area area15_ "
			+ "cross join LastVisitData lastvisitd16_ cross join Area area17_ "
			+ "where lastvisitd5_.FormId=:formId and rawdatasco0_.lastVisitDataId=lastvisitd5_.LastVisitDataId and "
			+ "lastvisitd5_.timePeriodId=timeperiod6_.timePeriodId and "
			+ "rawdatasco1_.xPathId=rawformxap7_.xPathId and rawdatasco0_.xPathId=rawformxap8_.xPathId and "
			+ "lastvisitd5_.AreaId=area15_.AreaId and rawdatasco1_.lastVisitDataId=lastvisitd16_.LastVisitDataId and "
			+ "lastvisitd16_.AreaId=area17_.AreaId and (rawformxap7_.xpath in :xPathsIdCol) "
			+ "and 'select_one '+choicesdet3_.choicName=rawformxap8_.type and 'select_one '+choicesdet4_.choicName=rawformxap7_.type "
			+ "and choicesdet3_.formId=rawformxap8_.formId and choicesdet4_.formId=rawformxap7_.formId and choicesdet3_.label=rawdatasco0_.score "
			+ "and choicesdet4_.label=rawdatasco1_.score and (area2_.ParentAreaId=:districtId or area2_.AreaId=:districtId) and "
			+ "(rawformxap8_.xpath in :xPathsRow) "
			+ "and lastvisitd5_.timePeriodId=:timePeriodId and area15_.ParentAreaId=area2_.AreaId and area17_.ParentAreaId=area2_.AreaId "
			+ "and rawdatasco0_.lastVisitDataId=rawdatasco1_.lastVisitDataId and (rawdatasco0_.score is not null) and rawdatasco0_.score<>'' "
			+ "and (rawdatasco1_.score is not null) and rawdatasco1_.score<>'' and lastvisitd5_.IsLive=1 "
			+ "and lastvisitd16_.IsLive=1)as a group by a.r1,a.r2,a.r2id,a.ch1,a.ch2,a.tp) as b "
			+ "group by b.ch1,b.ch2,b.tp order by b.ch1 DESC, b.ch2 DESC, b.tp ASC", nativeQuery=true)
		public List<Object[]> findCrossTabReportForADistrictAndATimePeriod(
				@Param("xPathsRow")List<String> xPathsRow, @Param("xPathsIdCol") List<String> xPathsIdCol,
				@Param("timePeriodId")int timePeriodId, @Param("districtId")int districtId, @Param("formId")int formId);
		
		@Override
		@Query(value="select sum(case when b.r1=b.r2 then 1 else 0 end) as sumval,b.ch1,b.ch2,b.tp "
				+ "from(select a.r1,a.r2,a.r2id,a.ch1,a.ch2,a.tp "
				+ "from(select rawdatasco0_.lastVisitDataId as r1,rawdatasco1_.lastVisitDataId as r2,rawdatasco1_.rawDataScoreId as r2id "
				+ ", choicesdet2_.choiceValue as ch1, choicesdet3_.choiceValue as ch2, "
				+ "timeperiod5_.timeperiod as tp from RawDataScore rawdatasco0_, LastVisitData lastvisitd4_, "
				+ "TimePeriod timeperiod5_ cross join RawDataScore rawdatasco1_ cross join ChoicesDetails choicesdet2_ "
				+ "cross join ChoicesDetails choicesdet3_ cross join RawFormXpaths rawformxap6_ "
				+ "cross join RawFormXpaths rawformxap7_ cross join LastVisitData lastvisitd14_ "
				+ "where lastvisitd4_.FormId=:formId and rawdatasco0_.lastVisitDataId=lastvisitd4_.LastVisitDataId and "
				+ "lastvisitd4_.timePeriodId=timeperiod5_.timePeriodId and rawdatasco1_.xPathId=rawformxap6_.xPathId and "
				+ "rawdatasco0_.xPathId=rawformxap7_.xPathId and rawdatasco1_.lastVisitDataId=lastvisitd14_.LastVisitDataId and "
				+ "(rawformxap6_.xpath in :xPathsIdCol) and 'select_one '+choicesdet2_.choicName=rawformxap7_.type and "
				+ "'select_one '+choicesdet3_.choicName=rawformxap6_.type and choicesdet2_.formId=rawformxap7_.formId and "
				+ "choicesdet3_.formId=rawformxap6_.formId and choicesdet2_.label=rawdatasco0_.score and "
				+ " choicesdet3_.label=rawdatasco1_.score and (rawformxap7_.xpath in :xPathsRow) and "
				+ "lastvisitd4_.timePeriodId=:timePeriodId and rawdatasco0_.lastVisitDataId=rawdatasco1_.lastVisitDataId and "
				+ "(rawdatasco0_.score is not null) and rawdatasco0_.score<>'' and (rawdatasco1_.score is not null) and "
				+ "rawdatasco1_.score<>'' and lastvisitd4_.IsLive=1 and lastvisitd14_.IsLive=1)as a "
				+ "group by a.r1,a.r2,a.r2id,a.ch1,a.ch2,a.tp)as b "
				+ "group by b.ch1,b.ch2,b.tp "
				+ "order by b.ch1 DESC,b.ch2 DESC,b.tp ASC", nativeQuery=true)
		public List<Object[]> findCrossTabReportForATimePeriod(
				@Param("xPathsRow")List<String> xPathsRow, @Param("xPathsIdCol") List<String> xPathsIdCol,
				@Param("timePeriodId")int timePeriodId, @Param("formId")int formId);
		
		@Override
		@Query(value="select sum(case when b.lvd1=b.lvd2 and b.score>=1 then 1 else 0 end) as AtleastOne, "
				+ "sum(case when b.lvd1=b.lvd2 and b.score>=2 then 1 else 0 end) as AtleastTwo, "
				+ "sum(case when b.lvd1=b.lvd2 and b.score>2 then 1 else 0 end) as MoreThanTwo,b.ch,b.tp "
				+ "from(select a.lvd1,a.lvd2,a.ch,a.tp,a.rdsId,a.score from(select rawdatasco0_.lastVisitDataId as lvd1, "
				+ "rawdatasco1_.lastVisitDataId lvd2,rawdatasco0_.rawDataScoreId rdsId,rawdatasco0_.score as score, "
				+ "choicesdet2_.choiceValue as ch, timeperiod4_.timeperiod as tp "
				+ "from RawDataScore rawdatasco0_, LastVisitData lastvisitd3_, TimePeriod timeperiod4_ "
				+ "cross join RawDataScore rawdatasco1_ cross join ChoicesDetails choicesdet2_ "
				+ "cross join RawFormXpaths rawformxap5_ cross join RawFormXpaths rawformxap8_ "
				+ "cross join LastVisitData lastvisitd11_ "
				+ "where lastvisitd3_.FormId=:formId and rawdatasco0_.lastVisitDataId=lastvisitd3_.LastVisitDataId "
				+ "and lastvisitd3_.timePeriodId=timeperiod4_.timePeriodId and rawdatasco1_.xPathId=rawformxap5_.xPathId "
				+ "and rawdatasco0_.xPathId=rawformxap8_.xPathId and rawdatasco1_.lastVisitDataId=lastvisitd11_.LastVisitDataId "
				+ "and (rawformxap5_.xpath in :xPathsIdRow) and 'select_one '+choicesdet2_.choicName=rawformxap5_.type "
				+ "and choicesdet2_.formId=rawformxap5_.formId and choicesdet2_.label=rawdatasco1_.score "
				+ "and (rawformxap8_.xpath in :intType) and (lastvisitd3_.timePeriodId in :timePeriodIds) "
				+ "and rawdatasco0_.lastVisitDataId=rawdatasco1_.lastVisitDataId and (rawdatasco0_.score is not null) "
				+ "and rawdatasco0_.score<>'' and (rawdatasco1_.score is not null) and rawdatasco1_.score<>'' "
				+ "and lastvisitd3_.IsLive=1 and lastvisitd11_.IsLive=1) as a "
				+ "group by a.lvd1,a.lvd2,a.ch,a.tp,a.rdsId,a.score)as b "
				+ "group by b.ch,b.tp order by b.ch desc, b.tp asc", nativeQuery=true)
		public List<Object[]> findCrossTabForOnlyOneIntegerType(
				@Param("xPathsIdRow")List<String> xPathsIdRow,@Param("intType")List<String> intType,
				@Param("timePeriodIds")Set<Integer> timePeriodIds, @Param("formId")int formId);
		
		@Override
		@Query(value="select sum(case when b.lvd1=b.lvd2 and b.score>=1 then 1 else 0 end) as AtleastOne, "
				+ "sum(case when b.lvd1=b.lvd2 and b.score>=2 then 1 else 0 end) as AtleastTwo, "
				+ "sum(case when b.lvd1=b.lvd2 and b.score>2 then 1 else 0 end) as MoreThanTwo,b.ch,b.tp "
				+ "from(select a.lvd1,a.lvd2,a.ch,a.score,a.tp from(select rawdatasco0_.lastVisitDataId as lvd1,rawdatasco1_.lastVisitDataId lvd2, "
				+ "choicesdet2_.choiceValue as ch, timeperiod5_.timeperiod as tp,rawdatasco0_.score from RawDataScore rawdatasco0_, "
				+ "LastVisitData lastvisitd4_, TimePeriod timeperiod5_ cross join RawDataScore rawdatasco1_ cross join "
				+ "ChoicesDetails choicesdet2_ cross join Area area3_ cross join RawFormXpaths rawformxap6_ cross join "
				+ "RawFormXpaths rawformxap9_ cross join LastVisitData lastvisitd12_ cross join Area area14_ cross join Area area16_ "
				+ "where lastvisitd4_.FormId=:formId and rawdatasco0_.lastVisitDataId=lastvisitd4_.LastVisitDataId and "
				+ "lastvisitd4_.timePeriodId=timeperiod5_.timePeriodId and rawdatasco1_.xPathId=rawformxap6_.xPathId and "
				+ "rawdatasco0_.xPathId=rawformxap9_.xPathId and rawdatasco1_.lastVisitDataId=lastvisitd12_.LastVisitDataId and "
				+ "lastvisitd4_.AreaId=area14_.AreaId and lastvisitd12_.AreaId=area16_.AreaId and (rawformxap6_.xpath in :xPathsIdRow) "
				+ "and 'select_one '+choicesdet2_.choicName=rawformxap6_.type and choicesdet2_.formId=rawformxap6_.formId and "
				+ "choicesdet2_.label=rawdatasco1_.score and (rawformxap9_.xpath in :intType) and "
				+ "(lastvisitd4_.timePeriodId in :timePeriodIds) and rawdatasco0_.lastVisitDataId=rawdatasco1_.lastVisitDataId and "
				+ "(rawdatasco0_.score is not null) and rawdatasco0_.score<>'' and (rawdatasco1_.score is not null) and "
				+ "rawdatasco1_.score<>'' and lastvisitd4_.IsLive=1 and lastvisitd12_.IsLive=1 and "
				+ "(area3_.ParentAreaId=:districtId or area3_.AreaId=:districtId) and area14_.ParentAreaId=area3_.AreaId and "
				+ "area16_.ParentAreaId=area3_.AreaId)as a "
				+ "group by a.lvd1,a.lvd2,a.ch,a.score,a.tp)as b "
				+ "group by b.ch,b.tp order by b.ch desc, b.tp asc", nativeQuery=true)
		public List<Object[]> findCrossTabForOnlyOneIntegerTypeADistrict(
				@Param("xPathsIdRow")List<String> xPathsIdRow,@Param("intType")List<String> intType,
				@Param("timePeriodIds")Set<Integer> timePeriodIds,@Param("districtId")int districtId, @Param("formId")int formId);
	
		
		@Override
		@Query("SELECT SUM(CASE WHEN  rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=1 AND colRds.score >=1 "
				+ " THEN 1 ELSE 0 END) AS AtleastOneOne , "
				+ " SUM(CASE WHEN  rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=1 AND colRds.score >=2 "
				+ " THEN 1 ELSE 0 END) AS AtleastOneTwo , "
				+ " SUM(CASE WHEN  rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=1 AND colRds.score > 2 "
				+ " THEN 1 ELSE 0 END) AS AtleastOneMoreTwo , "
				+"  SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=2 AND colRds.score >= 1 "
				+ " THEN 1 ELSE 0 END) AS AtleastTwoOne , "
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=2 AND colRds.score >= 2 "
				+ " THEN 1 ELSE 0 END) AS AtleastTwoTwo ,"
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=2 AND colRds.score > 2 "
				+ " THEN 1 ELSE 0 END) AS AtleastTwoMoreTwo ,"
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score > 2 AND colRds.score >= 1 "
				+ " THEN 1 ELSE 0 END) AS MoreThanTwoOne ,"
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score > 2 AND colRds.score >= 2 "
				+ " THEN 1 ELSE 0 END) AS MoreThanTwoTwo , "
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score > 2 AND colRds.score > 2 "
				+ " THEN 1 ELSE 0 END) AS MoreThanTwoMoreTwo ,"
				+ " rowRds.lastVisitData.timPeriod.timeperiod "
				+ " FROM RawDataScore rowRds ,"
				+ " RawDataScore colRds "
				+ " WHERE colRds.rawFormXapths.xpath IN :xPathsIdCol "
				+ " AND rowRds.rawFormXapths.xpath IN :xPathsRow "
				+ " AND rowRds.lastVisitData.timPeriod.timePeriodId IN :timePeriodIds "
				+" 	AND rowRds.lastVisitData.lastVisitDataId=colRds.lastVisitData.lastVisitDataId"
				+ " AND rowRds.score!=null AND rowRds.score!='' "
				+ " AND colRds.score!=null AND colRds.score!='' "
				+ " AND rowRds.lastVisitData.isLive = TRUE "
				+ " AND colRds.lastVisitData.isLive = TRUE "				
				+ " GROUP BY rowRds.lastVisitData.timPeriod.timeperiod"
				+ " ORDER BY rowRds.lastVisitData.timPeriod.timeperiod ASC ")
		public List<Object[]> findCrossTabForOnlyIntegerType(
				@Param("xPathsRow")List<String> xPathsRow, @Param("xPathsIdCol") List<String> xPathsIdCol,
				@Param("timePeriodIds")Set<Integer> timePeriodIds);

		
		@Override
		@Query("SELECT SUM(CASE WHEN  rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=1 AND colRds.score >=1 "
				+ " THEN 1 ELSE 0 END) AS AtleastOneOne , "
				+ " SUM(CASE WHEN  rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=1 AND colRds.score >=2 "
				+ " THEN 1 ELSE 0 END) AS AtleastOneTwo , "
				+ " SUM(CASE WHEN  rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=1 AND colRds.score > 2 "
				+ " THEN 1 ELSE 0 END) AS AtleastOneMoreTwo , "
				+"  SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=2 AND colRds.score >= 1 "
				+ " THEN 1 ELSE 0 END) AS AtleastTwoOne , "
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=2 AND colRds.score >= 2 "
				+ " THEN 1 ELSE 0 END) AS AtleastTwoTwo ,"
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score >=2 AND colRds.score > 2 "
				+ " THEN 1 ELSE 0 END) AS AtleastTwoMoreTwo ,"
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score > 2 AND colRds.score >= 1 "
				+ " THEN 1 ELSE 0 END) AS MoreThanTwoOne ,"
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score > 2 AND colRds.score >= 2 "
				+ " THEN 1 ELSE 0 END) AS MoreThanTwoTwo , "
				+ " SUM(CASE WHEN rowRds.lastVisitData.lastVisitDataId = colRds.lastVisitData.lastVisitDataId AND rowRds.score > 2 AND colRds.score > 2 "
				+ " THEN 1 ELSE 0 END) AS MoreThanTwoMoreTwo ,"
				+ " rowRds.lastVisitData.timPeriod.timeperiod "
				+ " FROM RawDataScore rowRds ,"
				+ " RawDataScore colRds ,Area area "
				+ " WHERE colRds.rawFormXapths.xpath IN :xPathsIdCol "
				+ " AND rowRds.rawFormXapths.xpath IN :xPathsRow "
				+ " AND rowRds.lastVisitData.timPeriod.timePeriodId IN :timePeriodIds "
				+" 	AND rowRds.lastVisitData.lastVisitDataId=colRds.lastVisitData.lastVisitDataId"
				+ " AND rowRds.score!=null AND rowRds.score!='' "
				+ " AND colRds.score!=null AND colRds.score!='' "
				+ " AND rowRds.lastVisitData.isLive = TRUE "
				+ " AND colRds.lastVisitData.isLive = TRUE "	
				+ " AND (area.parentAreaId = :districtId OR area.areaId=:districtId)"
				+ " AND rowRds.lastVisitData.area.parentAreaId = area.areaId "
				+ " AND colRds.lastVisitData.area.parentAreaId = area.areaId "
				+ " GROUP BY rowRds.lastVisitData.timPeriod.timeperiod"
				+ " ORDER BY rowRds.lastVisitData.timPeriod.timeperiod ASC ")
		public List<Object[]> findCrossTabForOnlyIntegerTypeForADistrict(
				@Param("xPathsRow")List<String> xPathsRow, @Param("xPathsIdCol") List<String> xPathsIdCol,
				@Param("timePeriodIds")Set<Integer> timePeriodIds, @Param("districtId")int districtId);
		
		
		
		@Override
		@Query("SELECT SUM(CASE WHEN  rowRds.score >=1 "
				+ " THEN 1 ELSE 0 END) AS AtleastOne , "
				+" SUM(CASE WHEN  rowRds.score >=2 "
				+ " THEN 1 ELSE 0 END) AS AtleastTwo , "
				+ " SUM(CASE WHEN  rowRds.score > 2 "
				+ " THEN 1 ELSE 0 END) AS MoreThanTwo ,"
				+ " rowRds.lastVisitData.xForm.areaLevel.areaLevelId,rowRds.lastVisitData.timPeriod.timeperiod "
				+ " FROM RawDataScore rowRds ,"
				+ " Area area "
				+ " WHERE rowRds.rawFormXapths.xpath IN :xPathsRow "
				+ " AND rowRds.lastVisitData.timPeriod.timePeriodId IN :timePeriodIds "
				+ " AND rowRds.score!=null AND rowRds.score!='' "
				+ " AND rowRds.lastVisitData.isLive = TRUE "
				+ " AND (area.parentAreaId = :districtId OR area.areaId=:districtId)"
				+ " AND rowRds.lastVisitData.area.parentAreaId = area.areaId "
				+ " GROUP BY rowRds.lastVisitData.xForm.formId,rowRds.lastVisitData.xForm.areaLevel.areaLevelId,rowRds.lastVisitData.timPeriod.timeperiod"
				+ " ORDER BY rowRds.lastVisitData.xForm.formId DESC ,rowRds.lastVisitData.timPeriod.timeperiod ASC ")
		public List<Object[]> findCrossTabForOnlyOneIntegerTypeADistrictByFacilityWise(
				@Param("xPathsRow")List<String> xPathsRow,
				@Param("timePeriodIds")Set<Integer> timePeriodIds, @Param("districtId")int districtId);
		
		
		@Override
		@Query("SELECT SUM(CASE WHEN  rowRds.score >=1 "
				+ " THEN 1 ELSE 0 END) AS AtleastOne , "
				+" SUM(CASE WHEN  rowRds.score >=2 "
				+ " THEN 1 ELSE 0 END) AS AtleastTwo , "
				+ " SUM(CASE WHEN  rowRds.score > 2 "
				+ " THEN 1 ELSE 0 END) AS MoreThanTwo ,"
				+ " rowRds.lastVisitData.xForm.areaLevel.areaLevelId,rowRds.lastVisitData.timPeriod.timeperiod "
				+ " FROM RawDataScore rowRds "
				+ " WHERE rowRds.rawFormXapths.xpath IN :xPathsRow "
				+ " AND rowRds.lastVisitData.timPeriod.timePeriodId IN :timePeriodIds "
				+ " AND rowRds.score!=null AND rowRds.score!='' "
				+ " AND rowRds.lastVisitData.isLive = TRUE "
				+ " GROUP BY rowRds.lastVisitData.xForm.formId,rowRds.lastVisitData.xForm.areaLevel.areaLevelId,rowRds.lastVisitData.timPeriod.timeperiod"
				+ " ORDER BY rowRds.lastVisitData.xForm.formId DESC ,rowRds.lastVisitData.timPeriod.timeperiod ASC ")
		public List<Object[]> findCrossTabForOnlyOneIntegerTypeByFacilityWise(
				@Param("xPathsRow")List<String> xPathsRow,
				@Param("timePeriodIds")Set<Integer> timePeriodIds);
		
		
		
		@Override
		@Query(" SELECT COUNT(*) as count,"
				+ " rowChoice.choiceValue,rowRds.lastVisitData.xForm.areaLevel.areaLevelId,rowRds.lastVisitData.timPeriod.timeperiod "
				+ " FROM RawDataScore rowRds ,"
				+ " Area area , "
				+ " ChoicesDetails rowChoice"
				+ " WHERE 'select_one '+rowChoice.choicName = rowRds.rawFormXapths.type "
				+ " AND rowChoice.form.formId = rowRds.rawFormXapths.form.formId "
				+ " AND rowChoice.label = rowRds.score "
				+ " AND (area.parentAreaId = :districtId OR area.areaId=:districtId)"
				+ " AND rowRds.rawFormXapths.xpath IN :xPathsRow "
				+ " AND rowRds.lastVisitData.timPeriod.timePeriodId IN :timePeriodIds "
				+ " AND rowRds.lastVisitData.area.parentAreaId = area.areaId"
				+ " AND rowRds.score!=null AND rowRds.score!='' "
				+ " AND rowRds.lastVisitData.isLive = TRUE "
				+ " GROUP BY rowRds.lastVisitData.xForm.formId,rowChoice.choiceValue,rowRds.lastVisitData.xForm.areaLevel.areaLevelId,rowRds.lastVisitData.timPeriod.timeperiod"
				+ " ORDER BY rowChoice.choiceValue DESC ,rowRds.lastVisitData.xForm.formId DESC,rowRds.lastVisitData.timPeriod.timeperiod ASC ")
		public List<Object[]> findCrossTabForChoiceTypeADistrictByFacilityWise(
				@Param("xPathsRow")List<String> xPathsRow,
				@Param("timePeriodIds")Set<Integer> timePeriodIds, @Param("districtId")int districtId);
		
		
		@Override
		@Query(" SELECT COUNT(*) as count,"
				+ " rowChoice.choiceValue,rowRds.lastVisitData.xForm.areaLevel.areaLevelId,rowRds.lastVisitData.timPeriod.timeperiod "
				+ " FROM RawDataScore rowRds ,"
				+ " ChoicesDetails rowChoice"
				+ " WHERE 'select_one '+rowChoice.choicName = rowRds.rawFormXapths.type "
				+ " AND rowChoice.form.formId = rowRds.rawFormXapths.form.formId "
				+ " AND rowChoice.label = rowRds.score "
				+ " AND rowRds.rawFormXapths.xpath IN :xPathsRow "
				+ " AND rowRds.lastVisitData.timPeriod.timePeriodId IN :timePeriodIds "
				+ " AND rowRds.score!=null AND rowRds.score!='' "
				+ " AND rowRds.lastVisitData.isLive = TRUE "
				+ " GROUP BY rowRds.lastVisitData.xForm.formId,rowChoice.choiceValue,rowRds.lastVisitData.xForm.areaLevel.areaLevelId,rowRds.lastVisitData.timPeriod.timeperiod"
				+ " ORDER BY rowChoice.choiceValue DESC ,rowRds.lastVisitData.xForm.formId DESC,rowRds.lastVisitData.timPeriod.timeperiod ASC ")
		public List<Object[]> findCrossTabForChoiceTypetByFacilityWise(
				@Param("xPathsRow")List<String> xPathsRow,
				@Param("timePeriodIds")Set<Integer> timePeriodIds);
}
