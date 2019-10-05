package org.sdrc.dga.web;

import java.io.File;
import java.io.FileInputStream;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.sdrc.dga.model.DDMDataEntryQuestionModel;
import org.sdrc.dga.model.DDMQuestionModel;
import org.sdrc.dga.model.DDMReportModel;
import org.sdrc.dga.model.ResponseModel;
import org.sdrc.dga.service.DistrictDataManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
public class DDMController {

	@Autowired
	private DistrictDataManageService districtDataManageService;
	
	@GetMapping("/configureQuestionTemplate")
	private boolean configureQuestionTemplate()
	{
		return districtDataManageService.configureIrfQuestionTemplate();
	}
	
//	@PreAuthorize("hasAuthority('dataentry_HAVING_write')")
	@GetMapping("getQuestion")
	public ResponseEntity<List<DDMDataEntryQuestionModel>> getQuestion(int formId, Principal auth,
			@RequestParam(name = "submissioId", required = false) Integer submissioId) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(districtDataManageService.getQuestion(formId, auth, submissioId));
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("Failed to get IRF question in collection command : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}
	
//	@PreAuthorize("hasAuthority('dataentry_HAVING_write')")
	@PostMapping("submitData")
	public ResponseEntity<ResponseModel> saveSubmitData(@RequestBody List<DDMQuestionModel> questionModels,
			Principal auth) {
		try {

			return ResponseEntity.status(HttpStatus.OK).body(districtDataManageService.saveSubmitData(questionModels, auth));

		} catch (Exception e) {
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	@GetMapping("sgetSubmissionId")
	@ResponseBody
	public Integer sgetSubmissionId( Principal auth) {
		try {
			return Integer.valueOf( districtDataManageService.getSubmissionId( auth));
		} catch (Exception e) {
			e.printStackTrace();
		return Integer.valueOf(0);
		}

	}
	
	@GetMapping("getDDMReportData")
//	 @ResponseBody
	public ResponseEntity<DDMReportModel> getDDMReportData( Principal auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(districtDataManageService.getDDMReportData( auth));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}
	
	
//	@PostMapping("exportSubmissionToPDF")
//	public ResponseEntity<InputStreamResource> getViewData(@RequestBody List<DDMDataEntryQuestionModel> dataEntryModels,
//			Principal auth, HttpServletResponse response, HttpServletRequest request) {
//		String filePath = "";
//		try {
//			filePath = districtDataManageService.generatePDF(dataEntryModels, auth, response, request);
//			File file = new File(filePath);
//
//			HttpHeaders respHeaders = new HttpHeaders();
//			respHeaders.add("Content-Disposition", "attachment; filename=" + file.getName());
//			InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
//
//			new File(filePath).delete();
//			return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		}

//	}
	
}
