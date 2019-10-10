package org.sdrc.dga.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.FileUtils;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.FacilityPlanning;
import org.sdrc.dga.domain.FacilityScore;
import org.sdrc.dga.domain.FormXpathScoreMapping;
import org.sdrc.dga.domain.LastVisitData;
import org.sdrc.dga.domain.Program;
import org.sdrc.dga.domain.TimePeriod;
import org.sdrc.dga.domain.XForm;
import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.model.FacilityPlanningModel;
import org.sdrc.dga.model.FormXpathScoreMappingModel;
import org.sdrc.dga.model.GoogleMapDataModel;
import org.sdrc.dga.model.ProgramModel;
import org.sdrc.dga.model.ScoreModel;
import org.sdrc.dga.model.SpiderDataCollection;
import org.sdrc.dga.model.SpiderDataModel;
import org.sdrc.dga.model.TimePeriodModel;
import org.sdrc.dga.repository.AreaRepository;
import org.sdrc.dga.repository.FacilityPlanningRepository;
import org.sdrc.dga.repository.FacilityScoreRepository;
import org.sdrc.dga.repository.FormXpathScoreMappingRepository;
import org.sdrc.dga.repository.LastVisitDataRepository;
import org.sdrc.dga.repository.XFormRepository;
import org.sdrc.dga.util.Constants;
import org.sdrc.dga.util.DomainToModelConverter;
import org.sdrc.dga.util.HeaderFooter;
import org.sdrc.dga.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author Harekrishna Panigrahi
 * @author Sarita Panigrahi
 * @author Harsh Pratyush
 *
 */

