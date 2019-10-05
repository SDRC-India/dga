package org.sdrc.dga.service;

import java.util.List;
import java.util.Map;

import org.sdrc.dga.model.BubbleDataModel;

/**
 * 
 * @author Harsh(harsh@sdrc.co.in)
 *
 */

public interface DataTreeService 
{

	/**
	 * Retruns the bubble datas
	 * @param sectorId
	 * @param areaId 
	 * @param timeperiodId 
	 * @return
	 */
	List<BubbleDataModel> getBubbleChartData(Integer sectorId, int areaId, int timeperiodId);

	/**
	 * Tree data i.e. the sectors
	 * @param timePeriodId 
	 * @param stateId 
	 * @return
	 */
	Map<String, Object> fetchTreeData(int stateId, int timePeriodId,int programId);

	/**
	 * return the data for a particular LVD
	 * @param sectorId
	 * @param areaId
	 * @param timeperiodId 
	 * @return
	 */
	Map<String, Object> forceLayoutData(Integer sectorId, Integer areaId, int timeperiodId);
}
