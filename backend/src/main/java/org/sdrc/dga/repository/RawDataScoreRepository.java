/**
 * 
 */
package org.sdrc.dga.repository;

import java.util.List;
import java.util.Set;

import org.sdrc.dga.domain.RawDataScore;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */
public interface RawDataScoreRepository {

	@Transactional
	void save(RawDataScore rawDataScore);

	List<RawDataScore> findByLastVisitDataLastVisitDataId(
			Integer lastVisitDataId);

	/**
	 * 
	 * @param xPathsRow
	 * @param xPathsIdCol
	 * @param timePeriodId
	 * @param districtId
	 * @param formId 
	 * @return
	 */
	//done
	List<Object[]> findCrossTabReportForADistrictAndATimePeriod(List<String> xPathsRow,
			List<String> xPathsIdCol, int timePeriodId, int districtId, int formId);

//	List<Object[]> findCrossTabReportForADistrictAndAllTimePeriod(
//			List<String> xPathsRow, List<String> xPathsIdCol, int districtId);

	//done
	List<Object[]> findCrossTabReportForATimePeriod(List<String> xPathsRow,
			List<String> xPathsIdCol, int timePeriodId, int formId);

//	List<Object[]> findCrossTabReportForAllTimePeriod(List<String> xPathsRow,
//			List<String> xPathsIdCol);

	//done
	List<Object[]> findCrossTabForOnlyOneIntegerType(List<String> xPathsIdCol,
			List<String> xPathsRow, Set<Integer> timePeriodIds, int formId);

	//done
	List<Object[]> findCrossTabForOnlyOneIntegerTypeADistrict(
			List<String> xPathsRow, List<String> xPathsIdCol,
			Set<Integer> timePeriodIds, int districtId, int formId);

	//correct
	List<Object[]> findCrossTabForOnlyIntegerType(List<String> xPathsRow,
			List<String> xPathsIdCol, Set<Integer> timePeriodIds);

	//correct
	List<Object[]> findCrossTabForOnlyIntegerTypeForADistrict(
			List<String> xPathsRow, List<String> xPathsIdCol,
			Set<Integer> timePeriodIds, int districtId);

	//skipped
	List<Object[]> findCrossTabForOnlyOneIntegerTypeADistrictByFacilityWise(
			 List<String> xPathsRow,
			Set<Integer> timePeriodIds, int districtId);

	//skipped
	List<Object[]> findCrossTabForOnlyOneIntegerTypeByFacilityWise(
			List<String> xPathsRow, Set<Integer> timePeriodIds);

	//skipped
	List<Object[]> findCrossTabForChoiceTypeADistrictByFacilityWise(
			List<String> xPathsRow, Set<Integer> timePeriodIds, int districtId);

	//skipped
	List<Object[]> findCrossTabForChoiceTypetByFacilityWise(
			List<String> xPathsRow, Set<Integer> timePeriodIds);

//	RawDataScore findByLastVisitDataLastVisitDataIdAndRawFormXapthsXPathId(
//			Integer lastVisitDataId, int signatureId);
	

}