@SuppressWarnings("deprecation")
@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private FacilityScoreRepository facilityScoreRepository;

	private static DecimalFormat df = new DecimalFormat(".#");

	@Autowired
	private LastVisitDataRepository lastVisitDataRepository;

	@Autowired
	private FormXpathScoreMappingRepository formXpathScoreMappingRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private MessageSource messages;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private ServletContext context;

	@Autowired
	private FacilityPlanningRepository facilityPlanningRepository;

	@Autowired
	private DomainToModelConverter domainToModelConverter;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");

	@Autowired
	private XFormRepository xFormRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ScoreModel> getAllAggregatedData(Integer formId) {

		List<Object[]> avgPercentValues = facilityScoreRepository.findAvgByFormId(formId);
		List<ScoreModel> scoreModels = new ArrayList<ScoreModel>();

		for (int i = 0; i < avgPercentValues.size(); i++) {

			// Map<String, Object> firstChildMap = new HashMap<>();
			Object[] objs = avgPercentValues.get(i);

			Integer formXpathScoreId = (Integer) objs[0];
			String label = (String) objs[1];
			Integer parentXpathId = (Integer) objs[2];
			Double percentScore = (Double) objs[3];
			Double maxScore = (Double) objs[4];
			Double score = percentScore != null ? (percentScore * maxScore) / 100 : null;

			ScoreModel scoreModel = new ScoreModel();
			scoreModel.setFormXpathScoreId(formXpathScoreId);
			scoreModel.setMaxScore(maxScore);
			scoreModel.setName(label);
			scoreModel.setParentXpathScoreId(parentXpathId);
			scoreModel.setPercentScore(percentScore != null && percentScore == 0.0 ? "0.0"
					: percentScore != null ? df.format(percentScore) : "NA");
			scoreModel.setScore(score);
			scoreModels.add(scoreModel);
		}
		return scoreModels;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GoogleMapDataModel> fetchAllGoogleMapData(Integer formId, Integer sectorId, Integer areaId,
			int timeperiodId) throws Exception {

		List<Object[]> objects;
		XForm xform = xFormRepository.findByFormId(formId);
		// if areaId is 0 then overall score of the state will shown
		if (areaId == 0) {
			//in database sectorId represented as formXpathScoreId inside table 
			objects = lastVisitDataRepository.getDataByFormId(formId, sectorId, timeperiodId);
		}

		// if areaId is Choosen and then if formId is 44 means DH Type then this will
		// get Executed
		else if (xform.getAreaLevel().getAreaLevelId() == Constants.DH_LEVEL) {
			objects = lastVisitDataRepository.getDataByFormIdAndAreaId(formId, sectorId, areaId, timeperiodId);
		}
		// if district filter is done or a district login is done and form type is not
		// of DH then this code will get executed
		else {
			objects = lastVisitDataRepository.getDataByFormIdAndDistrictAreaId(formId, sectorId, areaId, timeperiodId);
		}
		List<GoogleMapDataModel> googleMapDataModels = new ArrayList<>();

		for (Object[] obj : objects) {
			GoogleMapDataModel mapDataModel = new GoogleMapDataModel();

			Double dataValue = new Double(0.0);
			// Double numerator = new Double(0.0);
			// Double denominator = new Double(0.0);

			if (obj[0] instanceof LastVisitData) {
				LastVisitData lastVisitData = (LastVisitData) obj[0];
				mapDataModel.setId(lastVisitData.getLastVisitDataId());
				mapDataModel.setAreaID(lastVisitData.getArea().getAreaId().toString());
				mapDataModel.setLatitude(lastVisitData.getLatitude());
				mapDataModel.setLongitude(lastVisitData.getLongitude());
				mapDataModel.setShowWindow(false);
				mapDataModel.setDateOfVisit(
						null != lastVisitData.getDateOfVisit() ? lastVisitData.getDateOfVisit().toString() : null);
				mapDataModel.setTitle(lastVisitData.getArea().getAreaName());

				StringBuffer finalList = new StringBuffer();
				if (lastVisitData.getImageFileNames() != null) {
					String[] listOfImages = lastVisitData.getImageFileNames().split(",");
					for (String img : listOfImages) {
						finalList.append("resources/images/facilities/" + img + ",");
					}
					if (finalList.length() > 0) {
						finalList.replace(finalList.length() - 1, finalList.length(), "");
					}
					mapDataModel.setImages(finalList.toString());
				}

			}

			xform = null;
			if (obj[2] instanceof XForm) {
				xform = (XForm) obj[2];
			}

			if (obj[4] instanceof Double) {
				dataValue = (Double) obj[4];
			}
			// dataValue = ((numerator * 1.0)/(denominator * 1.0))*100;
			mapDataModel.setDataValue(
					dataValue != null && dataValue == 0.0 ? "0.0" : dataValue != null ? df.format(dataValue) : "NA");

			switch (xform.getAreaLevel().getAreaLevelId()) {
			
			case Constants.CHC_LEVEL:
			case Constants.UPHC_LEVEL:// CHC
				if (dataValue >= 80) {
					mapDataModel.setIcon("./assets/images/pushpins/CHC_green.png");
				} else if (dataValue >= 60 && dataValue < 80) {
					mapDataModel.setIcon("./assets/images/pushpins/CHC_orange.png");
				} else if (dataValue >= 0 && dataValue < 60) {
					mapDataModel.setIcon("./assets/images/pushpins/CHC_red.png");
				}
				break;
			case Constants.DH_LEVEL: // DH
				if (dataValue >= 80) {
					mapDataModel.setIcon("./assets/images/pushpins/DH_green.png");
				} else if (dataValue >= 60 && dataValue < 80) {
					mapDataModel.setIcon("./assets/images/pushpins/DH_orange.png");
				} else if (dataValue >= 0 && dataValue < 60) {
					mapDataModel.setIcon("./assets/images/pushpins/DH_red.png");
				}
				break;
			case Constants.PHC_LEVEL:// PHC
				if (dataValue >= 80) {
					mapDataModel.setIcon("./assets/images/pushpins/PHC_green.png");
				} else if (dataValue >= 60 && dataValue < 80) {
					mapDataModel.setIcon("./assets/images/pushpins/PHC_orange.png");
				} else if (dataValue >= 0 && dataValue < 60) {
					mapDataModel.setIcon("./assets/images/pushpins/PHC_red.png");
				}
				break;

			case Constants.HSC_LEVEL:// PHC
				if (dataValue >= 80) {
					mapDataModel.setIcon("./assets/images/pushpins/HSC_green.png");
				} else if (dataValue >= 60 && dataValue < 80) {
					mapDataModel.setIcon("./assets/images/pushpins/HSC_orange.png");
				} else if (dataValue >= 0 && dataValue < 60) {
					mapDataModel.setIcon("./assets/images/pushpins/HSC_red.png");
				}
				break;
				
//			case Constants.UHC_LEVEL:
//				if (dataValue >= 80) {
//					mapDataModel.setIcon("./assets/images/pushpins/PHC_green.png");
//				} else if (dataValue >= 60 && dataValue < 80) {
//					mapDataModel.setIcon("./assets/images/pushpins/PHC_orange.png");
//				} else if (dataValue >= 0 && dataValue < 60) {
//					mapDataModel.setIcon("./assets/images/pushpins/PHC_red.png");
//				}
//				break;
			default:
				break;
			}

			googleMapDataModels.add(mapDataModel);
		}
		return googleMapDataModels;
	}

	// Not used
	@Override
	@Transactional(readOnly = true)
	public List<ScoreModel> fetchLabelFromLastVisitData(Integer lastVisitDataId) throws Exception {

		List<Object[]> objects = lastVisitDataRepository.getByLastVisitData(lastVisitDataId);
		List<ScoreModel> facilityScoreMappingLabelModels = new ArrayList<>();
		for (Object[] obj : objects) {

			ScoreModel facilityScoreMappingLabelModel = new ScoreModel();

			if (obj[1] instanceof FacilityScore) {
				FacilityScore facilityScore = (FacilityScore) obj[1];
				facilityScoreMappingLabelModel
						.setScore(null != facilityScore.getScore() ? facilityScore.getScore() : null);
			}
			if (obj[2] instanceof FormXpathScoreMapping) {
				FormXpathScoreMapping formXpathScoreMapping = (FormXpathScoreMapping) obj[2];
				facilityScoreMappingLabelModel.setFormXpathScoreId(formXpathScoreMapping.getFormXpathScoreId());
				facilityScoreMappingLabelModel.setMaxScore(
						null != formXpathScoreMapping.getMaxScore() ? formXpathScoreMapping.getMaxScore() : null);
				facilityScoreMappingLabelModel.setName(
						null != formXpathScoreMapping.getLabel() && !formXpathScoreMapping.getLabel().equals("")
								? formXpathScoreMapping.getLabel()
								: null);
				facilityScoreMappingLabelModel.setParentXpathScoreId(formXpathScoreMapping.getParentXpathId());
			}
			if (obj[3] instanceof Double) {
				Double percentScore = (Double) obj[3];
				facilityScoreMappingLabelModel.setPercentScore(percentScore != null && percentScore == 0.0 ? "0.0"
						: percentScore != null ? df.format(percentScore) : "NA");
			}
			// facilityScoreMappingLabelModel.setLastVisitDataId(lastVisitDataId);
			facilityScoreMappingLabelModels.add(facilityScoreMappingLabelModel);

		}
		return facilityScoreMappingLabelModels;
	}

	// Not used
	@Override
	@Transactional(readOnly = true)
	public Map<String, List<ScoreModel>> getGridTableData(Integer formId, Integer lastVisitDataId, int timeperiodId)
			throws Exception {

		Map<String, List<ScoreModel>> headerScoreMap = new HashMap<String, List<ScoreModel>>();
		List<ScoreModel> scoreModels = new ArrayList<ScoreModel>();

		scoreModels = lastVisitDataId != 0 ? fetchLabelFromLastVisitData(lastVisitDataId)
				: getAllAggregatedData(formId);

		headerScoreMap.put("Score (%)", scoreModels);

		return headerScoreMap;
	}

	@Override
	@Transactional(readOnly = true)
	public SpiderDataCollection getfetchSpiderData(Integer formId, Integer lastVisitDataId, Integer areaId,
			Integer parenXpathId, int formMetaId) {
		// getting user details from the state manager
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		Integer areaLevelId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaLevelId();
		Integer parentAreaId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getParentAreaId();

		XForm xform = xFormRepository.findByFormId(formId);

		List<Object[]> maxMinTimePeriodId = new ArrayList<Object[]>();
		// If in google map data if pushpin is not clicked then lastVisit Data will be 0
		if (lastVisitDataId == 0) {
			// if areaID=0 the we will check if logged in user is of district Level
			if (areaLevelId > 3 && parentAreaId != -1) {
				areaId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
						.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaId();

				if (xform.getAreaLevel().getAreaLevelId() == Constants.DH_LEVEL) {
					//maxMinTimePeriodId = lastVisitDataRepository.findMaxMinTimePeriodIdForADistrict(areaId, formMetaId);
					maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForADistrict(areaId, formMetaId);
					if(maxMinTimePeriodId.get(0).toString().equals("2") && maxMinTimePeriodId.get(2).toString().equals("2")) {
						maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForADistrictPHCCHCForUhcAndHwc(areaId,
								formMetaId);
					}
					
				} else {
//					maxMinTimePeriodId = lastVisitDataRepository.findMaxMinTimePeriodIdForADistrictPHCCHC(areaId,
//							formMetaId);
					maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForADistrictPHCCHC(areaId,
							formMetaId);
				}
			}
			// if district filter is done then areaId will contain the Id of that
			// district.In case of State select Id will be 0
			else if (areaId != 0) {
				if (xform.getAreaLevel().getAreaLevelId() == Constants.DH_LEVEL) {
//					maxMinTimePeriodId = lastVisitDataRepository.findMaxMinTimePeriodIdForADistrict(areaId, formMetaId);
					
					maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForADistrict(areaId, formMetaId);
					if(maxMinTimePeriodId.get(0)[0].toString().equals("3") && maxMinTimePeriodId.get(0)[2].toString().equals("3")) {
						maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForADistrictv3(areaId,
								formMetaId);
					}
					
					
				} else {
//					maxMinTimePeriodId = lastVisitDataRepository.findMaxMinTimePeriodIdForADistrictPHCCHC(areaId,
//							formMetaId);
					if(formMetaId == 6 || formMetaId == 7 || formMetaId == 8 || formMetaId == 9 || formMetaId == 10||formMetaId == 5) {
						maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForADistrictPHCCHCForUhcAndHwc(areaId,
								formMetaId);
					}else {
					
					maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForADistrictPHCCHC(areaId,
							formMetaId);
					
					if(maxMinTimePeriodId.get(0)[0].toString().equals("3") && maxMinTimePeriodId.get(0)[2].toString().equals("3")) {
						maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForADistrictPHCCHCForUhcAndHwc(areaId,
								formMetaId);
					}
					}
				}
			}
			// for the state level or national Level User
			else {
				//this to show 3 timePeriod data except for HWC
				if(formMetaId == 6 || formMetaId == 7 || formMetaId == 8 || formMetaId == 9 || formMetaId == 10 ||formMetaId == 5) {
					maxMinTimePeriodId = lastVisitDataRepository.findMaxMinTimePeriodIdForHwcState(formMetaId,
							xform.getState().getAreaId());
				}
				else {
//				maxMinTimePeriodId = lastVisitDataRepository.findMaxMinTimePeriodIdForState(formMetaId,
//						xform.getState().getAreaId());
					maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForState(formMetaId,
							xform.getState().getAreaId());
					if(maxMinTimePeriodId.get(0)[0].toString().equals("3") && maxMinTimePeriodId.get(0)[2].toString().equals("3")) {
						maxMinTimePeriodId = lastVisitDataRepository.findMaxMinTimePeriodIdForHwcState(formMetaId,
								xform.getState().getAreaId());
					}
				}
			}
		}
		// if pushpin clicked
		else {
			if(formMetaId == 6 || formMetaId == 7 || formMetaId == 8 || formMetaId == 9 || formMetaId == 10 || formMetaId == 5) {
				maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForAFacilityOfUhc(lastVisitDataId);
			}
			else {
//			maxMinTimePeriodId = lastVisitDataRepository.findMaxMinTimePeriodIdForAFacility(lastVisitDataId);
			maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForAFacility(lastVisitDataId);
			if(maxMinTimePeriodId.get(0)[0].toString().equals("3") && maxMinTimePeriodId.get(0)[2].toString().equals("3")) {
				maxMinTimePeriodId = lastVisitDataRepository.findAllTimePeriodIdForAFacilityOfUhc(lastVisitDataId);
			}
			}
		}

		// setting max and min timeperiod for a area

		List<Integer> maxMinTime = new ArrayList<Integer>();
		if (maxMinTimePeriodId.size() > 0) {
			if (maxMinTimePeriodId.get(0)[0] != null) {
				maxMinTime.add(Integer.parseInt(maxMinTimePeriodId.get(0)[0].toString()));
			}

			if (maxMinTimePeriodId.get(0)[1] != null
					&& !maxMinTime.contains(Integer.parseInt(maxMinTimePeriodId.get(0)[1].toString()))) {
				maxMinTime.add(Integer.parseInt(maxMinTimePeriodId.get(0)[1].toString()));
			}
			
			//added new line to show data of 3 timePeriod
			if(formMetaId != 6 && formMetaId != 7 && formMetaId != 8 && formMetaId != 9 && formMetaId != 10 && formMetaId != 5) {
			if(maxMinTimePeriodId.get(0).length>2){
				if (maxMinTimePeriodId.get(0)[2] != null
					&& !maxMinTime.contains(Integer.parseInt(maxMinTimePeriodId.get(0)[2].toString()))) {
				maxMinTime.add(Integer.parseInt(maxMinTimePeriodId.get(0)[2].toString()));
			}
				}
			}
			Collections.reverse(maxMinTime);
		}
		

		
		List<List<SpiderDataModel>> spiderDataModelsLists = new ArrayList<List<SpiderDataModel>>();
		SpiderDataCollection spiderDataCollection = new SpiderDataCollection();
		List<Map<String, String>> gridData = new ArrayList<Map<String, String>>();
		Map<String, Map<String, String>> tableData = new LinkedHashMap<String, Map<String, String>>();
		for (int timeperiodId : maxMinTime) {

			List<SpiderDataModel> spiderDataModels = new ArrayList<SpiderDataModel>();

			List<Object[]> spiderDatas = new ArrayList<Object[]>();
			;


			// If in google map data if pushpin is not clicked then lastVisit Data will be 0
			if (lastVisitDataId == 0) {

				// if areaID=0 the we will check if logged in user is of district Level
				if (areaLevelId > 3 && parentAreaId != -1) {
					areaId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
							.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaId();

					// checking form is of DH
					if (xform.getAreaLevel().getAreaLevelId() == Constants.DH_LEVEL) {
						spiderDatas = facilityScoreRepository.findSpiderDataChartByFormIdForDistrictForDGA(formMetaId,
								-1, areaId, timeperiodId);
						if(!spiderDatas.isEmpty())
						spiderDatas.addAll(facilityScoreRepository.findSpiderDataChartByFormIdForDistrictForDGA(
								formMetaId, Integer.parseInt(spiderDatas.get(0)[3].toString()), areaId, timeperiodId));
					}
					// if form is of CHC,DH
					else {
						spiderDatas = facilityScoreRepository.findSpiderDataChartByFormIdForDistrict(formMetaId, -1,
								areaId, timeperiodId);
						if(!spiderDatas.isEmpty())
						spiderDatas.addAll(facilityScoreRepository.findSpiderDataChartByFormIdForDistrict(formMetaId,
								Integer.parseInt(spiderDatas.get(0)[3].toString()), areaId, timeperiodId));
					}

				}
				// if district filter is done then areaId will contain the Id of that
				// district.In case of State select Id will be 0
				else if (areaId != 0) {

					// if formaID is of DH
					if (xform.getAreaLevel().getAreaLevelId() == Constants.DH_LEVEL) {
						spiderDatas = facilityScoreRepository.findSpiderDataChartByFormIdForDistrictForDGA(formMetaId,
								-1, areaId, timeperiodId);

						if(!spiderDatas.isEmpty())
						spiderDatas.addAll(facilityScoreRepository.findSpiderDataChartByFormIdForDistrictForDGA(
								formMetaId, Integer.parseInt(spiderDatas.get(0)[3].toString()), areaId, timeperiodId));
					}
					// if formId is of PHC,CHC
					else {
						spiderDatas = facilityScoreRepository.findSpiderDataChartByFormIdForDistrict(formMetaId, -1,
								areaId, timeperiodId);
						if(!spiderDatas.isEmpty())
						spiderDatas.addAll(facilityScoreRepository.findSpiderDataChartByFormIdForDistrict(formMetaId,
								Integer.parseInt(spiderDatas.get(0)[3].toString()), areaId, timeperiodId));
					}

				}

				// for the state level or national Level User
				else {
					spiderDatas = facilityScoreRepository.findSpiderDataChartByFormId(formMetaId, -1, timeperiodId);

					if(!spiderDatas.isEmpty())
					spiderDatas.addAll(facilityScoreRepository.findSpiderDataChartByFormId(formMetaId,
							Integer.parseInt(spiderDatas.get(0)[3].toString()), timeperiodId));
				}

			}
			// if on google map pushpin is clicked
			else {
				spiderDatas = facilityScoreRepository.findSpiderDataChartByLasatVisitDatAndFormIdAndTimePeriodId(
						formMetaId, lastVisitDataId, -1, timeperiodId);
				if(!spiderDatas.isEmpty())
				spiderDatas.addAll(facilityScoreRepository.findSpiderDataChartByLasatVisitDatAndFormIdAndTimePeriodId(
						formMetaId, lastVisitDataId, Integer.parseInt(spiderDatas.get(0)[3].toString()), timeperiodId));
			}
			for (Object[] spiderData : spiderDatas) {

				SpiderDataModel spiderDataModel = new SpiderDataModel();

				spiderDataModel.setAxis(
						spiderData[0].toString().contains("Total score") ? "Overall Score" : spiderData[0].toString());
				spiderDataModel.setValue(spiderData[1] == null || spiderData[1].equals("0.00") ? "0.0"
						: spiderData[1].toString().equalsIgnoreCase("0.0") ? "0.0" : df.format((Double) spiderData[1]));

				spiderDataModel.setTimePeriod(spiderData[2].toString());
				Map<String, String> mapData = new LinkedHashMap<String, String>();

				// putting the data for tables.Setting the value in this format
				// <IndicatorName,<TimeperiodName,Value>>
				if (!tableData.containsKey(spiderDataModel.getAxis())) {
					mapData.put(spiderDataModel.getTimePeriod(), spiderDataModel.getValue());
					tableData.put(spiderDataModel.getAxis(), mapData);
				} else {
					tableData.get(spiderDataModel.getAxis()).put(spiderDataModel.getTimePeriod(),
							spiderDataModel.getValue());
				}

				spiderDataModels.add(spiderDataModel);
			}
			if (spiderDataModels.size() > 0)
				spiderDataModelsLists.add(spiderDataModels);
		}
		spiderDataCollection.setDataCollection(spiderDataModelsLists);

		tableData.forEach((k, v) -> {
			Map<String, String> gridMapTableData = new LinkedHashMap<String, String>();

			// setting the table data in following format
			// <Section/Sub-Section,IndicatorName>,<Timeperiod,Value>
			gridMapTableData.put("Section/Sub-Section", k);
			v.forEach((i, j) -> {
				gridMapTableData.put(i, j);
			});
			gridData.add(gridMapTableData);

		});
		spiderDataCollection.setTableData(gridData);
		return spiderDataCollection;
	}

	@Override
	@Transactional(readOnly = true)
	public List<FormXpathScoreMappingModel> getParentSectors(int timeperiodId, int stateId,int programId) {
		List<FormXpathScoreMapping> formXpathScoreMappings = formXpathScoreMappingRepository
				.findByParentXpathIdAndFormTimePeriodTimePeriodIdAndFormStateAreaIdAndFormProgramXFormMappingProgramProgramIdOrderByFormAreaLevelAreaLevelId(-1, timeperiodId, stateId,programId);
		/* formXpathScoreMapping */
		List<FormXpathScoreMappingModel> formXpathScoreMappingModels = new ArrayList<FormXpathScoreMappingModel>();

		// Getting the parent Sectors
		for (FormXpathScoreMapping formXpathScoreMapping : formXpathScoreMappings) {
			FormXpathScoreMappingModel formXpathScoreMappingModel = new FormXpathScoreMappingModel();
			formXpathScoreMappingModel.setFormXpathScoreId(formXpathScoreMapping.getFormXpathScoreId());
			// Slicing the name as DB Consist the name as Total SCore OF DH for every Sector
			formXpathScoreMappingModel.setLabel(formXpathScoreMapping.getLabel().split("Total score for")[1]);
			formXpathScoreMappingModel.setFormId(formXpathScoreMapping.getForm().getFormId());
			formXpathScoreMappingModel.setMarkerClass(formXpathScoreMapping.getForm().getMarkerClass());
			formXpathScoreMappingModel.setForm_meta_id(formXpathScoreMapping.getForm().getXform_meta_id());
			formXpathScoreMappingModels.add(formXpathScoreMappingModel);
		}
		return formXpathScoreMappingModels;
	}

	@Override
	@Transactional(readOnly = true)
	public List<FormXpathScoreMappingModel> getSectors(Integer parentId) {
		List<FormXpathScoreMapping> formXpathScoreMappings = formXpathScoreMappingRepository
				.findByParentXpathId(parentId);
		/* formXpathScoreMapping */
		List<FormXpathScoreMappingModel> formXpathScoreMappingModels = new ArrayList<FormXpathScoreMappingModel>();

		FormXpathScoreMappingModel formXpathScoreMappingModel = new FormXpathScoreMappingModel();
		formXpathScoreMappingModel.setFormXpathScoreId(parentId);
		formXpathScoreMappingModel.setLabel("Overall Score");
		formXpathScoreMappingModel.setFormId(0);
		formXpathScoreMappingModels.add(formXpathScoreMappingModel);

		for (FormXpathScoreMapping formXpathScoreMapping : formXpathScoreMappings) {
			formXpathScoreMappingModel = new FormXpathScoreMappingModel();
			formXpathScoreMappingModel.setFormXpathScoreId(formXpathScoreMapping.getFormXpathScoreId());
			formXpathScoreMappingModel.setLabel(formXpathScoreMapping.getLabel());
			formXpathScoreMappingModel.setFormId(formXpathScoreMapping.getForm().getFormId());
			formXpathScoreMappingModel.setParentXpathId(formXpathScoreMapping.getParentXpathId());
			formXpathScoreMappingModel.setMaxScore(formXpathScoreMapping.getMaxScore());
			formXpathScoreMappingModels.add(formXpathScoreMappingModel);
		}
		return formXpathScoreMappingModels;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AreaModel> getAllDistricts(int stateId) {
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
			return districtModels;
		}
		// else if state user setting stateid from session
		else if (areaLevelId == 2) {
			stateId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
					.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaId();
		}
// if of national or guest or admin type then all the areas
		List<Area> districts = areaRepository.findByParentAreaIdAndAreaLevelAreaLevelIdOrderByAreaNameAsc(stateId, 4);
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
			areaModel.setAreaCode(district.getAreaCode());

			districtModels.add(areaModel);
		}
		return districtModels;
	}

	@SuppressWarnings("resource")
	@Override
	@Transactional(readOnly = true)
	public String exportToPdf(String spiderChart, String columnChart, Integer formId, Integer lastVisitDataId,
			Integer areaId, HttpServletResponse response, int noOfFacilities, int timeperiodId, Integer parentXpathId,
			int formMetaId, HttpServletRequest request) throws Exception {

		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();

//		String ctxPath = request.getContextPath();

		url = url.replaceFirst(uri, "");

//		url+=ctxPath;

		SpiderDataCollection spiderDataCollection = getfetchSpiderData(formId, lastVisitDataId, areaId, parentXpathId,
				formMetaId);

		new FileOutputStream(new File(context.getRealPath("") + "\\resources\\spider.svg"))
				.write(spiderChart.getBytes());

		String area, sectorName;

		if (lastVisitDataId != 0) {
			noOfFacilities = 1;
		}
		// getting sector Name
		FormXpathScoreMapping fxm = formXpathScoreMappingRepository.findByFormXpathScoreId(parentXpathId);
		sectorName = fxm.getLabel().split("Total score for")[1];

		// If pushpin is clicked the we will set FaciliTy name in area
		if (lastVisitDataId != 0) {
			area = lastVisitDataRepository.findByLastVisitDataIdAndIsLiveTrue(lastVisitDataId).getArea().getAreaName();
		}
		// for the district filter
		else if (areaId != 0) {
			area = areaRepository.findByAreaId(areaId).getAreaName();
		} else
			area = fxm.getForm().getState().getAreaName();

		Font smallBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		Font dataFont = new Font(Font.FontFamily.HELVETICA, 10);

		Document document = new Document(PageSize.A4.rotate());
		String outputPath = messages.getMessage("outputPath", null, null) + area + "_" + sectorName + "_Score_Card_"
				+ sdf.format(new java.util.Date()) + ".pdf";
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

		// setting Header Footer.PLS Refer to org.sdrc.dga.util.HeaderFooter
		HeaderFooter headerFooter = new HeaderFooter(url);
		writer.setPageEvent(headerFooter);

		document.open();
		document.addAuthor("dgaindia.org");

//			BaseColor cellColor = WebColors.getRGBColor("#E8E3E2");

		Paragraph dashboardTitle = new Paragraph();
		dashboardTitle.setAlignment(Element.ALIGN_CENTER);
		dashboardTitle.setSpacingAfter(10);
		Chunk dashboardChunk = new Chunk("Score Card");
		dashboardTitle.add(dashboardChunk);

		Paragraph blankSpace = new Paragraph();
		blankSpace.setAlignment(Element.ALIGN_CENTER);
		blankSpace.setSpacingAfter(10);
		Chunk blankSpaceChunk = new Chunk("          ");
		blankSpace.add(blankSpaceChunk);

		Paragraph numberOfFacility = new Paragraph();
		numberOfFacility.setAlignment(Element.ALIGN_CENTER);
		Chunk numberOfFacilityChunk = new Chunk("N = " + noOfFacilities);
		numberOfFacility.add(numberOfFacilityChunk);

		Paragraph spiderDataParagraph = new Paragraph();
		spiderDataParagraph.setAlignment(Element.ALIGN_CENTER);
		spiderDataParagraph.setSpacingAfter(10);
		Chunk spiderChunk = new Chunk(
				"Area / Facility: " + area + "\t  \t  Facility Level: " + sectorName + "\n \n N = " + noOfFacilities);
		spiderDataParagraph.add(spiderChunk);

		// for Image

		String css = "svg {" + "shape-rendering: geometricPrecision;" + "text-rendering:  geometricPrecision;"
				+ "color-rendering: optimizeQuality;" + "image-rendering: optimizeQuality;" + "}";
		File cssFile = File.createTempFile("batik-default-override-", ".css");
		FileUtils.writeStringToFile(cssFile, css);

		String svg_URI_input = Paths.get(new File(context.getRealPath("") + "\\resources\\spider.svg").getPath())
				.toUri().toURL().toString();
		TranscoderInput input_svg_image = new TranscoderInput(svg_URI_input);
		// Step-2: Define OutputStream to PNG Image and attach to
		// TranscoderOutput
		ByteArrayOutputStream png_ostream = new ByteArrayOutputStream();
		TranscoderOutput output_png_image = new TranscoderOutput(png_ostream);
		// Step-3: Create PNGTranscoder and define hints if required
		PNGTranscoder my_converter = new PNGTranscoder();
		// Step-4: Convert and Write output
		my_converter.transcode(input_svg_image, output_png_image);
		png_ostream.flush();

		Image spiderDataImage = Image.getInstance(png_ostream.toByteArray());
//			int indentation1 = 0;
		/*
		 * float scaler1 = ((document.getPageSize().getWidth() - document.leftMargin() -
		 * document.rightMargin() - indentation1) / spiderDataImage .getWidth()) * 62;
		 */
		spiderDataImage.scalePercent(75);
		spiderDataImage.setAbsolutePosition(170, -1);

		BaseColor siNoColor = WebColors.getRGBColor("#f4f4f4");
		BaseColor redColor = WebColors.getRGBColor("#D7191C");
		BaseColor orangeColor = WebColors.getRGBColor("#FF8000");
		BaseColor greenColor = WebColors.getRGBColor("#1A9642");
		BaseColor noDataColor = WebColors.getRGBColor("#949292");
		BaseColor borderColor = WebColors.getRGBColor("#ddd");
		PdfPTable spiderDataTable = null;
		if (spiderDataCollection.getDataCollection().size() > 1) {
			
			 if(spiderDataCollection.getDataCollection().size() > 2) {
					spiderDataTable = new PdfPTable(4);
				}else {
					spiderDataTable = new PdfPTable(3);
				}
		}
		else {
			spiderDataTable = new PdfPTable(2);
		}

		// Spider Datas Table
		float[] spiderDatacolumnWidths;
		if (spiderDataCollection.getDataCollection().size() > 1) {
			
			if(spiderDataCollection.getDataCollection().size() > 2) {
				spiderDatacolumnWidths = new float[] { 25f, 4f, 4f,4f };
			}else {
				spiderDatacolumnWidths = new float[] { 25f, 4f, 4f };
			}

		} 
		else {
			spiderDatacolumnWidths = new float[] { 25f, 4f };
		}
		spiderDataTable.setHeaderRows(1);
		spiderDataTable.setWidths(spiderDatacolumnWidths);

		PdfPCell spiderDataCell1 = new PdfPCell(new Paragraph("Section/Sub-Section", smallBold));
		PdfPCell spiderDataCell3 = new PdfPCell(
				new Paragraph(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString(), smallBold));

		spiderDataCell1.setBackgroundColor(siNoColor);
		spiderDataCell3.setBackgroundColor(siNoColor);
		spiderDataCell3.setBorderColor(borderColor);

		spiderDataCell1.setBorderColor(borderColor);

		spiderDataCell1.setHorizontalAlignment(Element.ALIGN_LEFT);

		spiderDataCell3.setHorizontalAlignment(Element.ALIGN_CENTER);

		spiderDataTable.addCell(spiderDataCell1);
		spiderDataTable.addCell(spiderDataCell3);
		if (spiderDataCollection.getDataCollection().size() > 1) {
			PdfPCell spiderDataCell4 = new PdfPCell(new Paragraph(new Paragraph(
					spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString(), smallBold)));
			spiderDataCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			spiderDataCell4.setBackgroundColor(siNoColor);
			spiderDataCell4.setBorderColor(borderColor);
			spiderDataTable.addCell(spiderDataCell4);
		}
		if (spiderDataCollection.getDataCollection().size() > 2) {
			PdfPCell spiderDataCell40 = new PdfPCell(new Paragraph(new Paragraph(
					spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString(), smallBold)));
			spiderDataCell40.setHorizontalAlignment(Element.ALIGN_CENTER);
			spiderDataCell40.setBackgroundColor(siNoColor);
			spiderDataCell40.setBorderColor(borderColor);
			spiderDataTable.addCell(spiderDataCell40);
		}

		if (spiderDataCollection.getDataCollection().size() > 0
				&& spiderDataCollection.getDataCollection().get(0) != null
				&& !spiderDataCollection.getDataCollection().get(0).isEmpty()) {
			List<Map<String, String>> tableDatas = spiderDataCollection.getTableData();
			for (Map<String, String> spiderDataModel : tableDatas) {

				PdfPCell data1 = new PdfPCell(new Paragraph(spiderDataModel.get("Section/Sub-Section"), dataFont));
				data1.setFixedHeight(spiderDataCell3.getHeight());
				data1.setHorizontalAlignment(Element.ALIGN_LEFT);

				PdfPCell data3 = new PdfPCell(new Paragraph(spiderDataModel
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString()) == null
								? ""
								: spiderDataModel.get(
										spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString()),
						dataFont));
				data3.setHorizontalAlignment(Element.ALIGN_CENTER);
				data3.setFixedHeight(spiderDataCell3.getHeight());

				data1.setBorderColor(borderColor);
				data3.setBorderColor(borderColor);
				data1.setBackgroundColor(siNoColor);

				if (spiderDataModel
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString()) == null) {
					data3.setBackgroundColor(noDataColor);
				} else if (Double.parseDouble(spiderDataModel
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString())) < 60) {
					data3.setBackgroundColor(redColor);
				}

				else if (Double.parseDouble(spiderDataModel
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString())) < 80) {
					data3.setBackgroundColor(orangeColor);
				} else {
					data3.setBackgroundColor(greenColor);
				}

				spiderDataTable.addCell(data1);
				spiderDataTable.addCell(data3);
				if (spiderDataCollection.getDataCollection().size() > 1) {

					PdfPCell data4 = new PdfPCell(
							new Paragraph(
									spiderDataModel
											.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[2]
													.toString()) == null
															? ""
															: spiderDataModel.get(spiderDataCollection.getTableData()
																	.get(0).keySet().toArray()[2].toString()),
									dataFont));
					data4.setFixedHeight(spiderDataCell3.getHeight());
					data4.setBorderColor(borderColor);
					data4.setHorizontalAlignment(Element.ALIGN_CENTER);

					if (spiderDataModel
							.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString()) == null) {
						data4.setBackgroundColor(noDataColor);
					} else if (Double.parseDouble(spiderDataModel
							.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString())) < 60) {
						data4.setBackgroundColor(redColor);
					}

					else if (Double.parseDouble(spiderDataModel
							.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString())) < 80) {
						data4.setBackgroundColor(orangeColor);
					} else {
						data4.setBackgroundColor(greenColor);
					}

					spiderDataTable.addCell(data4);

				}
				if (spiderDataCollection.getDataCollection().size() > 2) {

					PdfPCell data4 = new PdfPCell(
							new Paragraph(
									spiderDataModel
											.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[3]
													.toString()) == null
															? ""
															: spiderDataModel.get(spiderDataCollection.getTableData()
																	.get(0).keySet().toArray()[3].toString()),
									dataFont));
					data4.setFixedHeight(spiderDataCell3.getHeight());
					data4.setBorderColor(borderColor);
					data4.setHorizontalAlignment(Element.ALIGN_CENTER);

					if (spiderDataModel
							.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString()) == null) {
						data4.setBackgroundColor(noDataColor);
					} else if (Double.parseDouble(spiderDataModel
							.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString())) < 60) {
						data4.setBackgroundColor(redColor);
					}

					else if (Double.parseDouble(spiderDataModel
							.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString())) < 80) {
						data4.setBackgroundColor(orangeColor);
					} else {
						data4.setBackgroundColor(greenColor);
					}

					spiderDataTable.addCell(data4);

				}

			}

		}

		document.add(blankSpace);
		document.add(dashboardTitle);
		document.add(spiderDataParagraph);

		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);

		document.add(spiderDataImage);

		document.newPage();

		document.add(spiderDataTable);

		document.close();

		return outputPath;

	}

	@SuppressWarnings({ "resource" })
	@Override
	@Transactional(readOnly = true)
	public String exportToExcel(String spiderChart, String columnChart, Integer formId, Integer lastVisitDataId,
			Integer areaId, HttpServletResponse response, int noOfFacilities, int timeperiodId, Integer parentXpathId,
			int formMetaId, HttpServletRequest request) throws Exception {
		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();

		String ctxPath = request.getContextPath();

		url = url.replaceFirst(uri, "");

		url += ctxPath;

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Score Card");
		CreationHelper creationHelper = workbook.getCreationHelper();
		XSSFHyperlink link = (XSSFHyperlink) creationHelper.createHyperlink(Hyperlink.LINK_URL);
		link.setAddress(url);
		link.setTooltip(url);
		link.setLabel(url);
		POIXMLProperties xmlProps = workbook.getProperties();
		POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
		coreProps.setCreator(url);

		new FileOutputStream(new File(context.getRealPath("") + "\\resources\\spider.svg"))
				.write(spiderChart.getBytes());

		new FileOutputStream(new File(context.getRealPath("") + "\\resources\\column.svg"))
				.write(columnChart.getBytes());
		if (lastVisitDataId != 0) {
			noOfFacilities = 1;
		}

		SpiderDataCollection spiderDataCollection = getfetchSpiderData(formId, lastVisitDataId, areaId, parentXpathId,
				formMetaId);

		String area, sectorName;
		FormXpathScoreMapping fxm = formXpathScoreMappingRepository.findByFormXpathScoreId(parentXpathId);
		sectorName = fxm.getLabel().split("Total score for")[1];
		if (lastVisitDataId != 0) {
			area = lastVisitDataRepository.findByLastVisitDataIdAndIsLiveTrue(lastVisitDataId).getArea().getAreaName();
		} else if (areaId != 0) {
			area = areaRepository.findByAreaId(areaId).getAreaName();
		} else
			area = fxm.getForm().getState().getAreaName();

		int rowId = 0;
		int colId = 0;
		Row row = sheet.createRow(rowId);
		Cell col = row.createCell(colId);
		XSSFCellStyle headCellStyle = workbook.createCellStyle();

		XSSFFont headFont = workbook.createFont();
		headFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headFont.setColor(HSSFColor.BLACK.index);
		headFont.setFontHeight(18);

		headCellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		headCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		headCellStyle.setFont(headFont);
		headCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		col.setCellValue("Area / Facility: " + area + "\t  \t  Facility Level: " + sectorName);
		col.setHyperlink(link);
		col.setCellStyle(headCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(rowId, rowId, colId, 19));
		rowId = 3;

		row = sheet.createRow(rowId++);
		colId = 7;
		col = row.createCell(colId);
		XSSFCellStyle headerCellStyle = workbook.createCellStyle();

		XSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setColor(HSSFColor.BLACK.index);

		headerCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		headerCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		headerCellStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		headerCellStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		headerCellStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);

		XSSFCellStyle leftHeaderCellStyle = workbook.createCellStyle();
		leftHeaderCellStyle.setFont(headerFont);
		leftHeaderCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		leftHeaderCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		leftHeaderCellStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		leftHeaderCellStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		leftHeaderCellStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		leftHeaderCellStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		leftHeaderCellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);

		XSSFFont font = workbook.createFont();
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		font.setColor(HSSFColor.GREY_80_PERCENT.index);

		XSSFFont cellFont = workbook.createFont();
		cellFont.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		cellFont.setColor(HSSFColor.BLACK.index);

		XSSFCellStyle redCellStyle = workbook.createCellStyle();
		redCellStyle.setFont(cellFont);
		redCellStyle.setFillForegroundColor(HSSFColor.RED.index);
		redCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		redCellStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		redCellStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		redCellStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		redCellStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		redCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		XSSFCellStyle orangeCellStyle = workbook.createCellStyle();
		orangeCellStyle.setFont(cellFont);
		orangeCellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
		orangeCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		orangeCellStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		orangeCellStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		orangeCellStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		orangeCellStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		orangeCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		XSSFCellStyle greenCellStyle = workbook.createCellStyle();
		greenCellStyle.setFont(cellFont);
		greenCellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		greenCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		greenCellStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		greenCellStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		greenCellStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		greenCellStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		greenCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		XSSFCellStyle greyCellStyle = workbook.createCellStyle();
		greyCellStyle.setFont(cellFont);
		greyCellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		greyCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		greyCellStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		greyCellStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		greyCellStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		greyCellStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		greyCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		col.setCellValue("Section/Sub-Section");
		headerCellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		col.setCellStyle(leftHeaderCellStyle);
		sheet.autoSizeColumn(colId);
		colId++;

		col = row.createCell(colId);
		headerCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		col.setCellValue(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString());
		col.setCellStyle(headerCellStyle);
		sheet.autoSizeColumn(colId);
		colId++;
		if (spiderDataCollection.getDataCollection().size() > 1) {
			col = row.createCell(colId);
			sheet.autoSizeColumn(colId);

			sheet.autoSizeColumn(colId);
			headerCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			col.setCellValue(spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString());
			col.setCellStyle(headerCellStyle);
			sheet.autoSizeColumn(colId);
			colId++;
		}
		if (spiderDataCollection.getDataCollection().size() > 2) {
			col = row.createCell(colId);
			sheet.autoSizeColumn(colId);

			sheet.autoSizeColumn(colId);
			headerCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			col.setCellValue(spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString());
			col.setCellStyle(headerCellStyle);
			sheet.autoSizeColumn(colId);
			colId++;
		}

		List<Map<String, String>> tableDatas = spiderDataCollection.getTableData();

		for (Map<String, String> spiderData : tableDatas) {

			cellStyle.setFont(font);
			cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
			cellStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
			cellStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
			cellStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
			cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

			XSSFCellStyle leftCellStyle = workbook.createCellStyle();
			leftCellStyle.setFont(font);
			leftCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			leftCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			leftCellStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
			leftCellStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
			leftCellStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
			leftCellStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
			leftCellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);

			colId = 7;
			row = sheet.createRow(rowId++);
			col = row.createCell(colId);
			col.setCellValue(spiderData.get("Section/Sub-Section"));
			col.setCellStyle(leftCellStyle);

			sheet.autoSizeColumn(colId);
			colId++;
			col = row.createCell(colId);
			col.setCellValue(
					spiderData.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString()) == null
							? ""
							: spiderData
									.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString()));

			if (spiderData.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString()) == null)
				col.setCellStyle(greyCellStyle);

			else if (Double.parseDouble(
					spiderData.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString())) < 60)
				col.setCellStyle(redCellStyle);
			else if (Double.parseDouble(
					spiderData.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[1].toString())) < 80)
				col.setCellStyle(orangeCellStyle);
			else
				col.setCellStyle(greenCellStyle);

			sheet.autoSizeColumn(colId);
			colId++;

			if (spiderDataCollection.getDataCollection().size() > 1) {

				col = row.createCell(colId);
				col.setCellValue(spiderData
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString()) == null ? ""
								: spiderData.get(
										spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString()));

				if (spiderData.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString()) == null)
					col.setCellStyle(greyCellStyle);

				else if (Double.parseDouble(spiderData
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString())) < 60)
					col.setCellStyle(redCellStyle);
				else if (Double.parseDouble(spiderData
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[2].toString())) < 80)
					col.setCellStyle(orangeCellStyle);
				else
					col.setCellStyle(greenCellStyle);
				sheet.autoSizeColumn(colId);

			}
			sheet.autoSizeColumn(colId);
			colId++;
			
			if (spiderDataCollection.getDataCollection().size() > 2) {

				col = row.createCell(colId);
				col.setCellValue(spiderData
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString()) == null ? ""
								: spiderData.get(
										spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString()));

				if (spiderData.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString()) == null)
					col.setCellStyle(greyCellStyle);

				else if (Double.parseDouble(spiderData
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString())) < 60)
					col.setCellStyle(redCellStyle);
				else if (Double.parseDouble(spiderData
						.get(spiderDataCollection.getTableData().get(0).keySet().toArray()[3].toString())) < 80)
					col.setCellStyle(orangeCellStyle);
				else
					col.setCellStyle(greenCellStyle);
				sheet.autoSizeColumn(colId);

			}

		}
		row = sheet.createRow(1);

		col = row.createCell(7);

		XSSFCellStyle normalCellStyle = workbook.createCellStyle();

		normalCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		normalCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		normalCellStyle.setFont(font);
		normalCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		col.setCellValue("N = " + noOfFacilities);
		col.setCellStyle(normalCellStyle);
		FileInputStream fileInputStream;

		String svg_URI_input = Paths.get(new File(context.getRealPath("") + "\\resources\\spider.svg").getPath())
				.toUri().toURL().toString();
		String path = createImgFromFile(svg_URI_input);

		fileInputStream = new FileInputStream(path);
		byte[] imageBytes = IOUtils.toByteArray(fileInputStream);
		int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_JPEG);

		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();

		anchor.setCol1(0);
		anchor.setRow1(3);
		anchor.setCol2(4);
		anchor.setRow2(12);
		Picture pict = drawing.createPicture(anchor, pictureureIdx);
		pict.resize(1.65);

		String outputPath = messages.getMessage("outputPath", null, null) + area + "_" + sectorName + "_Score_Card_"
				+ sdf.format(new java.util.Date()) + ".xlsx";
		FileOutputStream fileOut = null;
		fileOut = new FileOutputStream(outputPath);
		workbook.write(fileOut);
		fileOut.close();
		// End of image
		return outputPath;
	}

	public String createImgFromFile(String path) throws Exception {

		JPEGTranscoder t = new JPEGTranscoder();

		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));

		TranscoderInput input = new TranscoderInput(path);

		String fileName = messages.getMessage("output.filepath", null, null);
		OutputStream ostream = new FileOutputStream(fileName + "/CHART_" + ".jpg");
		TranscoderOutput output = new TranscoderOutput(ostream);

		t.transcode(input, output);

		ostream.flush();
		ostream.close();

		return fileName + "/CHART_" + ".jpg";
	}

	@Override
	@Transactional(readOnly = true)
	public FacilityPlanningModel getPlannedFacilities(int formId, int areaId, int timePeriodId, int stateId) {

		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);

		// checking if user is of District Level
		if (areaId == 0
				&& collectUserModel.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionSchemeModel()
						.getAreaModel().getAreaLevelId() > 3
				&& collectUserModel.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionSchemeModel()
						.getAreaModel().getParentAreaId() != -1) {
			areaId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
					.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaId();
		}
