/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 10-Sep-2019 2:54:12 PM
 */
package org.sdrc.dga.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.sdrc.dga.domain.TimePeriod;
import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.repository.TimePeriodRepository;
import org.sdrc.dga.service.DistrictHealthActionPlanService;
import org.sdrc.dga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistrictHealthActionPlanController {

	@Autowired
	DistrictHealthActionPlanService districtHealthActionPlanService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	TimePeriodRepository timePeriodRepository;
	
	//http://localhost:8080/dgaindia/database/insertIntoDHAPConfiguration
	@GetMapping("database/insertIntoDHAPConfiguration")
	@ResponseBody
	private boolean insertIntoDHAPConfiguratiion(){
		return districtHealthActionPlanService.insertIntoDHAPConfiguratiion();
	}
	
	//http://localhost:8080/dgaindia/database/aggregateDistrictHealthActionPlan
	@GetMapping("database/aggregateDistrictHealthActionPlan")
	@ResponseBody
	public boolean aggregateDistrictHealthActionPlan(){
		
		return districtHealthActionPlanService.aggregateDistrictHealthActionPlan();
	}
	
	//http://localhost:8080/dgaindia/database/exportDistrictHealthActionPlanExcel?timeperiodId=3&districtId=3
	@GetMapping("database/exportDistrictHealthActionPlanExcel")
	@ResponseBody
	public boolean exportDistrictHealthActionPlanExcel(@RequestParam("timeperiodId") int timeperiodId, int districtId){
		
		return districtHealthActionPlanService.exportDistrictHealthActionPlanExcel(timeperiodId, districtId);
	}
	
	@GetMapping("database/getAllAreaForDHAP")
	@ResponseBody
	public Map<String, List<AreaModel>> getArea() {
		return userService.getAllAreaList();
	}
	
	@GetMapping("database/getAllTimeperiodForDHAP")
	@ResponseBody
	public List<TimePeriod> getLatestTimeperiod(){
		List<TimePeriod> timePeriods = new ArrayList<>();
		timePeriods.add(timePeriodRepository.findTop1ByOrderByTimePeriodIdDesc());
		return timePeriods;
	}
	
	//http://localhost:8080/dgaindia/database/exportDHAPExcel?timeperiodId=3&districtId=24
	@GetMapping("database/exportDHAPExcel")
	@ResponseBody
	public JSONObject exportDHAPExcel(@RequestParam("timeperiodId") int timeperiodId, int districtId){
		return districtHealthActionPlanService.exportDHAPExcel(timeperiodId, districtId);
	}
	
	@PostMapping("database/downloadDHAP")
	public void downLoad(@RequestParam("fileName") String name, HttpServletResponse response) throws IOException {

		String fileName = name.replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("%2C", ",")
				.replaceAll("\\+", " ").replaceAll("%20", " ").replaceAll("%26", "&").replaceAll("%5C", "/");
		try(InputStream inputStream = new FileInputStream(fileName)) {
			
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", new java.io.File(fileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("application/octet-stream");
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			outputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
//			log.error("error-while downloading raw data report with payload : {}", name, e);
		}
	}
}
