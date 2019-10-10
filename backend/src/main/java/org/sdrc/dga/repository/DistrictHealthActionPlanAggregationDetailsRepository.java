/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 10-Sep-2019 3:26:24 PM
 */
package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.DistrictHealthActionPlanAggregationDetails;
import org.springframework.transaction.annotation.Transactional;

public interface DistrictHealthActionPlanAggregationDetailsRepository {

	@Transactional
	<S extends DistrictHealthActionPlanAggregationDetails> List<S> save(Iterable<S> dhapadtls);
	
	List<DistrictHealthActionPlanAggregationDetails> findByDhapAggregationIdAndDistrict(Integer parentId, String district);
}
