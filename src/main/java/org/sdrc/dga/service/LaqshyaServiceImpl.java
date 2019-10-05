package org.sdrc.dga.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.poi.POIXMLProperties;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.CollectUser;
import org.sdrc.dga.domain.LaqshyaData;
import org.sdrc.dga.domain.LaqshyaFacility;
import org.sdrc.dga.domain.LaqshyaFacilityType;
import org.sdrc.dga.model.LaqshyaDataModel;
import org.sdrc.dga.model.LaqshyaDatas;
import org.sdrc.dga.model.ResponseModel;
import org.sdrc.dga.model.TableDetails;
import org.sdrc.dga.model.ValueArray;
import org.sdrc.dga.repository.AreaRepository;
import org.sdrc.dga.repository.CollectUserRepository;
import org.sdrc.dga.repository.LaqshyaDataRepository;
import org.sdrc.dga.repository.LaqshyaFacilityRepository;
import org.sdrc.dga.repository.LaqshyaFacilityTypeRepository;
import org.sdrc.dga.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class LaqshyaServiceImpl implements LaqshyaService {

	@Autowired
	LaqshyaFacilityRepository laqshyaFacilityRepository;

	@Autowired
	AreaRepository areaRepository;

	@Autowired
	LaqshyaDataRepository laqshyaDataRepository;

	@Autowired
	LaqshyaFacilityTypeRepository laqshyaFacilityTypeRepository;

	@Autowired
	private CollectUserRepository collectUserRepository;

	@Autowired
	private MessageSource messages;

	@Override
	public boolean configureLaqshyaFacility() throws Exception {

		FileInputStream fis = new FileInputStream("C:/Users/SDRC_DEV/Desktop/DgaIndia/laqshya_cg_status.xlsx");
//		List<LaqshyaFacility> allFacility = laqshyaFacilityRepository.findAll();
		List<Area> areaList = areaRepository.findByAreaLevelAreaLevelId(4);
		List<LaqshyaData> dataList = laqshyaDataRepository.findAll();
		LaqshyaFacilityType facilityType = laqshyaFacilityTypeRepository.findByFacilityTypeName("DH");
		List<LaqshyaFacilityType> facilityTypeList = laqshyaFacilityTypeRepository.findByIsLiveTrue();

		Map<String, LaqshyaFacilityType> facilityTypeMap = new HashMap<String, LaqshyaFacilityType>();

		for (LaqshyaFacilityType type : facilityTypeList) {
			facilityTypeMap.put(type.getFacilityTypeName(), type);
		}

		List<LaqshyaData> newDataList = new ArrayList<LaqshyaData>();

		Map<String, Area> areaMap = new HashMap<String, Area>();
		Map<String, LaqshyaFacility> facilityMap = new HashMap<>();
		Map<String, LaqshyaData> dataMap = new HashMap<>();
		for (Area area : areaList) {
			areaMap.put(area.getAreaName(), area);
		}
//		for(LaqshyaFacility facility : allFacility) {
//			facilityMap.put(facility.getFacilityName(), facility);
//		}
		for (LaqshyaData ldata : dataList) {
			dataMap.put(ldata.getFacilityName().trim().toUpperCase(), ldata);
		}

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);

		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		LaqshyaData laqshyaData = null;
		for (int i = 1; i <= xssfSheet.getLastRowNum(); i++) {

			Row row = xssfSheet.getRow(i);

			if (row == null)
				break;

			Cell districtCell = row.getCell(1);
			Cell facilityCell = row.getCell(2);
			Cell typeCell = row.getCell(3);
//			Cell facilityTypeCell = row.getCell(4);
			Cell totalScoreCell = row.getCell(4);
			Cell serviceProviderCell = row.getCell(5);
			Cell pRightCell = row.getCell(6);
			Cell inputCell = row.getCell(7);
			Cell shupportServiceCell = row.getCell(8);
			Cell clenicalServiceCell = row.getCell(9);
			Cell infectionControlleCell = row.getCell(10);
			Cell qManagementCell = row.getCell(11);
			Cell outComeCell = row.getCell(12);
			Cell sAssesmentCell = row.getCell(13);
			Cell sCertificatioLaborCell = row.getCell(14);
			Cell sCertificationOtCell = row.getCell(15);
			Cell nCertificationLaborCell = row.getCell(16);
			Cell nCertificationOtCell = row.getCell(17);

			if (i % 2 != 0) {
				laqshyaData = new LaqshyaData();
				laqshyaData.setDistrictId(areaMap.get(districtCell.toString().trim().toUpperCase()));
				laqshyaData.setFacilityName(facilityCell.toString());
//				laqshyaData.setFacilityType(facilityType);
				if (facilityCell.toString().contains("District")) {
					laqshyaData.setFacilityType(facilityTypeMap.get("DH"));
				} else if (facilityCell.toString().contains("CHC")) {
					laqshyaData.setFacilityType(facilityTypeMap.get("CHC"));
				} else if (facilityCell.toString().contains("Medical")) {
					laqshyaData.setFacilityType(facilityTypeMap.get("MedicalCollege"));
				} else {
					laqshyaData.setFacilityType(facilityType);
				}
//				laqshyaData.setFacilityType(facilityTypeMap.get(facilityCell.toString()));
				laqshyaData.setDistrictName(districtCell.toString());
				laqshyaData.setLive(true);

				if (clenicalServiceCell != null)
					laqshyaData.setClinicalServicesPeer(clenicalServiceCell.toString());
				if (infectionControlleCell != null)
					laqshyaData.setInfectionControlPeer(infectionControlleCell.toString());
				if (inputCell != null)
					laqshyaData.setInputPeer(inputCell.toString());
				if (nCertificationLaborCell != null) {
					laqshyaData.setNationalCertificationLaborRoomPeer(nCertificationLaborCell.toString());
				}
				else
					laqshyaData.setNationalCertificationLaborRoomPeer("-");
				if (nCertificationOtCell != null) {
					laqshyaData.setNationalCertificationOTPeer(nCertificationOtCell.toString());
				}
				else if(nCertificationOtCell.toString().equals(""))
					laqshyaData.setNationalCertificationOTPeer("-");
				if (outComeCell != null)
					laqshyaData.setOutcomePeer(outComeCell.toString());
				if (pRightCell != null)
					laqshyaData.setPatientRightPeer(pRightCell.toString());
				if (qManagementCell != null)
					laqshyaData.setQualityManagementPeer(qManagementCell.toString());
				if (serviceProviderCell != null)
					laqshyaData.setServiceProvisionPeer(serviceProviderCell.toString());
				if (sAssesmentCell != null) {
					if(sAssesmentCell.toString().equals(""))
						laqshyaData.setStateAssesmentDonePeer("-");
					else
					laqshyaData.setStateAssesmentDonePeer(sAssesmentCell.toString());
				}
				
				if (sCertificatioLaborCell != null) {
					 if(sCertificatioLaborCell.toString().equals(""))
						laqshyaData.setStateCertificationLaborRoomPeer("-");
					 else
						 laqshyaData.setStateCertificationLaborRoomPeer(sCertificatioLaborCell.toString());
				}
			
				if (sCertificationOtCell != null) {
					 if(sCertificationOtCell.toString().equals(""))
						laqshyaData.setStateCertificationOTPeer("-");
					 else
						 laqshyaData.setStateCertificationOTPeer(sCertificationOtCell.toString());
				}
				
				if (shupportServiceCell != null)
					laqshyaData.setSupportServicesPeer(shupportServiceCell.toString());
				if (totalScoreCell != null)
					laqshyaData.setTotalScorePeer(totalScoreCell.toString());

			} else {

				if (clenicalServiceCell != null)
					laqshyaData.setClinicalServicesBaseline(clenicalServiceCell.toString());
				if (infectionControlleCell != null)
					laqshyaData.setInfectionControlBaseline(infectionControlleCell.toString());
				if (inputCell != null)
					laqshyaData.setInputBaseline(inputCell.toString());
				if (nCertificationLaborCell != null) {
					if(nCertificationLaborCell.toString().equals(""))
						laqshyaData.setNationalCertificationLaborRoomBaseline("-");
					else
					laqshyaData.setNationalCertificationLaborRoomBaseline(nCertificationLaborCell.toString());
				}
				
					
				if (nCertificationOtCell != null) {
					if(nCertificationOtCell.toString().equals(""))
						laqshyaData.setNationalCertificationOTBaseline("-");
					else
					laqshyaData.setNationalCertificationOTBaseline(nCertificationOtCell.toString());
				}
				
					
				if (outComeCell != null)
					laqshyaData.setOutcomeBaseline(outComeCell.toString());
				if (pRightCell != null)
					laqshyaData.setPatientRightBaseline(pRightCell.toString());
				if (qManagementCell != null)
					laqshyaData.setQualityManagementBaseline(qManagementCell.toString());
				if (serviceProviderCell != null)
					laqshyaData.setServiceProvisionBaseline(serviceProviderCell.toString());
				if (sAssesmentCell != null) {
					if(sAssesmentCell.toString().equals(""))
						laqshyaData.setStateAssesmentDoneBaseline("-");
					else
					laqshyaData.setStateAssesmentDoneBaseline(sAssesmentCell.toString());
				}
				
					
				if (sCertificatioLaborCell != null) {
					if(sCertificatioLaborCell.toString().equals(""))
						laqshyaData.setStateCertificationLaborRoomBaseline("-");
					else
					laqshyaData.setStateCertificationLaborRoomBaseline(sCertificatioLaborCell.toString());
				}
				
					
				if (sCertificationOtCell != null) {
					if(sCertificationOtCell.toString().equals(""))
						laqshyaData.setStateCertificationOTBaseline("-");
					else
					laqshyaData.setStateCertificationOTBaseline(sCertificationOtCell.toString());
				}
				
					
				if (shupportServiceCell != null)
					laqshyaData.setSupportServicesBaseline(shupportServiceCell.toString());
				if (totalScoreCell != null)
					laqshyaData.setTotalScoreBaseline(totalScoreCell.toString());

				newDataList.add(laqshyaData);
			}

		}
		System.out.println("datas->" + newDataList);
		laqshyaDataRepository.save(newDataList);

		return true;
	}

	@Override
	public LaqshyaDatas getLaqshyaData(Principal auth) {
//	public LaqshyaDatas getLaqshyaData() {
//		List<LaqshyaDatas> laqshayaDatasList = new ArrayList();

		String usreName = auth.getName();
//		System.out.println("get data for user - "+usreName);
//		String usreName = "test2";
		CollectUser user = collectUserRepository.findByUsername(usreName);
		List<LaqshyaData> datas = null;

		Area userArea = user.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getArea();

		if (usreName.equalsIgnoreCase("admin") || userArea.getAreaId() == 2 || userArea.getAreaId() == 1) {
			datas = laqshyaDataRepository.findByIsLiveTrue();
		} else
			datas = laqshyaDataRepository.findByDistrictIdAndIsLiveTrue(userArea);

//		List<LaqshyaData> datas = laqshyaDataRepository.findByDistrictIdAndIsLiveTrue(userArea);

		// set Table header data
		String[] headers = { "Type of Assessment ", "Total Score ", "Service Provision", "Patient Right", "Input",
				"Support Services", "Clinical Services", "Infection Control", "Quality Management", "Outcome",
				"State Assesment Done", "State Certification - Labor Room", "State Certification - OT",
				"National Certification - Labor Room", "National Certification - OT" };

//		TableDetails tableDetails = new TableDetails();
		LaqshyaDatas laqshayaDatas = new LaqshyaDatas();
		laqshayaDatas.setHeaders(headers);
		List<TableDetails> tableDetailsList = new ArrayList<TableDetails>();
		int i = 2;
		for (LaqshyaData data : datas) {
//			LaqshyaDataModel model = new LaqshyaDataModel();
			TableDetails tableDetails = new TableDetails();
			tableDetails.setDistrictName(data.getDistrictName());
			tableDetails.setNameOfFacility(data.getFacilityName());
			tableDetails.setId(data.getId());
			tableDetails.setSerialNo(i);
			List<ValueArray> valArrayList = new ArrayList<>();

			ValueArray assesmentArray = new ValueArray();
			assesmentArray.setBaseLine("Baseline Assessment Labor Room");
			assesmentArray.setPeer("Peer Assessment");
			valArrayList.add(assesmentArray);

			ValueArray totalScoreArray = new ValueArray();
			if (data.getTotalScoreBaseline() != null)
				totalScoreArray.setBaseLine(data.getTotalScoreBaseline().trim());
			if (data.getTotalScorePeer() != null)
				totalScoreArray.setPeer(data.getTotalScorePeer().trim());
			valArrayList.add(totalScoreArray);

			ValueArray serviceProvisionArray = new ValueArray();
			if (data.getServiceProvisionBaseline() != null)
				serviceProvisionArray.setBaseLine(data.getServiceProvisionBaseline().trim());
			if (data.getServiceProvisionPeer() != null)
				serviceProvisionArray.setPeer(data.getServiceProvisionPeer().trim());
			valArrayList.add(serviceProvisionArray);

			ValueArray patientRightArray = new ValueArray();
			if (data.getPatientRightBaseline() != null)
				patientRightArray.setBaseLine(data.getPatientRightBaseline().trim());
			if (data.getPatientRightPeer() != null)
				patientRightArray.setPeer(data.getPatientRightPeer().trim());
			valArrayList.add(patientRightArray);

			ValueArray inputArray = new ValueArray();
			if (data.getInputBaseline() != null)
				inputArray.setBaseLine(data.getInputBaseline().trim());
			if (data.getInputPeer() != null)
				inputArray.setPeer(data.getInputPeer().trim());
			valArrayList.add(inputArray);

			ValueArray supportServicesArray = new ValueArray();
			if (data.getSupportServicesBaseline() != null)
				supportServicesArray.setBaseLine(data.getSupportServicesBaseline().trim());
			if (data.getSupportServicesPeer() != null)
				supportServicesArray.setPeer(data.getSupportServicesPeer().trim());
			valArrayList.add(supportServicesArray);

			ValueArray clinicalServicesArray = new ValueArray();
			if (data.getClinicalServicesBaseline() != null)
				clinicalServicesArray.setBaseLine(data.getClinicalServicesBaseline().trim());
			if (data.getClinicalServicesPeer() != null)
				clinicalServicesArray.setPeer(data.getClinicalServicesPeer().trim());
			valArrayList.add(clinicalServicesArray);

			ValueArray infectionControlArray = new ValueArray();
			if (data.getInfectionControlBaseline() != null)
				infectionControlArray.setBaseLine(data.getInfectionControlBaseline().trim());
			if (data.getInfectionControlPeer() != null)
				infectionControlArray.setPeer(data.getInfectionControlPeer().trim());
			valArrayList.add(infectionControlArray);

			ValueArray qualityManagementArray = new ValueArray();
			if (data.getQualityManagementBaseline() != null)
				qualityManagementArray.setBaseLine(data.getQualityManagementBaseline().trim());
			if (data.getQualityManagementPeer() != null)
				qualityManagementArray.setPeer(data.getQualityManagementPeer().trim());
			valArrayList.add(qualityManagementArray);

			ValueArray outcomeArray = new ValueArray();
			if (data.getOutcomeBaseline() != null)
				outcomeArray.setBaseLine(data.getOutcomeBaseline().trim());
			if (data.getOutcomePeer() != null)
				outcomeArray.setPeer(data.getOutcomePeer().trim());
			valArrayList.add(outcomeArray);

			ValueArray stateAssesmentArray = new ValueArray();
			if (data.getStateAssesmentDoneBaseline() != null)
				stateAssesmentArray.setBaseLine(data.getStateAssesmentDoneBaseline().trim());
			else
				stateAssesmentArray.setBaseLine("-");
			if (data.getStateAssesmentDonePeer() != null)
				stateAssesmentArray.setPeer(data.getStateAssesmentDonePeer().trim());
			else
				stateAssesmentArray.setPeer("-");
			valArrayList.add(stateAssesmentArray);

			ValueArray stateCertificationLaborArray = new ValueArray();
			if (data.getStateCertificationLaborRoomBaseline() != null)
				stateCertificationLaborArray.setBaseLine(data.getStateCertificationLaborRoomBaseline().trim());
			else
				stateCertificationLaborArray.setBaseLine("-");
			if (data.getStateCertificationLaborRoomPeer() != null)
				stateCertificationLaborArray.setPeer(data.getStateCertificationLaborRoomPeer().trim());
			else
				stateCertificationLaborArray.setPeer("-");
			valArrayList.add(stateCertificationLaborArray);

			ValueArray stateCertificationOTArray = new ValueArray();
			if (data.getStateCertificationOTBaseline() != null)
				stateCertificationOTArray.setBaseLine(data.getStateCertificationOTBaseline().trim());
			else
				stateCertificationOTArray.setBaseLine("-");
			if (data.getStateCertificationOTPeer() != null)
				stateCertificationOTArray.setPeer(data.getStateCertificationOTPeer().trim());
			else
				stateCertificationOTArray.setPeer("-");
			valArrayList.add(stateCertificationOTArray);

			ValueArray nationalCertificationLaborArray = new ValueArray();
			if (data.getNationalCertificationLaborRoomBaseline() != null)
				nationalCertificationLaborArray.setBaseLine(data.getNationalCertificationLaborRoomBaseline().trim());
			else
				nationalCertificationLaborArray.setBaseLine("-");
			if (data.getNationalCertificationLaborRoomPeer() != null)
				nationalCertificationLaborArray.setPeer(data.getNationalCertificationLaborRoomPeer().trim());
			else
				nationalCertificationLaborArray.setPeer("-");
			valArrayList.add(nationalCertificationLaborArray);

			ValueArray nationalCertificationOTArray = new ValueArray();
			if (data.getNationalCertificationOTBaseline() != null)
				nationalCertificationOTArray.setBaseLine(data.getNationalCertificationOTBaseline().trim());
			else
				nationalCertificationOTArray.setBaseLine("-");
			if (data.getNationalCertificationOTPeer() != null)
				nationalCertificationOTArray.setPeer(data.getNationalCertificationOTPeer().trim());
			else
				nationalCertificationOTArray.setPeer("-");
			valArrayList.add(nationalCertificationOTArray);

			tableDetails.setValueArray(valArrayList);
//			laqshayaData.add(model);
			tableDetailsList.add(tableDetails);
			i++;
		}
		laqshayaDatas.setTableDetails(tableDetailsList);
//		laqshayaDatasList.add(laqshayaDatas);
		return laqshayaDatas;
	}

	@Override
	@Transactional
	public ResponseModel saveLaqshyaData(LaqshyaDatas laqshyaModel, Principal auth)
