package org.sdrc.dga.repository;
/**
 * @author Harsh(harsh@sdrc.co.in)
 */
import java.util.List;

import org.sdrc.dga.domain.FormXpathScoreMapping;
import org.springframework.transaction.annotation.Transactional;

public interface FormXpathScoreMappingRepository {
	
	public List<FormXpathScoreMapping> findByParentXpathId(Integer parentId);
	
	public FormXpathScoreMapping findByFormXpathScoreId(Integer formXpathScoreId);
	
	public List<FormXpathScoreMapping> findAll();

	public FormXpathScoreMapping findByFormXpathScoreIdAndDistrictId(Integer formXpathScoreId,Integer DistrictId);
	
	public FormXpathScoreMapping findByFormXpathScoreIdAndDistrictIdForDga(Integer formXpathScoreId,Integer DistrictId);

	public List<FormXpathScoreMapping> findByFormIdAndWithNoChildren(
			Integer formId);

	@Transactional
	public FormXpathScoreMapping save(FormXpathScoreMapping formXpathScoreMapping);

	public int findLastId();

	public List<FormXpathScoreMapping> findByParentXpathIdAndFormTimePeriodTimePeriodIdAndFormStateAreaId(int i,
			int timeperiodId, int stateId);

	public List<FormXpathScoreMapping> findByParentXpathIdAndFormStateAreaIdAndFormTimePeriodTimePeriodId(int i,
			int stateId, int timePeriodId);

	public List<FormXpathScoreMapping> findByFormStateAreaIdAndFormTimePeriodTimePeriodId(int stateId,
			int timePeriodId);

	public List<FormXpathScoreMapping> findByParentXpathIdAndFormStateAreaId(int i, int stateId);

//	public List<FormXpathScoreMapping> findByParentXpathIdAndFormTimePeriodTimePeriodIdAndFormStateAreaIdAndFormProgramXFormMappingProgramProgramId(
//			int i, int timeperiodId, int stateId, int programId);

	public List<FormXpathScoreMapping> findByParentXpathIdAndFormTimePeriodTimePeriodIdAndFormStateAreaIdAndFormProgramXFormMappingProgramProgramIdOrderByFormAreaLevelAreaLevelId(
			int i, int timeperiodId, int stateId, int programId);

	public List<FormXpathScoreMapping> findByParentXpathIdAndFormStateAreaIdAndFormTimePeriodTimePeriodIdAndFormProgramXFormMappingProgramProgramIdOrderByFormAreaLevelAreaLevelId(
			int i, int stateId, int timePeriodId, int programId);
}
