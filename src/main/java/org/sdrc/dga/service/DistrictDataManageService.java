package org.sdrc.dga.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.dga.model.DDMDataEntryQuestionModel;
import org.sdrc.dga.model.DDMQuestionModel;
import org.sdrc.dga.model.DDMReportModel;
import org.sdrc.dga.model.ResponseModel;

public interface DistrictDataManageService {

	public boolean configureIrfQuestionTemplate();
	
	public List<DDMDataEntryQuestionModel> getQuestion(int formId, Principal auth, Integer submissioId)throws Exception;
	
	public ResponseModel saveSubmitData(List<DDMQuestionModel> questionModel,Principal auth);
	
	public int getSubmissionId(Principal principal);
	
	public String generatePDF(List<DDMDataEntryQuestionModel> dataEntryModels, Principal auth,
			HttpServletResponse response, HttpServletRequest request)  throws Exception;
	
	public DDMReportModel getDDMReportData(Principal principal);
}
