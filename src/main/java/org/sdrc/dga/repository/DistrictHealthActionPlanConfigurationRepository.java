/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 09-Sep-2019 6:07:05 PM
 */
package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.DistrictHealthActionPlanConfiguration;

public interface DistrictHealthActionPlanConfigurationRepository {

	void save(DistrictHealthActionPlanConfiguration dhapc);
	
	List<DistrictHealthActionPlanConfiguration> findByFormFormIdAndTimeperiodTimePeriodId(int formId, int timeperiodId);
	
	List<DistrictHealthActionPlanConfiguration> findByTimeperiodTimePeriodId(int timeperiodId);
}
