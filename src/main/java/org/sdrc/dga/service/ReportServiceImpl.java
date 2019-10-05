package org.sdrc.dga.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sdrc.devinfo.domain.UtAreaEn;
import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;
import org.sdrc.devinfo.domain.UtTimeperiod;
import org.sdrc.devinfo.repository.IndicatorClassificationRepository;
import org.sdrc.devinfo.repository.TimeperiodRepository;
import org.sdrc.devinfo.repository.UtAreaEnRepository;
import org.sdrc.devinfo.repository.Ut_Data_Repository;
import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.FormXpathScoreMapping;
import org.sdrc.dga.domain.TimePeriod;
import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.model.CrossTabDataModel;
import org.sdrc.dga.model.CrossTabDropDownData;
import org.sdrc.dga.model.FormXpathScoreMappingModel;
import org.sdrc.dga.model.IndicatorFormXpathMappingModel;
import org.sdrc.dga.model.SectorModel;
import org.sdrc.dga.model.TimePeriodModel;
import org.sdrc.dga.model.ValueObject;
import org.sdrc.dga.repository.AreaRepository;
import org.sdrc.dga.repository.ChoiceDetailsRepository;
import org.sdrc.dga.repository.FormXpathScoreMappingRepository;
import org.sdrc.dga.repository.IndicatorFormXpathMappingRepository;
import org.sdrc.dga.repository.RawDataScoreRepository;
import org.sdrc.dga.repository.TimePeriodRepository;
import org.sdrc.dga.util.Constants;
import org.sdrc.dga.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private IndicatorClassificationRepository indicatorClassificationRepository;

	// devinfo timeperiod
	@Autowired
	private TimeperiodRepository timeperiodRepository;

	@Autowired
	private UtAreaEnRepository utAreaEnRepository;

	@Autowired
	private Ut_Data_Repository ut_Data_Repository;

	@Autowired
	private MessageSource messages;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private IndicatorFormXpathMappingRepository indicatorFormXpathMappingRepository;

	// web timeperiod
	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private FormXpathScoreMappingRepository formXpathScoreMappingRepository;

	@Autowired
	private RawDataScoreRepository rawDataScoreRepository;

	@Autowired
	private ChoiceDetailsRepository choiceDetailsRepository;

	@Override
	@Transactional(readOnly = true)
	public List<SectorModel> getAllSectors() {

		List<UtIndicatorClassificationsEn> utIndicatorClassificationsEns = indicatorClassificationRepository
				.getAllSectors();

		List<Object[]> timeperiodIusList = ut_Data_Repository.findTimePeriodAndIus();
		List<SectorModel> sectorsAndTimePeriods = new ArrayList<SectorModel>();
		List<Integer> timePeriodIds = new ArrayList<Integer>();

		for (UtIndicatorClassificationsEn utIndicatorClassificationsEn : utIndicatorClassificationsEns) {
			timePeriodIds = new ArrayList<Integer>();
			SectorModel sectorModel = new SectorModel();
			sectorModel.setiC_Name(utIndicatorClassificationsEn.getIC_Name());
			sectorModel.setiC_NId(utIndicatorClassificationsEn.getIC_NId());
			sectorModel.setiC_Parent_NId(utIndicatorClassificationsEn.getIC_Parent_NId());
			sectorModel.setiC_info(utIndicatorClassificationsEn.getIC_Info());

			for (Object[] timeperiodIus : timeperiodIusList) {
				if (utIndicatorClassificationsEn.getIC_NId() == Integer.parseInt(timeperiodIus[1].toString())) {
					timePeriodIds.add(Integer.parseInt(timeperiodIus[0].toString()));

				}

			}

			sectorModel.setUtTimeperiods(timePeriodIds);
			sectorsAndTimePeriods.add(sectorModel);
		}
		return sectorsAndTimePeriods;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SectorModel> getAllSectorPrograms() {

		List<UtIndicatorClassificationsEn> programs = indicatorClassificationRepository.getAllPrograms();

		List<SectorModel> sectorsAndTimePeriods = new ArrayList<SectorModel>();
		List<Integer> timePeriodIds = new ArrayList<Integer>();

		for (UtIndicatorClassificationsEn program : programs) {
			timePeriodIds = new ArrayList<Integer>();
			SectorModel sectorModel = new SectorModel();
			sectorModel.setiC_Name(program.getIC_Name());
			sectorModel.setiC_NId(program.getIC_NId());
			sectorModel.setiC_Parent_NId(program.getIC_Parent_NId());
			sectorModel.setiC_info(program.getIC_Info());
			sectorModel.setUtTimeperiods(timePeriodIds);
			sectorsAndTimePeriods.add(sectorModel);
		}
		return sectorsAndTimePeriods;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TimePeriodModel> getAllTimePeriod() {
		List<TimePeriodModel> timePeriodModels = new ArrayList<TimePeriodModel>();
		List<UtTimeperiod> utTimeperiods = timeperiodRepository.getAllTimePeriod();
		for (UtTimeperiod timeperiod : utTimeperiods) {
			TimePeriodModel periodModel = new TimePeriodModel();
			periodModel.setTimePeriod(timeperiod.getTimePeriod());
			periodModel.setTimePeriod_Nid(timeperiod.getTimePeriod_NId());

			timePeriodModels.add(periodModel);
		}
		return timePeriodModels;
	}

	@Override
	@Transactional(readOnly = true)
	public Object fetchSummaryforSectorAndTimePeriod(Integer checklistId, Integer sectionId, Integer timperiodNid,Integer programId, Integer sectionNid) {
		Integer oldchecklistId,oldsectionId,oldtimperiodNid;
		if(programId==235) {
			oldchecklistId=checklistId;
			oldsectionId=sectionId;
			oldtimperiodNid=timperiodNid;
			checklistId = sectionNid;
		}
		
		UtIndicatorClassificationsEn utIndicatorClassificationsEn = indicatorClassificationRepository
				.findOne(checklistId);
		Integer checkListId = utIndicatorClassificationsEn.getIC_NId();
		Integer sourceNid = 0;
		List<Integer> sourceNidList = new ArrayList<>();
		if(checklistId==205 || checklistId==230) {
			String[] sourceIds = messages.getMessage("source_" + checkListId.toString(), null, null).split(",");
			for(String id : sourceIds) {
				sourceNidList.add(Integer.parseInt(id)); 
			}
		}else {
		sourceNid = Integer.parseInt(messages.getMessage("source_" + checkListId.toString(), null, null));
		}

		List<Map<String, String>> gridTableDataLists = new ArrayList<>();
		Map<String, String> indicatorNameValueMap = null;
		List<UtAreaEn> utAreaEns = utAreaEnRepository.getAllAreaByLevel(3);
		String area = null;
		switch (checkListId) {
		case 1: {
			area = "PHC";
		}
			
			break;

		case 35: {
			area = "CHC";
		}
			
			break;

		case 17: {
			area = "DH";
		}
			
			break;
		case 120: {
			area = "HWC_HSC";
		}
			
			break;
		case 137: {
			area = "HWC_UPHC";
		}
			
			break;
		case 151: {
			area = "HWC_PHC";
		}
			
			break;
		case 165: {
			area = "HSC";
		}
			
			break;
			
		case 205: {
			area = "UHC_GENERAL_INFO";
		}
			
			break;
		case 206: {
			area = "UHC_HSC";
		}
			
			break;
		case 208: {
			area = "UHC_PHC";
		}
			
			break;
		case 210: {
			area = "UHC_CHC";
		}
			
			break;
		case 212: {
			area = "UHC_DH";
		}
			
			break;
			
		case 230: {
			area = "UHC_DRUG_GENERAL_INFO";
		}
			
			break;
		case 233: {
			area = "UHC_DRUG_HSC";
		}
			
			break;
		case 234: {
			area = "UHC_DRUG_PHC";
		}
			
			break;
		case 232: {
			area = "UHC_DRUG_CHC";
		}
			
			break;
		case 231: {
			area = "UHC_DRUG_DH";
		}
			
			break;


		case 111:
			area = "Facilities";

			break;
		case 204:
			area = "UHC_Diagnostics";

			break;
		case 229:
			area = "UHC_Drugs";

			break;
		}
		Map indicatorsName = new HashMap<String, Integer>();
		
		for (UtAreaEn areaEn : utAreaEns) {

			List<Object[]> dataSubmissionaggregate;

			// dataSubmissionaggregate=ut_Data_Repository.getSubmissionSummaryForGeneralInfo(sectionId,
			// timperiodNid, sourceNid, areaEn.getArea_NId());

			// if(dataSubmissionaggregate.isEmpty()||dataSubmissionaggregate==null||dataSubmissionaggregate.size()==0)
			// {
			if(checklistId==205 || checklistId==230) {

				dataSubmissionaggregate = ut_Data_Repository.getSubmissionSummaryForUhc(sectionId, timperiodNid, sourceNidList,
						areaEn.getArea_NId());
			}else {
			dataSubmissionaggregate = ut_Data_Repository.getSubmissionSummary(sectionId, timperiodNid, sourceNid,
					areaEn.getArea_NId());
			 }

			indicatorNameValueMap = new LinkedHashMap<String, String>();

			if (dataSubmissionaggregate.isEmpty())
				continue;
			/*
			 * header name according to arealevel
			 */

			indicatorNameValueMap.put("rowId", String.valueOf(areaEn.getArea_NId()));// area Id

			indicatorNameValueMap.put("District", areaEn.getArea_Name());// areaName
			
			

			if ((!(sectionId == Integer.parseInt(messages.getMessage(area + "_Man_Power", null, null))
					|| sectionId == Integer.parseInt(messages.getMessage(area + "_Human_Resource", null, null))
					|| sectionId == Integer.parseInt(messages.getMessage(area + "_Training", null, null))))
					&& checklistId == Integer.parseInt(messages.getMessage("District_Hospital", null, null)))

			{
				for (Object[] utData : dataSubmissionaggregate) {
					/*
					 * indicator name ( sub group)
					 */
					indicatorNameValueMap.put(
							utData[0] == null ? "NA" : utData[0].toString() + "(" + utData[3].toString() + ")",
							utData[1] == null ? "NA" : utData[1].toString().equals("1.0") ? "Yes" : "No");
				}
				
			}

			else {
				for (Object[] utData : dataSubmissionaggregate) {
					/*
					 * indicator name ( sub group)
					 */
					indicatorNameValueMap.put(
							utData[0] == null ? "NA" : utData[0].toString() + "(" + utData[3].toString() + ")",
							utData[1] == null ? "NA" : utData[1].toString());
					if(checklistId==230) {
						
						indicatorsName.put(utData[0] == null ? "NA" : utData[0].toString() + "(" + utData[3].toString() + ")", 0);
					}
//						indicatorsName.put(utData[0] == null ? "NA" : utData[0].toString() + "(" + utData[3].toString() + ")", 0);
				}
				
			}
			gridTableDataLists.add(indicatorNameValueMap);
			
		}
		if (gridTableDataLists.isEmpty())
			return "No Data Available"; // for no data modal in view

		List<Object[]> dataSubmissionaggregate;

		// dataSubmissionaggregate=ut_Data_Repository.getSubmissionSummaryForGeneralInfo(sectionId,
		// timperiodNid, sourceNid, Integer.parseInt(
		// messages.getMessage("Area_Id",null,null)));

		// if(dataSubmissionaggregate.isEmpty())

		//added for UHC generalinfo
		if(checklistId==205 || checklistId==230) {
			dataSubmissionaggregate = ut_Data_Repository.getSubmissionSummaryForUhc(sectionId, timperiodNid, sourceNidList,
					Integer.parseInt(messages.getMessage("Area_Id", null, null)));
		}else {
		
		dataSubmissionaggregate = ut_Data_Repository.getSubmissionSummary(sectionId, timperiodNid, sourceNid,
				Integer.parseInt(messages.getMessage("Area_Id", null, null)));
		}

		indicatorNameValueMap = new LinkedHashMap<String, String>();
		/*
		 * header name according to arealevel
		 * 
		 */

		if (checklistId != Integer.parseInt(messages.getMessage("District_Hospital", null, null))) {
			 indicatorNameValueMap.put("District", "Chattisgarh");// areaName

			for (Object[] utData : dataSubmissionaggregate) {
				/*
				 * indicator name ( sub group)
				 */
				indicatorNameValueMap.put(
						utData[0] == null ? "NA" : utData[0].toString() + "(" + utData[3].toString() + ")",
						utData[1] == null ? "NA" : utData[1].toString());
			}
			gridTableDataLists.add(indicatorNameValueMap);
		}
//		if(checklistId==230) {
////			indicatorsName.put("rowId", 0);
//			indicatorsName.put("District", 0);
//			gridTableDataLists.add(indicatorsName);
//		}

		return gridTableDataLists;

	}

	@Override
	@Transactional(readOnly = true)
	public Object fetchRawDataforSectorAndTimePeriodAndDivsion(Integer division, Integer checklistId, Integer sectionId,
			Integer timperiodNid ,Integer programId, Integer sectionNid) {
		Integer oldchecklistId,oldsectionId,oldtimperiodNid;
		if(programId==235) {
			oldchecklistId=checklistId;
			oldsectionId=sectionId;
			oldtimperiodNid=timperiodNid;
			checklistId = sectionNid;
		}
		
		UtIndicatorClassificationsEn utIndicatorClassificationsEn = indicatorClassificationRepository
				.findOne(checklistId);
		Integer checkListId = utIndicatorClassificationsEn.getIC_NId();
		Integer sourceNid = 0;
		String area = null;
		switch (checkListId) {
		case 1: {
			area = "PHC";
		}
			;
			break;

		case 35: {
			area = "CHC";
		}
			;
			break;

		case 17: {
			area = "DH";
		}
			;
			break;

		case 120: {
			area = "HWC_HSC";
		}
			
			break;
		case 137: {
			area = "HWC_UPHC";
		}
			
			break;
		case 151: {
			area = "HWC_PHC";
		}
			
			break;
		case 165: {
			area = "HSC";
		}
			
			break;
			
		case 205: {
			area = "UHC_GENERAL_INFO";
		}
			
			break;
		case 206: {
			area = "UHC_HSC";
		}
			
			break;
		case 208: {
			area = "UHC_PHC";
		}
			
			break;
		case 210: {
			area = "UHC_CHC";
		}
			
			break;
		case 212: {
			area = "UHC_DH";
		}
			
			break;
			
		case 230: {
			area = "UHC_DRUG_GENERAL_INFO";
		}
			
			break;
		case 233: {
			area = "UHC_DRUG_HSC";
		}
			
			break;
		case 234: {
			area = "UHC_DRUG_PHC";
		}
			
			break;
		case 232: {
			area = "UHC_DRUG_CHC";
		}
			
			break;
		case 231: {
			area = "UHC_DRUG_DH";
		}
			
			break;


		case 111:
			area = "Facilities";

			break;
		case 204:
			area = "UHC_Diagnostics";

			break;
		case 229:
			area = "UHC_Drugs";

			break;
		}
		List<Integer> sourceNidList = new ArrayList<>();
		if(checklistId==205 || checklistId==230) {
			String[] sourceIds = messages.getMessage("source_" + checkListId.toString(), null, null).split(",");
			for(String id : sourceIds) {
				sourceNidList.add(Integer.parseInt(id)); 
			}
		}else {
		sourceNid = Integer.parseInt(messages.getMessage("source_" + checkListId.toString(), null, null));
		}
		
//		sourceNid = Integer.parseInt(messages.getMessage("source_" + checkListId.toString(), null, null));

		List<Map<String, String>> gridTableDataLists = new ArrayList<>();
		Map<String, String> indicatorNameValueMap = null;
		List<UtAreaEn> utAreaEns = utAreaEnRepository.findByArea_Parent_NId(division);
		for (UtAreaEn areaEn : utAreaEns) {
			List<Object[]> dataSubmissionaggregate;
			if(checklistId==205 || checklistId==229) {
				dataSubmissionaggregate = ut_Data_Repository.getSubmissionSummaryForUhc(sectionId, timperiodNid, sourceNidList,
						areaEn.getArea_NId());
			}else {
			 dataSubmissionaggregate = ut_Data_Repository.getSubmissionSummary(sectionId, timperiodNid,
					sourceNid, areaEn.getArea_NId());
			}
			indicatorNameValueMap = new LinkedHashMap<String, String>();

			if (dataSubmissionaggregate.isEmpty())
				continue;
			/*
			 * header name according to arealevel
			 */

			indicatorNameValueMap.put(area, areaEn.getArea_Name());// areaName

			if (sectionId == Integer.parseInt(messages.getMessage(area + "_Man_Power", null, null))
					|| sectionId == Integer.parseInt(messages.getMessage(area + "_Human_Resource", null, null))
					|| sectionId == Integer.parseInt(messages.getMessage(area + "_Training", null, null))) {
				for (Object[] utData : dataSubmissionaggregate) {
					/*
					 * indicator name ( sub group)
					 */
					indicatorNameValueMap.put(
							utData[0] == null ? "NA" : utData[0].toString() + "(" + utData[3].toString() + ")",
							utData[1] == null ? "NA" : utData[1].toString());
				}
				gridTableDataLists.add(indicatorNameValueMap);
			} else {
				for (Object[] utData : dataSubmissionaggregate) {
					/*
					 * indicator name ( sub group)
					 */
					// indicatorNameValueMap.put(utData[0]==null?"NA":utData[0].toString() ,
					// utData[1]==null?"NA":utData[1].toString());

					indicatorNameValueMap.put(
							utData[0] == null ? "NA" : utData[0].toString() + "(" + utData[3].toString() + ")",
							utData[1] == null ? "NA" : utData[1].toString().equals("1.0") ? "Yes" : "No");
				}
				gridTableDataLists.add(indicatorNameValueMap);
			}
		}
		if (gridTableDataLists.isEmpty())
			return "No Data Available";
		return gridTableDataLists;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AreaModel> getAllDistricts() {
		List<UtAreaEn> utAreaEns = utAreaEnRepository.getAllAreaByLevel(3);
		List<AreaModel> districtModels = new ArrayList<AreaModel>();

		AreaModel areaModel = new AreaModel();

		areaModel.setAreaId(0);
		areaModel.setAreaName("Assam");

		districtModels.add(areaModel);

		for (UtAreaEn district : utAreaEns) {
			areaModel = new AreaModel();
			areaModel.setAreaId(district.getArea_NId());
			areaModel.setAreaName(district.getArea_Name());
			areaModel.setAreaLevelId(district.getArea_Level());
			areaModel.setParentAreaId(district.getArea_Parent_NId());

			districtModels.add(areaModel);
		}
		return districtModels;
	}

	@Override
	@Transactional(readOnly = true)
	public CrossTabDropDownData getCrossTabDropDown(int stateId, int timePeriodId) {
		List<Object[]> indicatorFormXpathMappingList = indicatorFormXpathMappingRepository.findDistinctLabels();
		List<IndicatorFormXpathMappingModel> indicatorFormXpathMappingModels = new ArrayList<IndicatorFormXpathMappingModel>();

		IndicatorFormXpathMappingModel formXpathMappingModel = new IndicatorFormXpathMappingModel();
		formXpathMappingModel.setIndicatorFormXpathMappingId(0);
		formXpathMappingModel.setLabel("Number of Facilities assessed");
		formXpathMappingModel.setChcXpath("0");
		formXpathMappingModel.setPhcXpath("0");
		formXpathMappingModel.setDhXpath("0");
		formXpathMappingModel.setHscXpath("0");
		formXpathMappingModel.setType("Facility");
		indicatorFormXpathMappingModels.add(formXpathMappingModel);

		for (Object[] formXpathMapping : indicatorFormXpathMappingList) {
			formXpathMappingModel = new IndicatorFormXpathMappingModel();
			formXpathMappingModel.setIndicatorFormXpathMappingId(Integer.parseInt(formXpathMapping[0].toString()));
			formXpathMappingModel.setLabel(formXpathMapping[1].toString());
			formXpathMappingModel.setChcXpath(formXpathMapping[5] == null ? "0" : (formXpathMapping[5].toString()));
			formXpathMappingModel.setPhcXpath(formXpathMapping[6] == null ? "0" : (formXpathMapping[6].toString()));
			formXpathMappingModel.setDhXpath(formXpathMapping[4] == null ? "0" : (formXpathMapping[4].toString()));
			formXpathMappingModel.setHscXpath(formXpathMapping[7] == null ? "0" : (formXpathMapping[7].toString()));
//			if(formXpathMapping[7] == null)
//				formXpathMappingModel.setHscXpath("0");
//			else
//				formXpathMappingModel.setHscXpath(formXpathMapping[7].toString());
			formXpathMappingModel.setType(formXpathMapping[2].toString());
			indicatorFormXpathMappingModels.add(formXpathMappingModel);
		}
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		Integer areaLevelId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaLevelId();
		Integer parentAreaId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getParentAreaId();

		// if Logged in user of District type then only its own District will be
		// displayed
		List<AreaModel> districtModels = new ArrayList<AreaModel>();
		if (areaLevelId > 3 && parentAreaId != -1) {
			districtModels.add(collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
					.getRoleFeaturePermissionSchemeModel().getAreaModel());
		}

		else {
			// else if state user setting stateid from session
			if (areaLevelId == 2) {
				stateId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
						.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaId();
			}
// if of national or guest or admin type then all the areas
			List<Area> districts = areaRepository.findByParentAreaIdAndAreaLevelAreaLevelIdOrderByAreaNameAsc(stateId,
					4);
			Area state = areaRepository.findByAreaId(stateId);

			AreaModel areaModel = new AreaModel();

			areaModel.setAreaId(0);
			areaModel.setAreaName(state.getAreaName());

			districtModels.add(areaModel);

			for (Area district : districts) {
				areaModel = new AreaModel();
				areaModel.setAreaId(district.getAreaId());
				areaModel.setAreaName(district.getAreaName());
				areaModel.setAreaLevelId(district.getAreaLevel().getAreaLevelId());
				areaModel.setParentAreaId(district.getParentAreaId());

				districtModels.add(areaModel);
			}
		}

//		List<TimePeriodModel> timePeriodModels = new ArrayList<TimePeriodModel>();
//		List<TimePeriod> timePeriods = xFormRepository.findDistinctTimPeriodByStateAreaId(stateId);
//		Collections.reverse(timePeriods);
//		for (TimePeriod timeperiod : timePeriods) {
//			TimePeriodModel periodModel = new TimePeriodModel();
//			periodModel.setTimePeriod(timeperiod.getTimeperiod());
//			periodModel.setTimePeriod_Nid(timeperiod.getTimePeriodId());
//
//			timePeriodModels.add(periodModel);
//		}

//		TimePeriodModel periodModel = new TimePeriodModel();
//		periodModel.setTimePeriod("All");
//		periodModel.setTimePeriod_Nid(0);
//		timePeriodModels.add(periodModel);

		List<FormXpathScoreMapping> formXpathScoreMappings = formXpathScoreMappingRepository
				.findByParentXpathIdAndFormStateAreaIdAndFormTimePeriodTimePeriodId(-1, stateId, timePeriodId);
		/* formXpathScoreMapping */
		List<FormXpathScoreMappingModel> formXpathScoreMappingModels = new ArrayList<FormXpathScoreMappingModel>();

		// Getting the parent Sectors
		for (FormXpathScoreMapping formXpathScoreMapping : formXpathScoreMappings) {
			
			FormXpathScoreMappingModel formXpathScoreMappingModel = new FormXpathScoreMappingModel();
			if(formXpathScoreMapping.getForm().getXform_meta_id()==1 
					||formXpathScoreMapping.getForm().getXform_meta_id()==2 
					||formXpathScoreMapping.getForm().getXform_meta_id()==3 
					||formXpathScoreMapping.getForm().getXform_meta_id()==5 ) {
			formXpathScoreMappingModel.setFormXpathScoreId(formXpathScoreMapping.getFormXpathScoreId());
			// Slicing the name as DB Consist the name as Total SCore OF DH for every Sector
			formXpathScoreMappingModel.setLabel(formXpathScoreMapping.getLabel().split("Total score for")[1]);
			formXpathScoreMappingModel.setFormId(formXpathScoreMapping.getForm().getFormId());
			formXpathScoreMappingModel.setForm_meta_id(formXpathScoreMapping.getForm().getXform_meta_id());
			formXpathScoreMappingModels.add(formXpathScoreMappingModel);
			}
		}

		FormXpathScoreMappingModel formXpathScoreMappingModel = new FormXpathScoreMappingModel();
		formXpathScoreMappingModel.setFormXpathScoreId(0);
		formXpathScoreMappingModel.setLabel("All");
		formXpathScoreMappingModel.setFormId(0);
		formXpathScoreMappingModels.add(formXpathScoreMappingModel);

		CrossTabDropDownData crossTabDropDownData = new CrossTabDropDownData();

		crossTabDropDownData.setAreaList(districtModels);
		crossTabDropDownData.setFormXpathScoreMappingModels(formXpathScoreMappingModels);
		crossTabDropDownData.setIndicatorFormXpathMappingModels(indicatorFormXpathMappingModels);
//		crossTabDropDownData.setTimePeriodModels(timePeriodModels);

		return crossTabDropDownData;
	}

	@Override
	@Transactional(readOnly = true)
	public Object getCrossTabTableData(CrossTabDataModel crossTabDataModel) {

		List<String> xPathsIdCol = new ArrayList<String>();
		xPathsIdCol.add(crossTabDataModel.getColChcXpath());
		xPathsIdCol.add(crossTabDataModel.getColPhcXpath());
		xPathsIdCol.add(crossTabDataModel.getColDhXpath());
		xPathsIdCol.add(crossTabDataModel.getColHscXpath());

		List<String> xPathsRow = new ArrayList<String>();
		xPathsRow.add(crossTabDataModel.getRowChcXpath());
		xPathsRow.add(crossTabDataModel.getRowPhcXpath());
		xPathsRow.add(crossTabDataModel.getRowDhXpath());
		xPathsRow.add(crossTabDataModel.getRowHscXpath());

		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		Integer areaLevelId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaLevelId();
		Integer parentAreaId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getParentAreaId();

		// if Logged in user of District type then only its own District will be
		// displayed
		if (areaLevelId > 3 && parentAreaId != -1) {
			crossTabDataModel.setDistrictId(collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
					.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaId());
		}

		if (crossTabDataModel.getColIndicatorFormXpathMappingId() != 0) {
			if (!(crossTabDataModel.getColIndicatorFormXpathMappingType().equalsIgnoreCase("integer")
					|| crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer"))) {
				return getCrossTabForChoiceIndicators(crossTabDataModel, xPathsIdCol, xPathsRow, crossTabDataModel.getFacilityTypeId());
			} else {
				return getCrossTabForAnyIntegerIndicator(crossTabDataModel, xPathsIdCol, xPathsRow, crossTabDataModel.getFacilityTypeId());
			}
		}

//		the else statement will never execute as per the condition, because column indicator is a mandatory field
//		Discussed with Rakesh sir
		else {
//			return getFacilityWiseCrossTabData(crossTabDataModel, xPathsIdCol, xPathsRow);
			return null;
		}
	}

	private Object getCrossTabForChoiceIndicators(CrossTabDataModel crossTabDataModel, List<String> xPathsIdCol,
			List<String> xPathsRow, int formId) {
		List<Object[]> crossTabDatas = new ArrayList<Object[]>();
		if (crossTabDataModel.getDistrictId() != 0) {
			if (crossTabDataModel.getTimePeriodId() != 0)
				crossTabDatas = rawDataScoreRepository.findCrossTabReportForADistrictAndATimePeriod(xPathsRow,
						xPathsIdCol, crossTabDataModel.getTimePeriodId(), crossTabDataModel.getDistrictId(), formId);

//			else
//				crossTabDatas = rawDataScoreRepository.findCrossTabReportForADistrictAndAllTimePeriod(xPathsRow,
//						xPathsIdCol, crossTabDataModel.getDistrictId());
		}

		else if (crossTabDataModel.getDistrictId() == 0) {
			if (crossTabDataModel.getTimePeriodId() != 0)
				crossTabDatas = rawDataScoreRepository.findCrossTabReportForATimePeriod(xPathsRow, xPathsIdCol,
						crossTabDataModel.getTimePeriodId(), formId);

//			else
//				crossTabDatas = rawDataScoreRepository.findCrossTabReportForAllTimePeriod(xPathsRow, xPathsIdCol);
		}

		Map<String, Object> tableDatas = new LinkedHashMap<String, Object>();

		Set<String> colSubGroups = choiceDetailsRepository.findByXpathIdAndType(xPathsIdCol, formId);
		colSubGroups.add("Total");

		Set<String> rowSubGroups = choiceDetailsRepository.findByXpathIdAndType(xPathsRow, formId);
		rowSubGroups.add("Total");

		Set<String> timePeriods = new LinkedHashSet<String>();
		if (crossTabDataModel.getTimePeriodId() != 0) {
			timePeriods
					.add(timePeriodRepository.findByTimePeriodId(crossTabDataModel.getTimePeriodId()).getTimeperiod());
		} else {
			timePeriods = timePeriodRepository.findTimePeriods();
		}
		Map<String, String> dataMap = new LinkedHashMap<String, String>();

		for (Object[] crossTabData : crossTabDatas) {
			dataMap.put(
					crossTabData[1].toString() + "_" + crossTabData[2].toString() + "_" + crossTabData[3].toString(),
					crossTabData[0].toString());
			if (dataMap.containsKey("Total_" + crossTabData[2].toString() + "_" + crossTabData[3].toString())) {
				int sum = Integer.parseInt(
						dataMap.get("Total_" + crossTabData[2].toString() + "_" + crossTabData[3].toString()));
				dataMap.put("Total_" + crossTabData[2].toString() + "_" + crossTabData[3].toString(),
						String.valueOf(Integer.parseInt(crossTabData[0].toString()) + sum));
			} else {
				dataMap.put("Total_" + crossTabData[2].toString() + "_" + crossTabData[3].toString(),
						crossTabData[0].toString());
			}

			if (dataMap.containsKey(crossTabData[1].toString() + "_Total_" + crossTabData[3].toString())) {
				int sum = Integer
						.parseInt(dataMap.get(crossTabData[1].toString() + "_Total_" + crossTabData[3].toString()));
				dataMap.put(crossTabData[1].toString() + "_Total_" + crossTabData[3].toString(),
						String.valueOf(Integer.parseInt(crossTabData[0].toString()) + sum));
			} else {
				dataMap.put(crossTabData[1].toString() + "_Total_" + crossTabData[3].toString(),
						crossTabData[0].toString());
			}

			if (dataMap.containsKey("Total_Total_" + crossTabData[3].toString())) {
				int sum = Integer.parseInt(dataMap.get("Total_Total_" + crossTabData[3].toString()));
				dataMap.put("Total_Total_" + crossTabData[3].toString(),
						String.valueOf(Integer.parseInt(crossTabData[0].toString()) + sum));
			} else {
				dataMap.put("Total_Total_" + crossTabData[3].toString(), crossTabData[0].toString());
			}

			// colSubGroups.add(crossTabData[2].toString());
			// rowSubGroups.add(crossTabData[1].toString());
			// timePeriods.add(crossTabData[3].toString());
		}

		List<Map<String, String>> tableDataMapList = new ArrayList<Map<String, String>>();
		Map<String, List<ValueObject>> valueObjectMap = new LinkedHashMap<String, List<ValueObject>>();
		List<ValueObject> timePeriodList = new ArrayList<ValueObject>();
		for (String colSubGroup : colSubGroups) {
			Map<String, String> tableDataMap = new LinkedHashMap<String, String>();

			tableDataMap.put("", crossTabDataModel.getCoLabel() + " (" + colSubGroup + ")");
			for (String rowSubGroup : rowSubGroups) {
				List<ValueObject> valueObjects = new ArrayList<ValueObject>();
				for (String timePeriod : timePeriods) {
					ValueObject valueObject = new ValueObject();
					valueObject.setKey(timePeriod);
					valueObject.setDescription(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")" + timePeriod);

					tableDataMap.put(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")" + timePeriod,
							dataMap.containsKey(rowSubGroup + "_" + colSubGroup + "_" + timePeriod)
									? dataMap.get(rowSubGroup + "_" + colSubGroup + "_" + timePeriod)
									: "-");

					valueObjects.add(valueObject);
				}

				if (!valueObjectMap.containsKey(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")")) {
					valueObjectMap.put(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")", valueObjects);
					timePeriodList.addAll(valueObjects);
				}
			}

			tableDataMapList.add(tableDataMap);
		}

		tableDatas.put("data", tableDataMapList);
		tableDatas.put("header", valueObjectMap);
		tableDatas.put("lowHeader", timePeriodList);
		return tableDatas;

	}

	private Object getCrossTabForAnyIntegerIndicator(CrossTabDataModel crossTabDataModel, List<String> xPathsIdCol,
			List<String> xPathsRow, int formId) {

		Map<String, Object> tableDatas = new LinkedHashMap<String, Object>();
		Map<String, String> dataMap = new LinkedHashMap<String, String>();

		List<Object[]> crossTabDatas = new ArrayList<Object[]>();

		Set<String> colSubGroups = new LinkedHashSet<String>();
		if (!(crossTabDataModel.getColIndicatorFormXpathMappingType().equalsIgnoreCase("integer")))
			colSubGroups = choiceDetailsRepository.findByXpathIdAndType(xPathsIdCol, formId);
		else {
			colSubGroups.add("Atleast One");
			colSubGroups.add("Atleast Two");
			colSubGroups.add("More Than Two");
		}

		Set<String> rowSubGroups = new LinkedHashSet<String>();
		if (!crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer"))
			rowSubGroups = choiceDetailsRepository.findByXpathIdAndType(xPathsRow, formId);
		else {
			rowSubGroups.add("Atleast One");
			rowSubGroups.add("Atleast Two");
			rowSubGroups.add("More Than Two");

		}

		Set<String> timePeriods = new LinkedHashSet<String>();
		Set<Integer> timePeriodIds = new LinkedHashSet<Integer>();
		if (crossTabDataModel.getTimePeriodId() != 0)

		{
			TimePeriod timePeriod = timePeriodRepository.findByTimePeriodId(crossTabDataModel.getTimePeriodId());
			timePeriods.add(timePeriod.getTimeperiod());
			timePeriodIds.add(timePeriod.getTimePeriodId());

		} else {
			List<TimePeriod> timPeriods = timePeriodRepository.findByOrderByTimePeriodIdDesc();

			for (TimePeriod timePeriod : timPeriods) {
				timePeriods.add(timePeriod.getTimeperiod());
				timePeriodIds.add(timePeriod.getTimePeriodId());
			}

		}

		if (crossTabDataModel.getDistrictId() == 0) {

			if (!(crossTabDataModel.getColIndicatorFormXpathMappingType().equalsIgnoreCase("integer")
					&& crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer"))) {

				if (crossTabDataModel.getColIndicatorFormXpathMappingType().equalsIgnoreCase("integer")) {
					crossTabDatas = rawDataScoreRepository.findCrossTabForOnlyOneIntegerType(xPathsRow, xPathsIdCol,
							timePeriodIds, formId);

					for (Object[] crossTabData : crossTabDatas) {
						dataMap.put(crossTabData[3].toString() + "_Atleast One_" + crossTabData[4].toString(),
								crossTabData[0].toString());
						dataMap.put(crossTabData[3].toString() + "_Atleast Two_" + crossTabData[4].toString(),
								crossTabData[1].toString());
						dataMap.put(crossTabData[3].toString() + "_More Than Two_" + crossTabData[4].toString(),
								crossTabData[2].toString());

					}
				}

				else if (crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer")) {

					crossTabDatas = rawDataScoreRepository.findCrossTabForOnlyOneIntegerType(xPathsIdCol, xPathsRow,
							timePeriodIds, formId);

					for (Object[] crossTabData : crossTabDatas) {
						dataMap.put("Atleast One_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
								crossTabData[0].toString());
						dataMap.put("Atleast Two_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
								crossTabData[1].toString());
						dataMap.put("More Than Two_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
								crossTabData[2].toString());

					}

				}
			} else {
				crossTabDatas = rawDataScoreRepository.findCrossTabForOnlyIntegerType(xPathsRow, xPathsIdCol,
						timePeriodIds);

				for (Object[] crossTabData : crossTabDatas) {
					dataMap.put("Atleast One_Atleast One_" + crossTabData[9].toString(), crossTabData[0].toString());
					dataMap.put("Atleast One_Atleast Two_" + crossTabData[9].toString(), crossTabData[1].toString());
					dataMap.put("Atleast One_More Than Two_" + crossTabData[9].toString(), crossTabData[2].toString());

					dataMap.put("Atleast Two_Atleast One_" + crossTabData[9].toString(), crossTabData[3].toString());
					dataMap.put("Atleast Two_Atleast Two_" + crossTabData[9].toString(), crossTabData[4].toString());
					dataMap.put("Atleast Two_More Than Two_" + crossTabData[9].toString(), crossTabData[5].toString());

					dataMap.put("More Than Two_Atleast One_" + crossTabData[9].toString(), crossTabData[6].toString());
					dataMap.put("More Than Two_Atleast Two_" + crossTabData[9].toString(), crossTabData[7].toString());
					dataMap.put("More Than Two_More Than Two_" + crossTabData[9].toString(),
							crossTabData[8].toString());

				}

			}

		} else {

			if (!(crossTabDataModel.getColIndicatorFormXpathMappingType().equalsIgnoreCase("integer")
					&& crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer"))) {

				if (crossTabDataModel.getColIndicatorFormXpathMappingType().equalsIgnoreCase("integer")) {
					crossTabDatas = rawDataScoreRepository.findCrossTabForOnlyOneIntegerTypeADistrict(xPathsRow,
							xPathsIdCol, timePeriodIds, crossTabDataModel.getDistrictId(), formId);

					for (Object[] crossTabData : crossTabDatas) {
						dataMap.put(crossTabData[3].toString() + "_Atleast One_" + crossTabData[4].toString(),
								crossTabData[0].toString());
						dataMap.put(crossTabData[3].toString() + "_Atleast Two_" + crossTabData[4].toString(),
								crossTabData[1].toString());
						dataMap.put(crossTabData[3].toString() + "_More Than Two_" + crossTabData[4].toString(),
								crossTabData[2].toString());

					}

				}

				else if (crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer")) {

					crossTabDatas = rawDataScoreRepository.findCrossTabForOnlyOneIntegerTypeADistrict(xPathsIdCol,
							xPathsRow, timePeriodIds, crossTabDataModel.getDistrictId(), formId);

					for (Object[] crossTabData : crossTabDatas) {
						dataMap.put("Atleast One_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
								crossTabData[0].toString());
						dataMap.put("Atleast Two_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
								crossTabData[1].toString());
						dataMap.put("More Than Two_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
								crossTabData[2].toString());

					}

				}
			}

			else {

				crossTabDatas = rawDataScoreRepository.findCrossTabForOnlyIntegerTypeForADistrict(xPathsRow,
						xPathsIdCol, timePeriodIds, crossTabDataModel.getDistrictId());

				for (Object[] crossTabData : crossTabDatas) {
					dataMap.put("Atleast One_Atleast One_" + crossTabData[9].toString(), crossTabData[0].toString());
					dataMap.put("Atleast One_Atleast Two_" + crossTabData[9].toString(), crossTabData[1].toString());
					dataMap.put("Atleast One_More Than Two_" + crossTabData[9].toString(), crossTabData[2].toString());

					dataMap.put("Atleast Two_Atleast One_" + crossTabData[9].toString(), crossTabData[3].toString());
					dataMap.put("Atleast Two_Atleast Two_" + crossTabData[9].toString(), crossTabData[4].toString());
					dataMap.put("Atleast Two_More Than Two_" + crossTabData[9].toString(), crossTabData[5].toString());

					dataMap.put("More Than Two_Atleast One_" + crossTabData[9].toString(), crossTabData[6].toString());
					dataMap.put("More Than Two_Atleast Two_" + crossTabData[9].toString(), crossTabData[7].toString());
					dataMap.put("More Than Two_More Than Two_" + crossTabData[9].toString(),
							crossTabData[8].toString());

				}

			}

		}

		List<Map<String, String>> tableDataMapList = new ArrayList<Map<String, String>>();
		Map<String, List<ValueObject>> valueObjectMap = new LinkedHashMap<String, List<ValueObject>>();
		List<ValueObject> timePeriodList = new ArrayList<ValueObject>();
		for (String colSubGroup : colSubGroups) {
			Map<String, String> tableDataMap = new LinkedHashMap<String, String>();

			tableDataMap.put("", crossTabDataModel.getCoLabel() + " (" + colSubGroup + ")");
			for (String rowSubGroup : rowSubGroups) {
				List<ValueObject> valueObjects = new ArrayList<ValueObject>();
				for (String timePeriod : timePeriods) {
					ValueObject valueObject = new ValueObject();
					valueObject.setKey(timePeriod);
					valueObject.setDescription(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")" + timePeriod);
					tableDataMap.put(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")" + timePeriod,
							dataMap.containsKey(rowSubGroup + "_" + colSubGroup + "_" + timePeriod)
									? dataMap.get(rowSubGroup + "_" + colSubGroup + "_" + timePeriod)
									: "-");
					valueObjects.add(valueObject);
				}

				if (!valueObjectMap.containsKey(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")")) {
					valueObjectMap.put(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")", valueObjects);
					timePeriodList.addAll(valueObjects);
				}
			}

			tableDataMapList.add(tableDataMap);
		}

		tableDatas.put("data", tableDataMapList);
		tableDatas.put("header", valueObjectMap);
		tableDatas.put("lowHeader", timePeriodList);
		return tableDatas;

	}

//	private Object getFacilityWiseCrossTabData(CrossTabDataModel crossTabDataModel, List<String> xPathsIdCol,
//			List<String> xPathsRow) {
//
//		Set<String> rowSubGroups = new LinkedHashSet<String>();
//		Map<String, Object> tableDatas = new LinkedHashMap<String, Object>();
//		Map<String, String> dataMap = new LinkedHashMap<String, String>();
//		List<Object[]> crossTabDatas = new ArrayList<Object[]>();
//		if (!crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer")) {
//			rowSubGroups = choiceDetailsRepository.findByXpathIdAndType(xPathsRow);
//			rowSubGroups.add("Total");
//		} else {
//			rowSubGroups.add("Atleast One");
//			rowSubGroups.add("Atleast Two");
//			rowSubGroups.add("More Than Two");
//		}
//
//		Set<String> colSubGroups = new LinkedHashSet<String>();
//		colSubGroups.add("DH");
//		colSubGroups.add("CHC");
//		colSubGroups.add("PHC");
//		if (!crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer"))
//			colSubGroups.add("Total");
//
//		Set<String> timePeriods = new LinkedHashSet<String>();
//		Set<Integer> timePeriodIds = new LinkedHashSet<Integer>();
//		if (crossTabDataModel.getTimePeriodId() != 0)
//
//		{
//			TimePeriod timePeriod = timePeriodRepository.findByTimePeriodId(crossTabDataModel.getTimePeriodId());
//			timePeriods.add(timePeriod.getTimeperiod());
//			timePeriodIds.add(timePeriod.getTimePeriodId());
//
//		} else {
//			List<TimePeriod> timPeriods = timePeriodRepository.findByOrderByTimePeriodIdDesc();
//
//			for (TimePeriod timePeriod : timPeriods) {
//				timePeriods.add(timePeriod.getTimeperiod());
//				timePeriodIds.add(timePeriod.getTimePeriodId());
//			}
//
//		}
//
//		if (crossTabDataModel.getDistrictId() != 0) {
//			if (crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer"))
//				crossTabDatas = rawDataScoreRepository.findCrossTabForOnlyOneIntegerTypeADistrictByFacilityWise(
//						xPathsRow, timePeriodIds, crossTabDataModel.getDistrictId());
//
//			else
//				crossTabDatas = rawDataScoreRepository.findCrossTabForChoiceTypeADistrictByFacilityWise(xPathsRow,
//						timePeriodIds, crossTabDataModel.getDistrictId());
//
//		}
//
//		else {
//			if (crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer"))
//				crossTabDatas = rawDataScoreRepository.findCrossTabForOnlyOneIntegerTypeByFacilityWise(xPathsRow,
//						timePeriodIds);
//
//			else
//				crossTabDatas = rawDataScoreRepository.findCrossTabForChoiceTypetByFacilityWise(xPathsRow,
//						timePeriodIds);
//
//		}
//		if (crossTabDataModel.getRowIndicatorFormXpathMappingType().equalsIgnoreCase("integer")) {
//			for (Object[] crossTabData : crossTabDatas) {
//				dataMap.put("Atleast One_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
//						crossTabData[0].toString());
//				dataMap.put("Atleast Two_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
//						crossTabData[1].toString());
//				dataMap.put("More Than Two_" + crossTabData[3].toString() + "_" + crossTabData[4].toString(),
//						crossTabData[2].toString());
//			}
//		}
//
//		else {
//			for (Object[] crossTabData : crossTabDatas) {
//				dataMap.put(crossTabData[1].toString() + "_" + crossTabData[2].toString() + "_"
//						+ crossTabData[3].toString(), crossTabData[0].toString());
//
//				if (dataMap.containsKey("Total_" + crossTabData[2].toString() + "_" + crossTabData[3].toString())) {
//					int sum = Integer.parseInt(
//							dataMap.get("Total_" + crossTabData[2].toString() + "_" + crossTabData[3].toString()));
//					dataMap.put("Total_" + crossTabData[2].toString() + "_" + crossTabData[3].toString(),
//							String.valueOf(Integer.parseInt(crossTabData[0].toString()) + sum));
//				} else {
//					dataMap.put("Total_" + crossTabData[2].toString() + "_" + crossTabData[3].toString(),
//							crossTabData[0].toString());
//				}
//
//				if (dataMap.containsKey(crossTabData[1].toString() + "_0_" + crossTabData[3].toString())) {
//					int sum = Integer
//							.parseInt(dataMap.get(crossTabData[1].toString() + "_0_" + crossTabData[3].toString()));
//					dataMap.put(crossTabData[1].toString() + "_0_" + crossTabData[3].toString(),
//							String.valueOf(Integer.parseInt(crossTabData[0].toString()) + sum));
//				} else {
//					dataMap.put(crossTabData[1].toString() + "_0_" + crossTabData[3].toString(),
//							crossTabData[0].toString());
//				}
//
//				if (dataMap.containsKey("Total_0_" + crossTabData[3].toString())) {
//					int sum = Integer.parseInt(dataMap.get("Total_0_" + crossTabData[3].toString()));
//					dataMap.put("Total_0_" + crossTabData[3].toString(),
//							String.valueOf(Integer.parseInt(crossTabData[0].toString()) + sum));
//				} else {
//					dataMap.put("Total_0_" + crossTabData[3].toString(), crossTabData[0].toString());
//				}
//			}
//		}
//
//		List<Map<String, String>> tableDataMapList = new ArrayList<Map<String, String>>();
//		Map<String, List<ValueObject>> valueObjectMap = new LinkedHashMap<String, List<ValueObject>>();
//		List<ValueObject> timePeriodList = new ArrayList<ValueObject>();
//
//		for (String colSubGroup : colSubGroups) {
//			int i = 0;
//			switch (colSubGroup) {
//			case "PHC":
//				i = Constants.PHC_LEVEL;
//				break;
//
//			case "CHC":
//				i = Constants.CHC_LEVEL;
//				break;
//
//			case "DH":
//				i = Constants.DH_LEVEL;
//				break;
//
//			}
//			Map<String, String> tableDataMap = new LinkedHashMap<String, String>();
//
//			tableDataMap.put("", colSubGroup);
//			for (String rowSubGroup : rowSubGroups) {
//				List<ValueObject> valueObjects = new ArrayList<ValueObject>();
//				for (String timePeriod : timePeriods) {
//					ValueObject valueObject = new ValueObject();
//					valueObject.setKey(timePeriod);
//					valueObject.setDescription(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")" + timePeriod);
//					tableDataMap.put(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")" + timePeriod,
//							dataMap.containsKey(rowSubGroup + "_" + i + "_" + timePeriod)
//									? dataMap.get(rowSubGroup + "_" + i + "_" + timePeriod)
//									: "-");
//
//					valueObjects.add(valueObject);
//				}
//
//				if (!valueObjectMap.containsKey(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")")) {
//					valueObjectMap.put(crossTabDataModel.getRowLabel() + " (" + rowSubGroup + ")", valueObjects);
//					timePeriodList.addAll(valueObjects);
//				}
//			}
//
//			tableDataMapList.add(tableDataMap);
//		}
//
//		tableDatas.put("data", tableDataMapList);
//		tableDatas.put("header", valueObjectMap);
//		tableDatas.put("lowHeader", timePeriodList);
//		return tableDatas;
//	}

	@Override
	public List<String> getAllRawDataReportsList() {
		List<String> rdr = new ArrayList();
		rdr.add("data1");
		rdr.add("raw data2");
		return rdr;
	}
}
