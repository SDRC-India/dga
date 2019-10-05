/**
 * @author Harsh Pratyush
 * @email harsh@sdrc.co.in
 * @create date 2018-12-07 17:15:46
 * @modify date 2018-12-07 17:15:46
 *
*/
package org.sdrc.dga.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.model.FacilityPlanningModel;
import org.sdrc.dga.model.FormXpathScoreMappingModel;
import org.sdrc.dga.model.GoogleMapDataModel;
import org.sdrc.dga.model.ProgramModel;
import org.sdrc.dga.model.ScoreModel;
import org.sdrc.dga.model.SpiderDataCollection;
import org.sdrc.dga.model.TimePeriodModel;
import org.sdrc.dga.service.DashboardService;
import org.sdrc.dga.util.Constants;
import org.sdrc.dga.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private StateManager stateManager;
	

	@RequestMapping("/getAllData")
	@ResponseBody
	public List<ScoreModel> getAllAggregatedData(
			@RequestParam("formId") Integer formId) {
		return dashboardService.getAllAggregatedData(formId);
	}
	


	@PreAuthorize("hasAuthority('dashboard,View')")
	@GetMapping(value = "/googleMapData")
	@ResponseBody
	public List<GoogleMapDataModel> fetchAllGoogleMapData(
			@RequestParam("formId") Integer formId,
			@RequestParam("sector") Integer sectorId,
			@RequestParam("areaId") Integer areaId,
			@RequestParam("timePeriodId")int timePeriodId) throws Exception {

		
		CollectUserModel collectUserModel = (CollectUserModel) stateManager
				.getValue(Constants.USER_PRINCIPAL);
		//On load of page if the user is of district level then areaId will be set as its district id
		if (collectUserModel.getUserRoleFeaturePermissionMappings()
						.get(0).getRoleFeaturePermissionSchemeModel()
						.getAreaModel().getAreaLevelId() > 3
				&& collectUserModel.getUserRoleFeaturePermissionMappings()
						.get(0).getRoleFeaturePermissionSchemeModel()
						.getAreaModel().getParentAreaId() != -1) {
			areaId = collectUserModel.getUserRoleFeaturePermissionMappings()
					.get(0).getRoleFeaturePermissionSchemeModel()
					.getAreaModel().getAreaId();
		}
		return dashboardService.fetchAllGoogleMapData(formId, sectorId, areaId,timePeriodId);
	}

	@PreAuthorize("hasAuthority('dashboard,View')")
	@GetMapping("/fetchLabelAndScore")
	@ResponseBody
	public List<ScoreModel> fetchLabelFromLastVisitData(
			@RequestParam("lastVisitDataId") Integer lastVisitDataId)
			throws Exception {
		return dashboardService.fetchLabelFromLastVisitData(lastVisitDataId);
	}

	@PreAuthorize("hasAuthority('dashboard,View')")
	@GetMapping("/fetchGridData")
	@ResponseBody
	public Map<String, List<ScoreModel>> fetchGridData(
			@RequestParam(value = "formId", required = false) Integer formId,
			@RequestParam(value = "lastVisitDataId", required = false) Integer lastVisitDataId
			,@RequestParam("timePeriodId")int timePeriodId)
			throws Exception {
		return dashboardService.getGridTableData(formId, lastVisitDataId,timePeriodId);
	}

	@PreAuthorize("hasAuthority('dashboard,View')")
	@GetMapping(value = "/spiderData")
	@ResponseBody
	public SpiderDataCollection fetchSpiderData(
			@RequestParam(value = "formId", required = false) Integer formId,
			@RequestParam(value = "lastVisitDataId", required = false) Integer lastVisitDataId,
			@RequestParam(value = "areaId", required = false) Integer areaId,
			@RequestParam("parentXpathId") int parentXpathId,
			@RequestParam("formMetaId") int formMetaId
			)
			throws Exception {
		return dashboardService.getfetchSpiderData(formId, lastVisitDataId,
				areaId,parentXpathId,formMetaId);
	}

	@PreAuthorize("hasAuthority('dashboard,View')")
	@GetMapping(value = "/getParentSectors")
	@ResponseBody
	public List<FormXpathScoreMappingModel> getParentSectors(@RequestParam("timeperiodId")int timeperiodId,
			@RequestParam("stateId")int stateId,@RequestParam("programId")int programId) {
		return dashboardService.getParentSectors(timeperiodId,stateId,programId);
	}

	@PreAuthorize("hasAuthority('dashboard,View')")
	@GetMapping(value = "/getSectors")
	@ResponseBody
	public List<FormXpathScoreMappingModel> getSectors(
			@RequestParam("parentId") Integer parentId) {
		return dashboardService.getSectors(parentId);
	}
	
	

	@PreAuthorize("hasAuthority('dashboard,View')")
	@GetMapping(value = "/getAllDistrict")
	@ResponseBody
	public List<AreaModel> getAllDistricts(@RequestParam("stateId")int stateId) {
		return dashboardService.getAllDistricts(stateId);
	}
	
	@PreAuthorize("hasAnyAuthority('dashboard,View','dataTree,View','FIP,View')")
	@GetMapping(value = "/getAllState")
	@ResponseBody
	public List<AreaModel> getAllState() {
		return dashboardService.getAllState();
	}

	@PreAuthorize("hasAuthority('dashboard,View')")
	@GetMapping("dashboard")
	public String dashboardPage() {
		return "Dashboard";
	}
	
	
	@PreAuthorize("hasAnyAuthority('dashboard,View','dataTree,View','FIP,View')")
	@GetMapping(value = "/getAllProgramme")
	@ResponseBody
	public List<ProgramModel> getAllProgramme(@RequestParam("stateId")int stateId) {
		return dashboardService.getAllProgramme(stateId);
	}

	

	@PreAuthorize("hasAnyAuthority('dashboard,View','dataTree,View','FIP,View')")
	@GetMapping("/getAllTimePeriod")
	@ResponseBody
	List<TimePeriodModel> getTimePeriods(@RequestParam("stateId")int stateId,@RequestParam("programId")int programId) {
		return dashboardService.getAllPlanningTimePeriod(stateId,programId);
	}

	@PreAuthorize("hasAuthority('dashboard,View')")
	@PostMapping("/exportToPdf")
