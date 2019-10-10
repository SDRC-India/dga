package org.sdrc.dga.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.sdrc.dga.domain.LastVisitData;
import org.springframework.dao.DataAccessException;

public interface LastVisitDataRepository {

	List<Object[]> getDataByFormId(Integer formId, Integer sectorId, int timeperiodId) throws DataAccessException;
	
	List<Object[]> getDataByFormIdAndAreaId(Integer formId, Integer sectorId,Integer areaId, int timeperiodId) throws DataAccessException;

	List<Object[]> getByLastVisitData(Integer lastVisitDataId)
			throws DataAccessException;

	List<Object[]> getDataByFormIdAndDistrictAreaId(Integer formId,
			Integer sectorId, Integer areaId, int timeperiodId);

	List <Object[]> getDataBySectorIdIdAndDistrictAreaId(Integer sectorId,
			Integer areaId, int timeperiodId);

	List<Object[]> getDataByparentSectorIdIdAndDistrictAreaId(Integer sectorId,
			Integer areaId, int timeperiodId);
	
	LastVisitData findByLastVisitDataIdAndIsLiveTrue(Integer lastVisitDataId);

	LastVisitData save(LastVisitData lastVisitDataLocal);

	LastVisitData findByxFormFormIdAndInstanceId(Integer formId,
			String instanceId);

	List<LastVisitData> findAll();

	List<Object[]> findMaxMinTimePeriodIdForADistrict(Integer areaId, Integer formMetaId);

	List<Object[]> findMaxMinTimePeriodIdForState(Integer formMetaId, Integer stateId);
	
	List<Object[]> findMaxMinTimePeriodIdForHwcState(Integer formMetaId, Integer stateId);

	List<Object[]> findMaxMinTimePeriodIdForAFacility(Integer lastVisitDataId);

	List<Object[]> findMaxMinTimePeriodIdForADistrictPHCCHC(Integer areaId,
			Integer formMetaId);
	
	
	List<Object[]> findAllTimePeriodIdForADistrict(Integer areaId, Integer formMetaId);
	
	List<Object[]> findAllTimePeriodIdForADistrictv3(Integer areaId, Integer formMetaId);

	List<Object[]> findAllTimePeriodIdForState(Integer formMetaId, Integer stateId);
	
	List<Object[]> findAllTimePeriodIdForAFacility(Integer lastVisitDataId);
	
	

	List<Object[]> findAllTimePeriodIdForADistrictPHCCHC(Integer areaId,
			Integer formMetaId);
	List<Object[]> findAllTimePeriodIdForADistrictPHCCHCForUhcAndHwc(Integer areaId,
			Integer formMetaId);
	List<Object[]> findAllTimePeriodIdForAFacilityOfUhc(Integer lastVisitDataId);
	
	

	LastVisitData findByxFormFormIdAndAreaAreaIdAndTimPeriodTimePeriodId(
			Integer formId, Integer areaId, int i);

	List<LastVisitData> findByTimPeriodTimePeriodId(int i);


//	List<LastVisitData> findByAreaAreaCodeAndIsLiveTrueAndXFormFormIdNotEqualsOrderByTimPeriodTimePeriodIdAsc(
//			String areaCode, Integer i);

	List<LastVisitData> findByAreaAreaCodeAndIsLiveTrueAndXFormFormIdLessThanOrderByTimPeriodTimePeriodIdAsc(
			String areaCode, int i);

//	List<LastVisitData> findByIsLiveTrue();

	List<LastVisitData> findByIsLiveTrueAndxFormFormIdIn(Set<Integer> ids);

	List<LastVisitData> findByLatitudeIsNullAndIsLiveTrue();

	LastVisitData findByAreaAreaIdAndTimPeriodTimePeriodIdAndIsLiveTrueAndLatitudeIsNotNull(
			Integer areaId, int timePeriodId);

	List<LastVisitData> findByAreaAreaCodeAndIsLiveTrueAndXFormMetaIdOrderByTimPeriodTimePeriodIdAsc(String areaCode,
			int formMetaId);

	LastVisitData findByxFormFormIdAndInstanceIdAndIsLiveTrue(Integer formId, String instanceId);
	
	List<LastVisitData> findByDateOfVisit(Date date);

}
