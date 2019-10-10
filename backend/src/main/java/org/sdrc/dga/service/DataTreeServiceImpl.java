package org.sdrc.dga.service;

/**
 * @author Harsh(harsh@sdrc.co.in)
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.FacilityScore;
import org.sdrc.dga.domain.FormXpathScoreMapping;
import org.sdrc.dga.model.BubbleDataModel;
import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.repository.AreaRepository;
import org.sdrc.dga.repository.FacilityScoreRepository;
import org.sdrc.dga.repository.FormXpathScoreMappingRepository;
import org.sdrc.dga.repository.LastVisitDataRepository;
import org.sdrc.dga.util.Constants;
import org.sdrc.dga.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataTreeServiceImpl implements DataTreeService {

	@Autowired
	private FormXpathScoreMappingRepository formXpathScoreMappingRepository;

	@Autowired
	private LastVisitDataRepository lastVisitDataRepository;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private FacilityScoreRepository facilityScoreRepository;

	private static DecimalFormat df = new DecimalFormat(".##");

	@Override
	@Transactional(readOnly=true)
	public List<BubbleDataModel> getBubbleChartData(Integer sectorId,int areaId,int timeperiodId) {
		FormXpathScoreMapping formXpathScoreMapping = formXpathScoreMappingRepository
				.findByFormXpathScoreId(sectorId);

		List<BubbleDataModel> bubbleDataModels = new ArrayList<BubbleDataModel>();

		Integer UserAreaId = ((CollectUserModel) stateManager
				.getValue(Constants.USER_PRINCIPAL))
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel()
				.getAreaId();
		Double bubbleRadius = 0.0;
		String color1 = "#D13F43";
		String color2 = "#F19537";
		String color3 = "#22B369";
		String color = "";
		
		

		// if login type is of district then the respective district data will be shown only		
		if (((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL))
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel()
				.getAreaLevelId() > 3
				&& ((CollectUserModel) stateManager
						.getValue(Constants.USER_PRINCIPAL))
						.getUserRoleFeaturePermissionMappings().get(0)
						.getRoleFeaturePermissionSchemeModel().getAreaModel()
						.getParentAreaId() != -1) {
			
			// if request data is for formId=44 i.e. DH
			if (formXpathScoreMapping.getForm().getAreaLevel().getAreaLevelId() ==Constants.DH_LEVEL) {
				
				for (FacilityScore facilityScore : facilityScoreRepository.findByFormXpathScoreMappingAndLastVisitDataIsLiveTrueAndLastVisitDataAreaParentAreaIdInAndLastVisitDataTimPeriodTimePeriodId(formXpathScoreMapping,Arrays.asList(UserAreaId),timeperiodId) ) {
					if (facilityScore.getLastVisitData().isLive()
							&& facilityScore.getLastVisitData().getArea()
									.getParentAreaId() == UserAreaId) {
						BubbleDataModel bubbleDataModel = new BubbleDataModel();

						bubbleDataModel.setAreaId(facilityScore
								.getLastVisitData().getArea().getAreaId());
						bubbleDataModel.setDistrictName(((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL))
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaName());
						bubbleDataModel.setName(facilityScore
								.getLastVisitData().getArea().getAreaName());
						if (facilityScore.getScore() == null
								|| facilityScore.getMaxScore() == null) {
							bubbleDataModel.setValue(0.00);
						} else {
							bubbleDataModel
									.setValue((double) Math
											.round((facilityScore.getScore() / facilityScore
													.getMaxScore()) * 100));
						}
						// Setting color
						if (bubbleDataModel.getValue() >= 80) {
							color = color3;
						} else if (bubbleDataModel.getValue() >= 60
								&& bubbleDataModel.getValue() < 80) {
							color = color2;
						} else if (bubbleDataModel.getValue() >= 0
								&& bubbleDataModel.getValue() < 60) {
							color = color1;
						}
						bubbleDataModel.setColor(color);

						// setting bubble Size
						if (bubbleDataModel.getValue() >= 80) {
							bubbleRadius = Math
									.sqrt(bubbleDataModel.getValue()) * 1.6;
						} else if (bubbleDataModel.getValue() >= 60
								&& bubbleDataModel.getValue() < 80) {
							bubbleRadius = Math
									.sqrt(bubbleDataModel.getValue()) * 1.4;
						} else if (bubbleDataModel.getValue() >= 0
								&& bubbleDataModel.getValue() < 60) {
							bubbleRadius = Math
									.sqrt(bubbleDataModel.getValue()) * 1.2;
						}
						bubbleDataModel.setSize(bubbleRadius);

						bubbleDataModels.add(bubbleDataModel);

					}

				}
			} else

				// For form of CHC AND PHC
			{

				List<Integer> parentIds = areaRepository
						.findAreaIdByParentAreaId(UserAreaId);
				for (FacilityScore facilityScore :facilityScoreRepository.findByFormXpathScoreMappingAndLastVisitDataIsLiveTrueAndLastVisitDataAreaParentAreaIdInAndLastVisitDataTimPeriodTimePeriodId(formXpathScoreMapping,parentIds,timeperiodId)) {
					
					if (facilityScore.getLastVisitData().isLive()
							&& parentIds.contains(facilityScore
									.getLastVisitData().getArea()
									.getParentAreaId())) {
						BubbleDataModel bubbleDataModel = new BubbleDataModel();

						bubbleDataModel.setDistrictName(((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL))
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaName());
						bubbleDataModel.setAreaId(facilityScore
								.getLastVisitData().getArea().getAreaId());
						bubbleDataModel.setName(facilityScore
								.getLastVisitData().getArea().getAreaName());
						if (facilityScore.getScore() == null
								|| facilityScore.getMaxScore() == null) {
							bubbleDataModel.setValue(0.00);
						} else {
							bubbleDataModel
									.setValue((double) Math
											.round((facilityScore.getScore() / facilityScore
													.getMaxScore()) * 100));
						}
						// Setting color
						if (bubbleDataModel.getValue() >= 80) {
							color = color3;
						} else if (bubbleDataModel.getValue() >= 60
								&& bubbleDataModel.getValue() < 80) {
							color = color2;
						} else if (bubbleDataModel.getValue() >= 0
								&& bubbleDataModel.getValue() < 60) {
							color = color1;
						}
						bubbleDataModel.setColor(color);

						// setting bubble Size
						if (bubbleDataModel.getValue() >= 80) {
							bubbleRadius = Math
									.sqrt(bubbleDataModel.getValue()) * 1.6;
						} else if (bubbleDataModel.getValue() >= 60
								&& bubbleDataModel.getValue() < 80) {
							bubbleRadius = Math
									.sqrt(bubbleDataModel.getValue()) * 1.4;
						} else if (bubbleDataModel.getValue() >= 0
								&& bubbleDataModel.getValue() < 60) {
							bubbleRadius = Math
									.sqrt(bubbleDataModel.getValue()) * 1.2;
						}
						bubbleDataModel.setSize(bubbleRadius);

						bubbleDataModels.add(bubbleDataModel);

					}

				}
			}
		} 
		
		// for state and national level type
		else if(areaId==0)
		{
			List<Area> areas =areaRepository.findAll();
			Map<Integer,Area> parentAreaMap=new HashMap<Integer, Area>();
			for(Area area:areas)
			{
				if(area.getAreaLevel().getAreaLevelId()==4||area.getAreaLevel().getAreaLevelId()==5)
				{
					parentAreaMap.put(area.getAreaId(), area);
					
				}
			}
			
			for (FacilityScore facilityScore : facilityScoreRepository.findByFormXpathScoreMappingAndLastVisitDataIsLiveTrueAndLastVisitDataTimPeriodTimePeriodId(formXpathScoreMapping,timeperiodId)) {
				if (facilityScore.getLastVisitData().isLive()) {
					BubbleDataModel bubbleDataModel = new BubbleDataModel();

					bubbleDataModel.setAreaId(facilityScore.getLastVisitData()
							.getArea().getAreaId());
					bubbleDataModel.setName(facilityScore.getLastVisitData()
							.getArea().getAreaName());
					if (facilityScore.getScore() == null
							|| facilityScore.getMaxScore() == null) {
						bubbleDataModel.setValue(0.00);
					} else {
						bubbleDataModel
								.setValue((double) Math.round((facilityScore
										.getScore() / facilityScore
										.getMaxScore()) * 100));
					}
					// Setting color
					if (bubbleDataModel.getValue() >= 80) {
						color = color3;
					} else if (bubbleDataModel.getValue() >= 60
							&& bubbleDataModel.getValue() < 80) {
						color = color2;
					} else if (bubbleDataModel.getValue() >= 0
							&& bubbleDataModel.getValue() < 60) {
						color = color1;
					}
					bubbleDataModel.setColor(color);

					// setting bubble Size
					if (bubbleDataModel.getValue() >= 80) {
						bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.6;
					} else if (bubbleDataModel.getValue() >= 60
							&& bubbleDataModel.getValue() < 80) {
						bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.4;
					} else if (bubbleDataModel.getValue() >= 0
							&& bubbleDataModel.getValue() < 60) {
						bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.2;
					}
					bubbleDataModel.setSize(bubbleRadius);
					//setting district name of the DH
					if (formXpathScoreMapping.getForm().getAreaLevel().getAreaLevelId() ==Constants.DH_LEVEL)
						
					{
						bubbleDataModel.setDistrictName(parentAreaMap.get(facilityScore.getLastVisitData()
								.getArea().getParentAreaId()).getAreaName());
						bubbleDataModel.setDistrictId(parentAreaMap.get(facilityScore.getLastVisitData()
								.getArea().getParentAreaId()).getAreaId());
					}
					//setting district name of the phc or chc
					else
					{
//						System.out.println("value of 13->"+parentAreaMap.get(parentAreaMap.get(13).getParentAreaId()).getAreaId());
//						System.out.println(facilityScore.getLastVisitData()
//								.getArea().getParentAreaId());
//						int i= parentAreaMap.get(parentAreaMap.get(facilityScore.getLastVisitData()
//								.getArea().getParentAreaId()).getParentAreaId()).getAreaId();
//						System.out.println("dId->"+i);
						
						if(parentAreaMap.get(facilityScore.getLastVisitData()
								.getArea().getParentAreaId()).getParentAreaId()!=2) {
						bubbleDataModel.setDistrictName(parentAreaMap.get(parentAreaMap.get(facilityScore.getLastVisitData()
								.getArea().getParentAreaId()).getParentAreaId()).getAreaName());
						bubbleDataModel.setDistrictId(parentAreaMap.get(parentAreaMap.get(facilityScore.getLastVisitData()
								.getArea().getParentAreaId()).getParentAreaId()).getAreaId());
						}
					}
					bubbleDataModels.add(bubbleDataModel);

				}

			}

		}
		// District Filter
		else
		{

			List<Area> areas =areaRepository.findAll();
			Map<Integer,Area> parentAreaMap=new HashMap<Integer, Area>();
			for(Area area:areas)
			{
				if(area.getAreaLevel().getAreaLevelId()==4||area.getAreaLevel().getAreaLevelId()==5)
				{
					parentAreaMap.put(area.getAreaId(), area);
					
				}
			}
			
			// if DH TYPE
			if(formXpathScoreMapping.getForm().getAreaLevel().getAreaLevelId() ==Constants.DH_LEVEL)
					{
			for (FacilityScore facilityScore :  facilityScoreRepository.findByFormXpathScoreMappingAndLastVisitDataIsLiveTrueAndLastVisitDataAreaParentAreaIdInAndLastVisitDataTimPeriodTimePeriodId(formXpathScoreMapping,Arrays.asList(areaId),timeperiodId)) {
				if (facilityScore.getLastVisitData().isLive() && facilityScore.getLastVisitData().getArea().getParentAreaId()==areaId)
				{
					BubbleDataModel bubbleDataModel = new BubbleDataModel();

					bubbleDataModel.setAreaId(facilityScore.getLastVisitData()
							.getArea().getAreaId());
					bubbleDataModel.setName(facilityScore.getLastVisitData()
							.getArea().getAreaName());
					if (facilityScore.getScore() == null
							|| facilityScore.getMaxScore() == null) {
						bubbleDataModel.setValue(0.00);
					} else {
						bubbleDataModel
								.setValue((double) Math.round((facilityScore
										.getScore() / facilityScore
										.getMaxScore()) * 100));
					}
					// Setting color
					if (bubbleDataModel.getValue() >= 80) {
						color = color3;
					} else if (bubbleDataModel.getValue() >= 60
							&& bubbleDataModel.getValue() < 80) {
						color = color2;
					} else if (bubbleDataModel.getValue() >= 0
							&& bubbleDataModel.getValue() < 60) {
						color = color1;
					}
					bubbleDataModel.setColor(color);

					// setting bubble Size
					if (bubbleDataModel.getValue() >= 80) {
						bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.6;
					} else if (bubbleDataModel.getValue() >= 60
							&& bubbleDataModel.getValue() < 80) {
						bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.4;
					} else if (bubbleDataModel.getValue() >= 0
							&& bubbleDataModel.getValue() < 60) {
						bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.2;
					}
					bubbleDataModel.setSize(bubbleRadius);
					//setting district name of the DH

						bubbleDataModel.setDistrictName(parentAreaMap.get(facilityScore.getLastVisitData()
								.getArea().getParentAreaId()).getAreaName());
						bubbleDataModel.setDistrictId(parentAreaMap.get(facilityScore.getLastVisitData()
								.getArea().getParentAreaId()).getAreaId());

					bubbleDataModels.add(bubbleDataModel);

				}

			}

		
		}
			// For chc or PHC
			else
			{
				List<Integer> parentIds = areaRepository
						.findAreaIdByParentAreaId(areaId);
				for (FacilityScore facilityScore :facilityScoreRepository.findByFormXpathScoreMappingAndLastVisitDataIsLiveTrueAndLastVisitDataAreaParentAreaIdInAndLastVisitDataTimPeriodTimePeriodId(formXpathScoreMapping,parentIds,timeperiodId)) {
					if (facilityScore.getLastVisitData().isLive() && parentAreaMap.get(parentAreaMap.get(facilityScore.getLastVisitData()
							.getArea().getParentAreaId()).getParentAreaId()).getAreaId()==areaId)
					{
						BubbleDataModel bubbleDataModel = new BubbleDataModel();

						bubbleDataModel.setAreaId(facilityScore.getLastVisitData()
								.getArea().getAreaId());
						bubbleDataModel.setName(facilityScore.getLastVisitData()
								.getArea().getAreaName());
						if (facilityScore.getScore() == null
								|| facilityScore.getMaxScore() == null) {
							bubbleDataModel.setValue(0.00);
						} else {
							bubbleDataModel
									.setValue((double) Math.round((facilityScore
											.getScore() / facilityScore
											.getMaxScore()) * 100));
						}
						// Setting color
						if (bubbleDataModel.getValue() >= 80) {
							color = color3;
						} else if (bubbleDataModel.getValue() >= 60
								&& bubbleDataModel.getValue() < 80) {
							color = color2;
						} else if (bubbleDataModel.getValue() >= 0
								&& bubbleDataModel.getValue() < 60) {
							color = color1;
						}
						bubbleDataModel.setColor(color);

						// setting bubble Size
						if (bubbleDataModel.getValue() >= 80) {
							bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.6;
						} else if (bubbleDataModel.getValue() >= 60
								&& bubbleDataModel.getValue() < 80) {
							bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.4;
						} else if (bubbleDataModel.getValue() >= 0
								&& bubbleDataModel.getValue() < 60) {
							bubbleRadius = Math.sqrt(bubbleDataModel.getValue()) * 1.2;
						}
						bubbleDataModel.setSize(bubbleRadius);
						//setting district name of the PHC and CHC

							bubbleDataModel.setDistrictName(parentAreaMap.get(parentAreaMap.get(facilityScore.getLastVisitData()
									.getArea().getParentAreaId()).getParentAreaId()).getAreaName());
							bubbleDataModel.setDistrictId(parentAreaMap.get(parentAreaMap.get(facilityScore.getLastVisitData()
									.getArea().getParentAreaId()).getParentAreaId()).getAreaId());
						bubbleDataModels.add(bubbleDataModel);

					}

				}

			
			
				
			}
		
		}
		return bubbleDataModels;
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> fetchTreeData(int stateId,int timePeriodId,int programId) {
		Map<String, Object> resultMap = new HashMap<>();
		List<FormXpathScoreMapping> formXpathScoreMappings = formXpathScoreMappingRepository
				.findByParentXpathIdAndFormStateAreaIdAndFormTimePeriodTimePeriodIdAndFormProgramXFormMappingProgramProgramIdOrderByFormAreaLevelAreaLevelId(-1,stateId,timePeriodId,programId);
		List<FormXpathScoreMapping> childFormXpathScoreMappings = formXpathScoreMappingRepository
				.findByFormStateAreaIdAndFormTimePeriodTimePeriodId(stateId,timePeriodId);
		resultMap.put("name", "Facility Level");
		List<Map<String, Object>> parentList = new ArrayList<>();

		for (FormXpathScoreMapping formXpathScoreMapping : formXpathScoreMappings) {
			Map<String, Object> indMap = new HashMap<>();
			indMap.put(
					"name",
					formXpathScoreMapping.getLabel().split("Total score for")[1]);
			indMap.put("Id", formXpathScoreMapping.getFormXpathScoreId());
			List<Map<String, Object>> childList = new ArrayList<>();

			for (FormXpathScoreMapping childFormXpathScoreMapping : childFormXpathScoreMappings) {
				if (childFormXpathScoreMapping.getParentXpathId() == formXpathScoreMapping
						.getFormXpathScoreId()) {
					Map<String, Object> childMap = new HashMap<>();
					childMap.put("name", childFormXpathScoreMapping.getLabel());
					childMap.put("Id",
							childFormXpathScoreMapping.getFormXpathScoreId());
					childList.add(childMap);
				}

			}
			indMap.put("children", childList);
			parentList.add(indMap);
		}

		resultMap.put("children", parentList);
		return resultMap;
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> forceLayoutData(Integer sectorId, Integer areaId,int timeperiodId) {
		Map<String, Object> resultMap = new HashMap<>();

		String color1 = "#D13F43";
		String color2 = "#F19537";
		String color3 = "#22B369";
		String color4 = "6868F3";
		String color5="#299dd6";

		String color = "";

		List<Object[]> parentLastVisitDatas = lastVisitDataRepository
				.getDataBySectorIdIdAndDistrictAreaId(sectorId, areaId,timeperiodId);

		Object[] parentLastVisitData = parentLastVisitDatas.get(0);

		resultMap.put("qsname", parentLastVisitData[0].toString());
		resultMap.put("name", parentLastVisitData[0].toString());
		resultMap.put("value",parentLastVisitData[1]==null?0.0:df.format(parentLastVisitData[1]));
		resultMap.put("size", "300");
		if(parentLastVisitData[1]==null)
		
			color=color1;
		
		else if (Double.parseDouble(parentLastVisitData[1].toString()) >= 80)
			color = color3;
		else if (80 > Double.parseDouble(parentLastVisitData[1].toString())
				&& Double.parseDouble(parentLastVisitData[1].toString()) >= 60)
			color = color2;
		else
			color = color1;
		resultMap.put("color", color);
		List<Map<String, Object>> parentList = new ArrayList<>();

		resultMap.put("size", "300");

		List<Object[]> childlastVisitDatas = lastVisitDataRepository
				.getDataByparentSectorIdIdAndDistrictAreaId(sectorId, areaId,timeperiodId);
//		
//		List<Integer> sectorIds=childlastVisitDatas.stream().map(arg0) 
//		lastVisitDataRepository
//		.getDataByparentIdsInSectorIdIdAndDistrictAreaId(sectorId, areaId,timeperiodId);

		for (Object[] childlastVisitData : childlastVisitDatas) {
			Map<String, Object> indMap = new HashMap<>();
			indMap.put("qsname", childlastVisitData[0]);
			indMap.put("name", childlastVisitData[0]);
			indMap.put("value", childlastVisitData[1] == null ? "N/A"
					:childlastVisitData[1]);
			if(childlastVisitData[4].toString().equalsIgnoreCase("select_one yes_no")|| childlastVisitData[4].toString().equalsIgnoreCase("integer"))
			{
				if(childlastVisitData[4].toString().equalsIgnoreCase("select_one yes_no"))
				{
					indMap.put("value", childlastVisitData[1] == null ? "N/A"
							:Double.parseDouble(childlastVisitData[1].toString()) >0?"Yes":"No");
				
				}
				indMap.put("response", true);
				indMap.put("size", "200");
				indMap.put("color", color5);
			}
			else
			{
				indMap.put("wt", childlastVisitData[2] == null ? 0
						: childlastVisitData[2]);
				
				indMap.put("percentile",childlastVisitData[2] == null||childlastVisitData[1] == null?"0.0":
					df.format((Double.parseDouble(childlastVisitData[1]
							.toString()) / Double.parseDouble(childlastVisitData[2]
									.toString())) * 100)==".0"? "0.0"
						:df.format((Double.parseDouble(childlastVisitData[1]
						.toString()) / Double.parseDouble(childlastVisitData[2]
								.toString())) * 100));
				
				indMap.put("size", "200");
				if (childlastVisitData[2] == null||childlastVisitData[1] == null)
				{
					color=color1;
				}
				else
				{
				Double percetn = (Double.parseDouble(childlastVisitData[1]
						.toString()) / Double.parseDouble(childlastVisitData[2]
						.toString())) * 100;
				if (percetn >= 80)
					color = color3;
				else if (80 > percetn && percetn >= 60)
					color = color2;
				else
					color = color1;
				}
				indMap.put("color", color);
				}

			List<Map<String, Object>> childList = new ArrayList<>();
			for (Object[] childnodelastVisitData : lastVisitDataRepository
					.getDataByparentSectorIdIdAndDistrictAreaId(
							Integer.parseInt(childlastVisitData[3].toString()),
							areaId,timeperiodId)) {
				Map<String, Object> childMap = new HashMap<>();

				childMap.put("qsname", childnodelastVisitData[0]);
				childMap.put("name", childnodelastVisitData[0]);
				childMap.put("value", childnodelastVisitData[1] == null ? 0
						: childnodelastVisitData[1]);
				childMap.put("wt", childnodelastVisitData[2] == null ? 0
						: childnodelastVisitData[2]);
				childMap.put("size", "150");
				
				
				if(childnodelastVisitData[4].toString().equalsIgnoreCase("select_one yes_no")|| childnodelastVisitData[4].toString().equalsIgnoreCase("integer"))
				{
					if(childnodelastVisitData[4].toString().equalsIgnoreCase("select_one yes_no"))
					{
						childMap.put("value", childnodelastVisitData[1] == null ? "N/A"
								:Double.parseDouble(childnodelastVisitData[1].toString()) >0?"Yes":"No");
					}
					childMap.put("response", true);
					childMap.put("color", color5);
				}
				else
				{	childMap.put("wt", childnodelastVisitData[2] == null ? 0
						: childnodelastVisitData[2]);
				childMap.put("color", color4);
				}
				
				
				
				List<Map<String, Object>> child1List = new ArrayList<>();
				for (Object[] childnodelastVisitData1 : lastVisitDataRepository
						.getDataByparentSectorIdIdAndDistrictAreaId(
								Integer.parseInt(childnodelastVisitData[3].toString()),
								areaId,timeperiodId)) {
				Map<String, Object> child1Map = new HashMap<>();
				child1Map.put("qsname", childnodelastVisitData1[0]);
				child1Map.put("name", childnodelastVisitData1[0]);
				child1Map.put("value", childnodelastVisitData1[1] == null ?"N/A"
						: childnodelastVisitData1[1]);
				child1Map.put("size", "80");
				if(childnodelastVisitData1[4].toString().equalsIgnoreCase("select_one yes_no")|| childnodelastVisitData1[4].toString().equalsIgnoreCase("integer"))
				{
					if(childnodelastVisitData1[4].toString().equalsIgnoreCase("select_one yes_no"))
					{
						child1Map.put("value", childnodelastVisitData1[1] == null ? "N/A"
								:Double.parseDouble(childnodelastVisitData1[1].toString())>0?"Yes":"No");
					}
					child1Map.put("response", true);
				child1Map.put("color", color5);
				}
				else
				{
					child1Map.put("wt", childnodelastVisitData1[2] == null ? 0
							: childnodelastVisitData1[2]);
					child1Map.put("color", color4);
					
					
				}
//				childMap.put("color", color4);
				child1List.add(child1Map);
				}
				
				
				
				childMap.put("lastLevel", true);
				childMap.put("children",child1List);
				childList.add(childMap);
			}

			indMap.put("children", childList);
			parentList.add(indMap);
		}

		resultMap.put("children", parentList);
		return resultMap;
	}

	

}
