/**
 * 
 */
package org.sdrc.dga.web;

import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.service.DistrictDataManageService;
import org.sdrc.dga.service.DistrictHealthActionPlanService;
import org.sdrc.dga.service.MasterRawDataService;
import org.sdrc.dga.service.ODKService;
import org.sdrc.dga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in), Pratyush(pratyush@sdrc.co.in)
 * This controller is not meant for the use in production enviroment .
 * All database update will be donw using this controller
 */

@Controller
@RequestMapping("database")
public class DatabaseManagementController {

	@Autowired
	private MasterRawDataService masterRawDataService;
	
	@Autowired
	private ODKService odkService;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private UserService userService;
	
//	
//	@GetMapping("insertXpathsUptoQuestionLevel")
//	@ResponseBody
//	String insertXpaths() throws Exception
//	{
//		return masterRawDataService.getAllFormXpathScoreMappingUptoQuestionLevel();
//	}
//	
//	
//	
	@GetMapping("generateXpath")
	@ResponseBody
	boolean generateXpath() throws Exception
	{
		return masterRawDataService.generateXpath();
	}
	
	
	@GetMapping("updateRawData")
	@ResponseBody
	boolean updateRawData() throws Exception
	{
		return masterRawDataService.persistRawData();
		
	}
	
	@GetMapping("/updateFacilityScore")
	@ResponseBody
	public boolean updateFacilityScore() throws Exception
	{
		return odkService.updateFacilityScore();
	}
	
//	@GetMapping("updateRawDataOfDataTree")
//	@ResponseBody
//	boolean updateRawDataOfDataTree() throws Exception
//	{
//		
//		return odkService.updateDataTreeData();
//	}
//	
//	@GetMapping("updateXformMapping")
//	@ResponseBody
//	boolean updateXformMapping() throws Exception
//	{
//		
//		return odkService.updateXformMapping();
//	}
//	
//	
//	@GetMapping("/insertCrossTabIndicator")
//	@ResponseBody
//	public boolean insertCrossTabIndicator() throws Exception
//	{
//		return masterRawDataService.insertCrossTabIndicatorXpath();
//	}
//	
//	
//	@GetMapping("/folderStructure")
//	@ResponseBody
//	public boolean folderStructure() throws Exception
//	{
//		return masterRawDataService.createFoldersOfImages();
//	}
//	
//	@GetMapping("/updateLatLong")
//	@ResponseBody
//	public boolean updateLatLong() throws Exception
//	{
//		return masterRawDataService.updateLatitudeLogitudeOfSubmission();
//	}
	
	@PostMapping(value="updatePassword")
	@ResponseBody
	boolean updatePassword(@RequestBody CollectUserModel collectUserModel,@RequestHeader("secret") String secret)throws Exception
	{
		if(secret!=null && secret.equalsIgnoreCase(messages.getMessage("secret.code", null,null)))
		return userService.updatePassword(collectUserModel);
		else
		return false;
	}
	
	@GetMapping(value="updateArea")
	@ResponseBody
	boolean updateArea()
	{
		try {
		return masterRawDataService.updateArea();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		}
//	
//	@GetMapping("insertUserTable")
//	@ResponseBody
//	boolean insertUserTable()
//	{
//		return userService.insertUserTable();
//	}
//	
	
//	http://localhost:8080/dgaindia/database/configureUserDatabase
	@GetMapping("configureUserDatabase")
	@ResponseBody
	boolean configureUserDatabase()
	{
		return userService.configureUserDatabase();
	}
	
	
//	
//	@GetMapping("configureRawDataXpath")
//	@ResponseBody
//	boolean configureRawDataXpath()
//	{
//		try {
//			return masterRawDataService.updateRawXpaths();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
//	@GetMapping("generateIndicatorMap")
//	@ResponseBody
//	boolean generateIndicatorMap() throws Exception
//	{
//		return masterRawDataService.generateIndicatorXpathMapping();
//		
//	}
	
	@Autowired
	private DistrictDataManageService districtDataManageService;
	
	@GetMapping("/configureQuestionTemplate")
	private boolean configureQuestionTemplate()
	{
		return districtDataManageService.configureIrfQuestionTemplate();
	}
	
}