//	@Authorize(feature = "dashboard", permission = "View")
	@ResponseBody
	public String exportToPdf(
			@RequestBody List<String> svgs,
			@RequestParam("noOfFacilities")int noOfFacilities,
			@RequestParam(value = "formId", required = false) Integer formId,
			@RequestParam(value = "lastVisitDataId", required = false) Integer lastVisitDataId,
			@RequestParam(value = "areaId", required = false) Integer areaId,
			@RequestParam("timePeriodId")int timePeriodId,
			@RequestParam("parentXpathId") int parentXpathId,
			@RequestParam("formMetaId") int formMetaId,
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {

		String pdfPath = dashboardService.exportToPdf(svgs.get(0), svgs.get(0),
				formId, lastVisitDataId, areaId, response,noOfFacilities,timePeriodId,parentXpathId,formMetaId
				,request);

		return pdfPath;

	}
	
	@PreAuthorize("hasAuthority('dashboard,View')")
	@PostMapping("/exportToExcel")
//	@Authorize(feature = "dashboard", permission = "View")
	@ResponseBody
	public String exportToExcel(
			@RequestBody List<String> svgs,
			@RequestParam("noOfFacilities")int noOfFacilities,
			@RequestParam(value = "formId", required = false) Integer formId,
			@RequestParam(value = "lastVisitDataId", required = false) Integer lastVisitDataId,
			@RequestParam(value = "areaId", required = false) Integer areaId,
			@RequestParam("timePeriodId")int timePeriodId,
			@RequestParam("parentXpathId") int parentXpathId,
			@RequestParam("formMetaId") int formMetaId,
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {

		String pdfPath = dashboardService.exportToExcel(svgs.get(0), svgs.get(0),
				formId, lastVisitDataId, areaId, response,noOfFacilities,timePeriodId,parentXpathId,formMetaId,request);

		return pdfPath;

	}
	
	@PostMapping(value = "/downloadFile")
	public void downLoad(@RequestParam("fileName") String name,HttpServletResponse response) throws IOException {
		InputStream inputStream;
		String fileName = "";
		try {
//			fileName=name.replaceAll("%3A", ":").replaceAll("%2F", "/")
//						 .replaceAll("%5C", "/").replaceAll("%2C",",")
//						 .replaceAll("\\+", " ").replaceAll("%22", "")
//						 .replaceAll("%3F", "?").replaceAll("%3D", "=");
			
			fileName = name.replaceAll("%3A", ":")
					.replaceAll("%2F", "/").replaceAll("%5C", "/")
					.replaceAll("%2C", ",").replaceAll("\\+", " ").replaceAll("%22", "")
					.replaceAll("%3F", "?").replaceAll("%3D", "=")
					.replaceAll("%20", " ");
			inputStream = new FileInputStream(fileName);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					new java.io.File(fileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("application/octet-stream"); //for all file type
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			new File(fileName).delete();
		}
	}

	@PreAuthorize("hasAuthority('dashboard,View')")
//	@Authorize(feature = "dashboard", permission = "View")
	@RequestMapping("/getPlannedFacilities")
	@ResponseBody
	public FacilityPlanningModel getPlannedFacilities(@RequestParam("formId") int formId,@RequestParam("areaId") int areaId,@RequestParam("timeperiodId") int timePeriodId,@RequestParam("stateId")int stateId)
	{
	
		return dashboardService.getPlannedFacilities(formId,areaId,timePeriodId,stateId);
	}
	
	
}
