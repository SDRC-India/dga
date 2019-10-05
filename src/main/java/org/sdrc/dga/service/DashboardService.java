package org.sdrc.dga.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.FacilityPlanningModel;
import org.sdrc.dga.model.FormXpathScoreMappingModel;
import org.sdrc.dga.model.GoogleMapDataModel;
import org.sdrc.dga.model.ProgramModel;
import org.sdrc.dga.model.ScoreModel;
import org.sdrc.dga.model.SpiderDataCollection;
import org.sdrc.dga.model.TimePeriodModel;

/**
 * 
 * @author Harekrishna Panigrahi
 * @author Sarita Panigrahi
 * @author Harsh Pratyush
 *
 */
public interface DashboardService {

	List<ScoreModel> getAllAggregatedData(Integer formId);

	/**
	 * This method will return the google map datas
	 * 
	 * @param formId
	 * @param sectorId
	 * @param areaId
	 * @return
	 * @throws Exception
	 */
	List<GoogleMapDataModel> fetchAllGoogleMapData(Integer formId, Integer sectorId, Integer areaId, int timePeriodId)
			throws Exception;

	List<ScoreModel> fetchLabelFromLastVisitData(Integer lastVisitDataId) throws Exception;

	Map<String, List<ScoreModel>> getGridTableData(Integer formId, Integer lastVisitDataId, int timePeriodId)
			throws Exception;

	/**
	 * This will return spider data which will be used for bar chart and spider
	 * chart in dashboard
	 * 
	 * @param formId
	 * @param lastVisitDataId
	 * @param areaId
	 * @param parentXpathId 
	 * @param formMetaId 
	 * @return
	 */
	SpiderDataCollection getfetchSpiderData(Integer formId, Integer lastVisitDataId, Integer areaId, Integer parentXpathId, int formMetaId);

	/**
	 * This will return the parent sectors i.e. CHC,PHC,DH
	 * 
	 * @return
	 */
	List<FormXpathScoreMappingModel> getParentSectors(int timeperiodId,int stateId,int programId);

	/**
	 * This will give the Sectors under a Parent Sectors
	 * 
	 * @param parentId
	 * @return
	 */
	List<FormXpathScoreMappingModel> getSectors(Integer parentId);

	/**
	 * 
	 * @return List<AreaModel> All the districts
	 */
	List<AreaModel> getAllDistricts(int stateId);

	/**
	 * This method will Generate PDF on dashboard
	 * 
	 * @param spiderChart
	 * @param columnChart
	 * @param formId
	 * @param lastVisitDataId
	 * @param areaId
	 * @param response
	 * @param noOfFacilities
	 * @param formMetaId 
	 * @return
	 * @throws Exception
	 */
	String exportToPdf(String spiderChart, String columnChart, Integer formId, Integer lastVisitDataId, Integer areaId,
			HttpServletResponse response, int noOfFacilities, int timePeriodId,
			Integer parentXpathId, int formMetaId,HttpServletRequest request) throws Exception;

	/**
	 * This Module will generate Excel
	 * 
	 * @param spiderChart
	 * @param columnChart
	 * @param formId
	 * @param lastVisitDataId
	 * @param areaId
	 * @param response
	 * @param noOfFacilities
	 * @param formMetaId 
	 * @return
	 * @throws Exception
	 */
	String exportToExcel(String spiderChart, String columnChart, Integer formId, Integer lastVisitDataId,
			Integer areaId, HttpServletResponse response, int noOfFacilities, int timePeriodId,Integer parentXpathId, int formMetaId,HttpServletRequest request) throws Exception;

	/**
	 * this method will return the facility planned for a form in an area and
	 * timeperiod
	 * 
	 * @note Timeperiod is not yet used
	 * @param formId
	 * @param areaId
	 * @param timePeriodId
	 * @return
	 */
	FacilityPlanningModel getPlannedFacilities(int formId, int areaId, int timePeriodId,int stateId);

	List<TimePeriodModel> getAllPlanningTimePeriod(int stateId,int programId);

	List<AreaModel> getAllState();

	List<ProgramModel> getAllProgramme(int stateId);
	
	

}
