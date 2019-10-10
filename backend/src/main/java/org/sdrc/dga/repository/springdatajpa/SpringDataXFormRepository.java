package org.sdrc.dga.repository.springdatajpa;

import java.util.List;

import org.sdrc.dga.domain.Program;
import org.sdrc.dga.domain.TimePeriod;
import org.sdrc.dga.domain.XForm;
import org.sdrc.dga.repository.XFormRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * @author Sarita
 * @since version 1.0.0.0
 *
 */
public interface SpringDataXFormRepository extends XFormRepository, Repository<XForm, String> {

	XForm findByXFormIdAndIsLiveTrue(String getxFormId);

	@Override
	@Transactional
	@Modifying
	@Query("UPDATE XForm form SET form.isLive = FALSE WHERE form.formId= :xFormId")
	void updateIsLiveById(@Param("xFormId") Integer xFormId);

	@Override
	@Query("SELECT xform FROM XForm xform WHERE xform.isLive = True AND xform.xFormId=:xFormId")
	XForm findByxFormIdAndIsLiveTrue(@Param("xFormId") String xFormId);

	@Override
	@Query("Select xform.xFormId FROM XForm xform WHERE xform.isLive = True")
	public List<String> findAllXformNameByIsLiveTrue();

	@Override
	@Query("Select DISTINCT(xform.timePeriod) FROM XForm xform join  xform.formXpathScoreMappings fxm WHERE xform.isLive = True AND xform.state.areaId =:stateId AND fxm IS NOT NULL")
	public List<TimePeriod> findDistinctTimPeriodByStateAreaId(@Param("stateId") int stateId);

	@Override
	@Query("Select DISTINCT(xform.timePeriod) FROM XForm xform WHERE xform.isLive = True AND xform.state.areaId =:stateId AND xform.xform_meta_id = :formMetaId ")
	public List<TimePeriod> findDistinctTimePeriodBySateIdAndFormMetaId(@Param("formMetaId") int formMetaId,
			@Param("stateId") int stateId);
	
	@Override
	@Query("Select xform FROM XForm xform WHERE xform.isLive = True AND xform.state.areaId =:stateId AND xform.markerClass is not null AND  xform.timePeriod.timePeriodId IN("
			+ "  Select MAX(xForm2.timePeriod.timePeriodId) FROM XForm xForm2 where xForm2.state.areaId =:stateId AND xForm2.isLive = True AND xForm2.markerClass is not null)")
	 List<XForm> findAllByIsLiveTrueAndStateAndMaxTimePeriod(@Param("stateId") Integer stateId);
	
	
	
	
	
	@Override
	@Query("Select DISTINCT(form.programXFormMapping.program) FROM XForm form WHERE "
			+ "form.state.areaId =:stateId AND form.isLive IS TRUE AND form.programXFormMapping.isLive IS TRUE AND form.programXFormMapping.program.isLive IS TRUE  ")
	List<Program> findAllProgramByState(@Param("stateId")int stateId) ;
	
	
	@Override
	@Query("Select DISTINCT(xform.timePeriod) FROM XForm xform join  xform.formXpathScoreMappings fxm WHERE"
			+ " xform.isLive = True AND xform.state.areaId =:stateId AND fxm IS NOT NULL  AND xform.programXFormMapping.program.programId = :programId ")
	List<TimePeriod> findDistinctTimPeriodByStateAreaIdAndProgramId(@Param("stateId") int stateId,@Param("programId") int programId) ;
	
}