// if areaId is 0 and user is national or state type the areaId will set as areaId of State
		else if (areaId == 0) {
			areaId = stateId;
		}

		// facilities planned for a selected area and form type
		FacilityPlanning facilityPlanning = facilityPlanningRepository
				.findByAreaAreaIdAndXFormFormIdAndTimPeriodTimePeriodId(areaId, formId, timePeriodId);
		FacilityPlanningModel facilityPlanningModel = new FacilityPlanningModel();

		if (facilityPlanning != null) {
			facilityPlanningModel.setXformId(facilityPlanning.getxForm().getFormId());
			facilityPlanningModel.setStartDate(facilityPlanning.getTimPeriod().getStartDate());
			facilityPlanningModel.setEndDate(facilityPlanning.getTimPeriod().getEndDate());
			facilityPlanningModel.setFacilityPlanned(facilityPlanning.getFacilityPlanned());
			AreaModel areaModel = new AreaModel();

			areaModel.setAreaId(facilityPlanning.getArea().getAreaId());
			areaModel.setAreaName(facilityPlanning.getArea().getAreaName());
			areaModel.setParentAreaId(facilityPlanning.getArea().getParentAreaId());

			facilityPlanningModel.setAreaModel(areaModel);
		}
		return facilityPlanningModel;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TimePeriodModel> getAllPlanningTimePeriod(int stateId,int programId) {
		List<TimePeriodModel> timePeriodModels = new ArrayList<TimePeriodModel>();
		List<TimePeriod> timePeriods = xFormRepository.findDistinctTimPeriodByStateAreaIdAndProgramId(stateId,programId);
		timePeriods.sort(new Comparator<TimePeriod>() {

			@Override
			public int compare(TimePeriod o1, TimePeriod o2) {
				return o2.getTimePeriodId()-o1.getTimePeriodId();
			}
		});
		for (TimePeriod timeperiod : timePeriods) {
			TimePeriodModel periodModel = new TimePeriodModel();
			periodModel.setTimePeriod(timeperiod.getTimeperiod());
			periodModel.setTimePeriod_Nid(timeperiod.getTimePeriodId());

			timePeriodModels.add(periodModel);
		}

		return timePeriodModels;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AreaModel> getAllState() {
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		Integer areaLevelId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getAreaLevelId();
		Integer parentAreaId = collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getAreaModel().getParentAreaId();

		// if Logged in user of District type then only its own state will be
		// displayed
		List<AreaModel> stateModels = new ArrayList<AreaModel>();
		if (areaLevelId > 3 && parentAreaId != -1) {
			stateModels.add(domainToModelConverter
					.toAreaModel(areaRepository.findByAreaId(collectUserModel.getUserRoleFeaturePermissionMappings()
							.get(0).getRoleFeaturePermissionSchemeModel().getAreaModel().getParentAreaId())));
			return stateModels;
		}
		// else if state user setting logged in state
		else if (areaLevelId == 2) {
			stateModels.add(collectUserModel.getUserRoleFeaturePermissionMappings().get(0)
					.getRoleFeaturePermissionSchemeModel().getAreaModel());
			return stateModels;

		}
// if of national or guest or admin type then all the areas
//		List<Area> states = areaRepository.findByAreaLevelAreaLevelIdOrderByAreaNameAsc(2);
		List<Area> states = areaRepository.findByAreaLevelAreaLevelIdAndAreaIdNotOrderByAreaNameAsc(2,1921);

		AreaModel areaModel = new AreaModel();

		for (Area district : states) {
			areaModel = new AreaModel();
			areaModel.setAreaId(district.getAreaId());
			areaModel.setAreaName(district.getAreaName());
			areaModel.setAreaLevelId(district.getAreaLevel().getAreaLevelId());
			areaModel.setParentAreaId(district.getParentAreaId());
			areaModel.setAreaCode(district.getAreaCode());

			stateModels.add(areaModel);
		}
		return stateModels;
	}

	@Override
	public List<ProgramModel> getAllProgramme(int stateId) {
		List<ProgramModel> programModels = new ArrayList<ProgramModel>();
		
		List<Program> programs = xFormRepository.findAllProgramByState(stateId);
		
		for(Program program:programs)
		{
			
			ProgramModel programModel = new ProgramModel();
			programModel.setLive(program.getIsLive());
			programModel.setProgramId(program.getProgramId());
			programModel.setProgramName(program.getProgramName());
			
			programModels.add(programModel);
			
		}
		
		return programModels;
	}

}
