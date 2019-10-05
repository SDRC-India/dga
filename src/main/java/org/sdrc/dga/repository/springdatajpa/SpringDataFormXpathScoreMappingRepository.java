package org.sdrc.dga.repository.springdatajpa;

/**
 * @author Harsh(harsh@sdrc.co.in)
 */
import java.util.List;

import org.sdrc.dga.domain.FormXpathScoreMapping;
import org.sdrc.dga.repository.FormXpathScoreMappingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataFormXpathScoreMappingRepository extends
		FormXpathScoreMappingRepository,
		Repository<FormXpathScoreMapping, Integer> {

	@Override
	@Query(" SELECT fxm from FormXpathScoreMapping fxm,"
			+ " FacilityScore fs,LastVisitData lvd,Area area "
			+ " WHERE fxm.formXpathScoreId=:formXpathScoreId "
			+ " AND fs.formXpathScoreMapping.formXpathScoreId=fxm.formXpathScoreId"
			+ " AND lvd.isLive=true "
			+ " AND area.parentAreaId=:districtId "
			+ " AND lvd.area.parentAreaId=area.areaId "
			+ " AND fs.lastVisitData.lastVisitDataId=lvd.lastVisitDataId ")
	public FormXpathScoreMapping findByFormXpathScoreIdAndDistrictId(
			@Param("formXpathScoreId")Integer formXpathScoreId, 
			@Param("districtId")Integer districtId);
	
	@Override
	@Query(" SELECT fs.formXpathScoreMapping from "
			+ " FacilityScore fs,LastVisitData lvd "
			+ " WHERE fs.formXpathScoreMapping.formXpathScoreId=:formXpathScoreId "
			+ " AND lvd.isLive=true "
			+ " AND lvd.area.parentAreaId=:districtId "
			+ " AND fs.lastVisitData.lastVisitDataId=lvd.lastVisitDataId "	)
	public FormXpathScoreMapping findByFormXpathScoreIdAndDistrictIdForDga(
			@Param("formXpathScoreId")Integer formXpathScoreId, 
			@Param("districtId")Integer districtId);
	
	
	
	@Override
	@Query("SELECT fxm from FormXpathScoreMapping fxm WHERE "
			+ "fxm.form.formId = :formId AND "
			+ "fxm.formXpathScoreId NOT IN (SELECT DISTINCT(fxm1.parentXpathId) FROM FormXpathScoreMapping fxm1)")
	public List<FormXpathScoreMapping> findByFormIdAndWithNoChildren(
			@Param("formId")Integer formId);
	
	@Override
	@Query("SELECT MAX(fxm.formXpathScoreId) from FormXpathScoreMapping fxm")
	public int findLastId();

}
