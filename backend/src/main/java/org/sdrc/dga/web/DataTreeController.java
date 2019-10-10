package org.sdrc.dga.web;
/**
 * 
 * @author Harsh (harsh@sdrc.co.in)
 *
 */
import java.util.List;
import java.util.Map;

import org.sdrc.dga.model.BubbleDataModel;
import org.sdrc.dga.service.DataTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class DataTreeController {
	
	@Autowired 
	private DataTreeService dataTreeService;
	
	@PreAuthorize("hasAuthority('dataTree,View')")
	@GetMapping("/bubbleChartData")
	@ResponseBody
	public List<BubbleDataModel> getBubbleChartData(@RequestParam("sectorId")Integer sectorId,@RequestParam("areaId") int areaId,@RequestParam("timeperiodId") int timeperiodId)
	{
		return dataTreeService.getBubbleChartData(sectorId,areaId,timeperiodId);
	}
	
	@PreAuthorize("hasAuthority('dataTree,View')")
	@GetMapping("/treeData")
	@ResponseBody
	public Map<String, Object> fetchTreeData(@RequestParam("stateId")int stateId,@RequestParam("timePeriodId")int timePeriodId,@RequestParam("programId") int programId)
	{
		return dataTreeService.fetchTreeData(stateId,timePeriodId,programId);
	}
	
	@PreAuthorize("hasAuthority('dataTree,View')")
	@GetMapping("/forceLayoutData")
	@ResponseBody
	public Map<String, Object> forceLayoutData(@RequestParam("sectorId")Integer sectorId,@RequestParam("areaId")Integer areaId,@RequestParam("timeperiodId") int timeperiodId)
	{
		return dataTreeService.forceLayoutData(sectorId,areaId,timeperiodId);
	}
	@PreAuthorize("hasAuthority('dataTree,View')")
	@GetMapping("dataTree")
	public String dataTreePage()
	{
		return "dataTree";
	}
	

}
