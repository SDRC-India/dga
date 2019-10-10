/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 10-Sep-2019 3:19:36 PM
 */
package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.DistrictHealthActionPlanAggregation;
import org.sdrc.dga.domain.DistrictHealthActionPlanConfiguration;
import org.springframework.transaction.annotation.Transactional;

public interface DistrictHealthActionPlanAggregationRepository {

//	@Transactional
//	<S extends DistrictHealthActionPlanAggregation> List<S> save(Iterable<S> dhaps);
	
	DistrictHealthActionPlanAggregation save(DistrictHealthActionPlanAggregation dhap);
	
	List<Object[]> getAggregatedData(String requiredScore, Integer formId, Integer timeperiodId, Integer xPathId, String formula);
	
	List<Object[]> getAggregatedDataForSumOfMultiple(String requiredScore, Integer formId, Integer timeperiodId, List<Integer> xPathIds);
	
	List<String> findByDistinctSector();
	
	List<DistrictHealthActionPlanAggregation> findByDhapConfigurationInOrderBySector(List<DistrictHealthActionPlanConfiguration> dhapc);
}
