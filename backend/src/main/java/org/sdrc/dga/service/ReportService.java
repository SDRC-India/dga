package org.sdrc.dga.service;

import java.util.List;

import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.CrossTabDataModel;
import org.sdrc.dga.model.CrossTabDropDownData;
import org.sdrc.dga.model.SectorModel;
import org.sdrc.dga.model.TimePeriodModel;
/**
 * @author Harsh
 * @since version 1.0.0.0
 *
 */
public interface ReportService {
	
	List<SectorModel> getAllSectors();
	
	List<SectorModel> getAllSectorPrograms();
	
	List<TimePeriodModel> getAllTimePeriod();
	
	/**
	 *This method will return the Summary and aggreagete report of each division for Selected Checklist andSection And Time */
	Object fetchSummaryforSectorAndTimePeriod(Integer checklistId,Integer sectionId,Integer timperiodNid,Integer programId, Integer sectionNid);

	/**
	 *This method will return the raw report of each division for Selected Checklist andSection And Time PHC or CHC Or DG Wise of each area */
	Object fetchRawDataforSectorAndTimePeriodAndDivsion(Integer division,
			Integer checklistId, Integer sectionId,Integer timperiodNid
			,Integer programId, Integer sectionNid);

	List<AreaModel> getAllDistricts();

	

	
	/**
	 * 
	 * @return {@link CrossTabDropDownData}
	 */
	CrossTabDropDownData getCrossTabDropDown(int stateId,int timePeriodId);
	
	/**
	 * This method will return the data 
	 * for the cross tab table based on the selection of indicator and other selections.
	 * @param crossTabDataModel {@link CrossTabDataModel}
	 * @return
	 */
	Object getCrossTabTableData(CrossTabDataModel crossTabDataModel);
	
	List<String> getAllRawDataReportsList();

}
