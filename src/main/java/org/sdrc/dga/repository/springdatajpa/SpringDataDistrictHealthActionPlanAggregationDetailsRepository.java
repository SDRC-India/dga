/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 10-Sep-2019 3:28:08 PM
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DistrictHealthActionPlanAggregationDetails;
import org.sdrc.dga.repository.DistrictHealthActionPlanAggregationDetailsRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataDistrictHealthActionPlanAggregationDetailsRepository
		extends DistrictHealthActionPlanAggregationDetailsRepository, Repository<DistrictHealthActionPlanAggregationDetails, Integer> {

}
