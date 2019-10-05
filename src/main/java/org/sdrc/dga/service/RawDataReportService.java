package org.sdrc.dga.service;

import java.util.List;
import java.util.Map;

import org.sdrc.dga.model.TimePeriodModel;
import org.sdrc.dga.model.XFormModel;
import org.springframework.stereotype.Service;


public interface RawDataReportService {

	public Map<String,List<XFormModel>> getFacilityFormADistrictForRawData(Integer stateId);
	
	public List<TimePeriodModel> getAllPlanningTimePeriodForRawData(int stateId,String programId);
	
	public String getRawDataReportName(String programId,String facilityName,int timePeriodId);
}
