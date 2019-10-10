/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 16-Sep-2019 10:18:43 AM
 */
package org.sdrc.dga.repository;

import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.DistrictHealthActionPlanFilePath;
import org.sdrc.dga.domain.TimePeriod;

public interface DistrictHealthActionPlanFilePathRepository {

	void save(DistrictHealthActionPlanFilePath dhapFilePath);
	
	DistrictHealthActionPlanFilePath findByAreaAndTimePeriod(Area area, TimePeriod timeperiod);
}