//	public ResponseModel saveLaqshyaData()
	{
		ResponseModel responseModel = new ResponseModel();
		String usreName = auth.getName();
		System.out.println("save data for user - " + usreName);
//		String usreName = "test2";
		CollectUser user = collectUserRepository.findByUsername(usreName);
		Area userArea = user.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getArea();

//		LaqshyaDatas laqshyaModel = getLaqshyaData();

		List<TableDetails> tableDetailsList = laqshyaModel.getTableDetails();
		List<LaqshyaData> laqshyaDtaList = new ArrayList();

		for (TableDetails tableData : tableDetailsList) {
			LaqshyaData data = laqshyaDataRepository.findById(tableData.getId());
//			data.setId(tableData.getId());

			List valueArrays = tableData.getValueArray();

			data.setTotalScoreBaseline(((ValueArray) valueArrays.get(1)).getBaseLine());
			data.setTotalScorePeer(((ValueArray) valueArrays.get(1)).getPeer());

			data.setServiceProvisionBaseline(((ValueArray) valueArrays.get(2)).getBaseLine());
			data.setServiceProvisionPeer(((ValueArray) valueArrays.get(2)).getPeer());

			data.setPatientRightBaseline(((ValueArray) valueArrays.get(3)).getBaseLine());
			data.setPatientRightPeer(((ValueArray) valueArrays.get(3)).getPeer());

			data.setInputBaseline(((ValueArray) valueArrays.get(4)).getBaseLine());
			data.setInputPeer(((ValueArray) valueArrays.get(4)).getPeer());

			data.setSupportServicesBaseline(((ValueArray) valueArrays.get(5)).getBaseLine());
			data.setSupportServicesPeer(((ValueArray) valueArrays.get(5)).getPeer());

			data.setClinicalServicesBaseline(((ValueArray) valueArrays.get(6)).getBaseLine());
			data.setClinicalServicesPeer(((ValueArray) valueArrays.get(6)).getPeer());

			data.setInfectionControlBaseline(((ValueArray) valueArrays.get(7)).getBaseLine());
			data.setInfectionControlPeer(((ValueArray) valueArrays.get(7)).getPeer());

			data.setQualityManagementBaseline(((ValueArray) valueArrays.get(8)).getBaseLine());
			data.setQualityManagementPeer(((ValueArray) valueArrays.get(8)).getPeer());

			data.setOutcomeBaseline(((ValueArray) valueArrays.get(9)).getBaseLine());
			data.setOutcomePeer(((ValueArray) valueArrays.get(9)).getPeer());

			data.setStateAssesmentDoneBaseline(((ValueArray) valueArrays.get(10)).getBaseLine());
			data.setStateAssesmentDonePeer(((ValueArray) valueArrays.get(10)).getPeer());

			data.setStateCertificationLaborRoomBaseline(((ValueArray) valueArrays.get(11)).getBaseLine());
			data.setStateCertificationLaborRoomPeer(((ValueArray) valueArrays.get(11)).getPeer());

			data.setStateCertificationOTBaseline(((ValueArray) valueArrays.get(12)).getBaseLine());
			data.setStateCertificationOTPeer(((ValueArray) valueArrays.get(12)).getPeer());

			data.setNationalCertificationLaborRoomBaseline(((ValueArray) valueArrays.get(13)).getBaseLine());
			data.setNationalCertificationLaborRoomPeer(((ValueArray) valueArrays.get(13)).getPeer());

			data.setNationalCertificationOTBaseline(((ValueArray) valueArrays.get(14)).getBaseLine());
			data.setNationalCertificationOTPeer(((ValueArray) valueArrays.get(14)).getPeer());

			laqshyaDataRepository.save(data);
		}
		responseModel.setStatusCode(HttpStatus.OK.value());
		responseModel.setMessage("Submission successful");

		return responseModel;
	}

	@Override
	public String getLaqshyaReport(Principal auth) throws IOException {
		String usreName = auth.getName();
//		String usreName = "admin";
		List<LaqshyaData> datas = null;

		CollectUser user = collectUserRepository.findByUsername(usreName);
		Area userArea = user.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getArea();

		String[] headerData = { "Sr.No.", "District", "Name of Facility", "Type of Assessment ", "Total Score ",
				"Service Provision", "Patient Right", "Input", "Support Services", "Clinical Services",
				"Infection Control", "Quality Management", "Outcome", "State Assesment Done",
				"State Certification - Labor Room", "State Certification - OT", "National Certification - Labor Room",
				"National Certification - OT" };

		if (usreName.equalsIgnoreCase("admin") || userArea.getAreaId() == 2) {
			datas = laqshyaDataRepository.findByIsLiveTrue();
		} else
			datas = laqshyaDataRepository.findByDistrictIdAndIsLiveTrue(userArea);

		if (datas.size() > 0) {

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
			Row row;
			Cell cell;
			int rowNum = 0, colNum = 0;

			XSSFSheet sheet = xssfWorkbook.createSheet();
//		POIXMLProperties xmlProps = xssfWorkbook.getProperties();
//		POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
//		coreProps.setCreator("dgaindia.org");

			CellStyle headingStyle = xssfWorkbook.createCellStyle();
			Font font = xssfWorkbook.createFont();
			font.setBold(true);
			headingStyle.setFont(font);
			headingStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			headingStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			headingStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headingStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			headingStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

			CellStyle cellStyle = xssfWorkbook.createCellStyle();
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			cellStyle.setDataFormat(xssfWorkbook.getCreationHelper().createDataFormat().getFormat("#.#"));

			CellStyle cellStyle2 = xssfWorkbook.createCellStyle();
			cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);

			cellStyle2.setDataFormat(xssfWorkbook.getCreationHelper().createDataFormat().getFormat("#.##"));

			row = sheet.createRow(rowNum);

			for (int i = 0; i < 18; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headingStyle);
				cell.setCellValue(headerData[i]);
				sheet.autoSizeColumn(cell.getColumnIndex());
			}
			rowNum++;

			// row = sheet.createRow(rowNum);
			int counter = 1;
			for (int j = 0; j < datas.size(); j++) {
				row = sheet.createRow(rowNum + j);
				cell = row.createCell(0);
//			cell.setCellStyle(headingStyle);
				cell.setCellValue(j + counter);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(1);
//			cell.setCellStyle(headingStyle);
				cell.setCellValue(datas.get(j).getDistrictName());
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(2);
				cell.setCellValue(datas.get(j).getFacilityName());
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(3);
				cell.setCellValue("Peer Assessment");
				cell.setCellStyle(cellStyle);

				cell = row.createCell(4);
				if (datas.get(j).getTotalScorePeer() == null || datas.get(j).getTotalScorePeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getTotalScorePeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(5);
				if (datas.get(j).getServiceProvisionPeer() == null || datas.get(j).getServiceProvisionPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getServiceProvisionPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(6);
				if (datas.get(j).getPatientRightPeer() == null || datas.get(j).getPatientRightPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getPatientRightPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(7);
				if (datas.get(j).getInputPeer() == null || datas.get(j).getInputPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getInputPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(8);
				if (datas.get(j).getSupportServicesPeer() == null || datas.get(j).getSupportServicesPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getSupportServicesPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(9);
				if (datas.get(j).getClinicalServicesPeer() == null || datas.get(j).getClinicalServicesPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getClinicalServicesPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(10);
				if (datas.get(j).getInfectionControlPeer() == null || datas.get(j).getInfectionControlPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getInfectionControlPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(11);
				if (datas.get(j).getQualityManagementPeer() == null
						|| datas.get(j).getQualityManagementPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getQualityManagementPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(12);
				if (datas.get(j).getOutcomePeer() == null || datas.get(j).getOutcomePeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getOutcomePeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(13);
				if (datas.get(j).getStateAssesmentDonePeer() == null
						|| datas.get(j).getStateAssesmentDonePeer().isEmpty()
						|| datas.get(j).getStateAssesmentDonePeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateAssesmentDonePeer());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(14);
				if (datas.get(j).getStateCertificationLaborRoomPeer() == null
						|| datas.get(j).getStateCertificationLaborRoomPeer().isEmpty()
						|| datas.get(j).getStateCertificationLaborRoomPeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateCertificationLaborRoomPeer());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(15);
				if (datas.get(j).getStateCertificationOTPeer() == null
						|| datas.get(j).getStateCertificationOTPeer().isEmpty()
						|| datas.get(j).getStateCertificationOTPeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateCertificationOTPeer());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(16);
				if (datas.get(j).getNationalCertificationLaborRoomPeer() == null
						|| datas.get(j).getNationalCertificationLaborRoomPeer().isEmpty()
						|| datas.get(j).getNationalCertificationLaborRoomPeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getNationalCertificationLaborRoomPeer());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(17);
				if (datas.get(j).getNationalCertificationOTPeer() == null
						|| datas.get(j).getNationalCertificationOTPeer().isEmpty()
						|| datas.get(j).getNationalCertificationOTPeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getNationalCertificationOTPeer());
				cell.setCellStyle(cellStyle);

				counter++;
				rowNum++;
				row = sheet.createRow(rowNum + j);

				cell = row.createCell(0);
//			cell.setCellStyle(headingStyle);
				cell.setCellValue(j + counter);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(1);
//			cell.setCellStyle(headingStyle);
				cell.setCellValue(datas.get(j).getDistrictName());
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(2);
				cell.setCellValue(datas.get(j).getFacilityName());
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(3);
				cell.setCellValue("Baseline Assessment Labor Room");
				cell.setCellStyle(cellStyle);

				cell = row.createCell(4);
				if (datas.get(j).getTotalScoreBaseline() == null || datas.get(j).getTotalScoreBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getTotalScoreBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(5);
				if (datas.get(j).getServiceProvisionBaseline() == null
						|| datas.get(j).getServiceProvisionBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getServiceProvisionBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(6);
				if (datas.get(j).getPatientRightBaseline() == null || datas.get(j).getPatientRightBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getPatientRightBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(7);
				if (datas.get(j).getInputBaseline() == null || datas.get(j).getInputBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getInputBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(8);
				if (datas.get(j).getSupportServicesBaseline() == null
						|| datas.get(j).getSupportServicesBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getSupportServicesBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(9);
				if (datas.get(j).getClinicalServicesBaseline() == null
						|| datas.get(j).getClinicalServicesBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getClinicalServicesBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(10);
				if (datas.get(j).getInfectionControlBaseline() == null
						|| datas.get(j).getInfectionControlBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getInfectionControlBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(11);
				if (datas.get(j).getQualityManagementBaseline() == null
						|| datas.get(j).getQualityManagementBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getQualityManagementBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(12);
				if (datas.get(j).getOutcomeBaseline() == null || datas.get(j).getOutcomeBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Double.valueOf(datas.get(j).getOutcomeBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(13);
				if (datas.get(j).getStateAssesmentDoneBaseline() == null
						|| datas.get(j).getStateAssesmentDoneBaseline().isEmpty()
						|| datas.get(j).getStateAssesmentDoneBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateAssesmentDoneBaseline());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(14);
				if (datas.get(j).getStateCertificationLaborRoomBaseline() == null
						|| datas.get(j).getStateCertificationLaborRoomBaseline().isEmpty()
						|| datas.get(j).getStateCertificationLaborRoomBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateCertificationLaborRoomBaseline());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(15);
				if (datas.get(j).getStateCertificationOTBaseline() == null
						|| datas.get(j).getStateCertificationOTBaseline().isEmpty()
						|| datas.get(j).getStateCertificationOTBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateCertificationOTBaseline());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(16);
				if (datas.get(j).getNationalCertificationLaborRoomBaseline() == null
						|| datas.get(j).getNationalCertificationLaborRoomBaseline().isEmpty()
						|| datas.get(j).getNationalCertificationLaborRoomBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getNationalCertificationLaborRoomBaseline());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(17);
				if (datas.get(j).getNationalCertificationOTBaseline() == null
						|| datas.get(j).getNationalCertificationOTBaseline().isEmpty()
						|| datas.get(j).getNationalCertificationOTBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getNationalCertificationOTBaseline());
				cell.setCellStyle(cellStyle);

//			counter++;
//			rowNum++;

			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
			String filepath = messages.getMessage("LaqshyaOutputPath", null, null) + datas.get(0).getDistrictName()
					+ "_" + sdf.format(new Date()) + ".xlsx";

//		sheet.createFreezePane(0, 1);
			FileOutputStream fileOutputStream = new FileOutputStream(filepath);
			xssfWorkbook.write(fileOutputStream);
			xssfWorkbook.close();
			System.out.println(filepath);
			return filepath;
		} else
			return "";
	}

	@Override
	public String getLaqshyaReport() throws IOException {
//		String usreName = auth.getName();
		String usreName = "admin";
		List<LaqshyaData> datas = null;

		CollectUser user = collectUserRepository.findByUsername(usreName);
		Area userArea = user.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getArea();

		String[] headerData = { "Sr.No.", "District", "Name of Facility", "Type of Assessment ", "Total Score ",
				"Service Provision", "Patient Right", "Input", "Support Services", "Clinical Services",
				"Infection Control", "Quality Management", "Outcome", "State Assesment Done",
				"State Certification - Labor Room", "State Certification - OT", "National Certification - Labor Room",
				"National Certification - OT" };

		if (usreName.equalsIgnoreCase("admin") || userArea.getAreaId() == 2) {
			datas = laqshyaDataRepository.findByIsLiveTrue();
		} else
			datas = laqshyaDataRepository.findByDistrictIdAndIsLiveTrue(userArea);

		if (datas.size() > 1) {

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
			Row row;
			Cell cell;
			int rowNum = 0, colNum = 0;

			XSSFSheet sheet = xssfWorkbook.createSheet();
//		POIXMLProperties xmlProps = xssfWorkbook.getProperties();
//		POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
//		coreProps.setCreator("dgaindia.org");

			CellStyle headingStyle = xssfWorkbook.createCellStyle();
			Font font = xssfWorkbook.createFont();
			font.setBold(true);
			headingStyle.setFont(font);
			headingStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			headingStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			headingStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headingStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			headingStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

			CellStyle cellStyle = xssfWorkbook.createCellStyle();
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

			row = sheet.createRow(rowNum);

			for (int i = 0; i < 18; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headingStyle);
				cell.setCellValue(headerData[i]);
				sheet.autoSizeColumn(cell.getColumnIndex());
			}
			rowNum++;

			// row = sheet.createRow(rowNum);
			int counter = 1;
			for (int j = 0; j < datas.size(); j++) {
				row = sheet.createRow(rowNum + j);
				cell = row.createCell(0);
//			cell.setCellStyle(headingStyle);
				cell.setCellValue(j + counter);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(1);
//			cell.setCellStyle(headingStyle);
				cell.setCellValue(datas.get(j).getDistrictName());
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(2);
				cell.setCellValue(datas.get(j).getFacilityName());
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(3);
				cell.setCellValue("Peer Assessment");
				cell.setCellStyle(cellStyle);

				cell = row.createCell(4);
				if (datas.get(j).getTotalScorePeer().isEmpty())
					cell.setCellValue(datas.get(j).getTotalScorePeer());
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getTotalScorePeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(5);
				if (datas.get(j).getServiceProvisionPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getServiceProvisionPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(6);
				if (datas.get(j).getPatientRightPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getPatientRightPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(7);
				if (datas.get(j).getInputPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getInputPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(8);
				if (datas.get(j).getSupportServicesPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getSupportServicesPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(9);
				if (datas.get(j).getClinicalServicesPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getClinicalServicesPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(10);
				if (datas.get(j).getInfectionControlPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getInfectionControlPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(11);
				if (datas.get(j).getQualityManagementPeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getQualityManagementPeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(12);
				if (datas.get(j).getOutcomePeer().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getOutcomePeer()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(13);
				if (datas.get(j).getStateAssesmentDonePeer().isEmpty()
						|| datas.get(j).getStateCertificationLaborRoomBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateAssesmentDonePeer());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(14);
				if (datas.get(j).getStateCertificationLaborRoomPeer().isEmpty()
						|| datas.get(j).getStateCertificationLaborRoomPeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateCertificationLaborRoomPeer());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(15);
				if (datas.get(j).getStateCertificationOTPeer().isEmpty()
						|| datas.get(j).getStateCertificationOTPeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateCertificationOTPeer());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(16);
				if (datas.get(j).getNationalCertificationLaborRoomPeer().isEmpty()
						|| datas.get(j).getNationalCertificationLaborRoomPeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getNationalCertificationLaborRoomPeer());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(17);
				if (datas.get(j).getNationalCertificationOTPeer().isEmpty()
						|| datas.get(j).getNationalCertificationOTPeer().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getNationalCertificationOTPeer());
				cell.setCellStyle(cellStyle);

				counter++;
				rowNum++;
				row = sheet.createRow(rowNum + j);

				cell = row.createCell(0);
//			cell.setCellStyle(headingStyle);
				cell.setCellValue(j + counter);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(1);
//			cell.setCellStyle(headingStyle);
				cell.setCellValue(datas.get(j).getDistrictName());
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(2);
				cell.setCellValue(datas.get(j).getFacilityName());
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(cell.getColumnIndex());

				cell = row.createCell(3);
				cell.setCellValue("Baseline Assessment Labor Room");
				cell.setCellStyle(cellStyle);

				cell = row.createCell(4);
				if (datas.get(j).getTotalScoreBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getTotalScoreBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(5);
				if (datas.get(j).getServiceProvisionBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getServiceProvisionBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(6);
				if (datas.get(j).getPatientRightBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getPatientRightBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(7);
				if (datas.get(j).getInputBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getInputBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(8);
				if (datas.get(j).getSupportServicesBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getSupportServicesBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(9);
				if (datas.get(j).getClinicalServicesBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getClinicalServicesBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(10);
				if (datas.get(j).getInfectionControlBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getInfectionControlBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(11);
				if (datas.get(j).getQualityManagementBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getQualityManagementBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(12);
				if (datas.get(j).getOutcomeBaseline().isEmpty())
					cell.setCellValue("");
				else
					cell.setCellValue(Float.valueOf(datas.get(j).getOutcomeBaseline()));
				cell.setCellStyle(cellStyle);

				cell = row.createCell(13);
				if (datas.get(j).getStateAssesmentDoneBaseline().isEmpty()
						|| datas.get(j).getStateAssesmentDoneBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateAssesmentDoneBaseline());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(14);
				if (datas.get(j).getStateCertificationLaborRoomBaseline().isEmpty()
						|| datas.get(j).getStateCertificationLaborRoomBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateCertificationLaborRoomBaseline());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(15);
				if (datas.get(j).getStateCertificationOTBaseline().isEmpty()
						|| datas.get(j).getStateCertificationOTBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getStateCertificationOTBaseline());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(16);
				if (datas.get(j).getNationalCertificationLaborRoomBaseline().isEmpty()
						|| datas.get(j).getNationalCertificationLaborRoomBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getNationalCertificationLaborRoomBaseline());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(17);
				if (datas.get(j).getNationalCertificationOTBaseline().isEmpty()
						|| datas.get(j).getNationalCertificationOTBaseline().trim().equals("-"))
					cell.setCellValue("");
				else
					cell.setCellValue(datas.get(j).getNationalCertificationOTBaseline());
				cell.setCellStyle(cellStyle);

//			counter++;
//			rowNum++;

			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
			String filepath = messages.getMessage("LaqshyaOutputPath", null, null) + datas.get(0).getDistrictName()
					+ "_" + sdf.format(new Date()) + ".xlsx";

//		sheet.createFreezePane(0, 1);
			FileOutputStream fileOutputStream = new FileOutputStream(filepath);
			xssfWorkbook.write(fileOutputStream);
			xssfWorkbook.close();

			return filepath;
		} else
			return "";
	}
}
