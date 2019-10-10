package org.sdrc.devinfo.repository;

import java.util.List;
/**
 * @author Harsh
 * @since version 1.0.0.0
 *
 */
public interface Ut_Data_Repository {
	
	List<Object[]> getSubmissionSummary(Integer iCNid,Integer timeperiodNid,Integer sourceNid,Integer areaNid);
	
	List<Object[]> getSubmissionSummaryForUhc(Integer iCNid,Integer timeperiodNid,List<Integer> sourceNid,Integer areaNid);

	List<Object[]> getSubmissionSummaryForGeneralInfo(Integer iCNid,
			Integer timeperiodNid, Integer sourceNid, Integer areaNid);

	List<Object[]> getCrossTabData(int facilityId, int colId,int sourceNid, List<Integer> maxMinTime);

	List<Object[]> getCrossTabDataForDistrict(int facilityId, int colId,
			int sourceNid, int areaid, List<Integer> maxMinTime);

	List<Object[]> findTimePeriodAndIus();

	List<Integer> findAreaWithData();

	List<Object[]> findMaxMinTimePeriodForState();

	List<Object[]> findMaxMinTimePeriodForDistrict(int areaid);

	
	/*List<Object[]> get*/

}
