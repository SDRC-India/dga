package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.FacilityScore;
import org.sdrc.dga.domain.FormXpathScoreMapping;
import org.springframework.transaction.annotation.Transactional;

public interface FacilityScoreRepository {

	List<Object[]> findAvgByFormId(Integer formId);

	List<Object[]> findAllPercentValueByFormId(Integer formId);

	/**
	 * This method will return the spider chart data when a particular pushpin is clicked 
	 * @param formId
	 * @param lastVisitDataId
	 * @param parenXpathId
	 * @return
	 */
	List<Object[]> findSpiderDataChartByLasatVisitDatAndFormId(Integer formMetaId,Integer lastVisitDataId, Integer parenXpathId);

	/**
	 * This method will return the spider chart data for complete state and a particular facility type
	 * @param formId
	 * @param parenXpathId
	 * @param timeperiodId
	 * @return
	 */
	List<Object[]> findSpiderDataChartByFormId(Integer formMetaId, Integer parenXpathId, int timeperiodId);

	/**
	 * This method will return the spider chart data for a particular district and facility type
	 * @param formId
	 * @param parenXpathId
	 * @param timeperiodId
	 * @return
	 */
	List<Object[]> findSpiderDataChartByFormIdForDistrict(Integer formMetaId,
			Integer parenXpathId, Integer areaId, int timeperiodId);

	List<Object[]> findSpiderDataChartByFormIdForDistrictForDGA(Integer formMetaId,
			Integer parenXpathId, Integer areaId, int timeperiodId);

	@Transactional
	FacilityScore save(FacilityScore facilityScore);

	List<FacilityScore> findByFormXpathScoreMappingAndLastVisitDataIsLiveTrueAndLastVisitDataAreaParentAreaIdInAndLastVisitDataTimPeriodTimePeriodId(
			FormXpathScoreMapping formXpathScoreMapping, List<Integer> asList,
			int timeperiodId);

	List<FacilityScore> findByFormXpathScoreMappingAndLastVisitDataIsLiveTrueAndLastVisitDataTimPeriodTimePeriodId(
			FormXpathScoreMapping formXpathScoreMapping, int timeperiodId);

	List<Object[]> findSpiderDataChartByLasatVisitDatAndFormIdAndTimePeriodId(
			Integer formId, Integer lastVisitDataId, Integer parenXpathId,
			int timeperiodId);
}
