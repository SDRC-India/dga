/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 09-Sep-2019 6:10:54 PM
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DistrictHealthActionPlanConfiguration;
import org.sdrc.dga.repository.DistrictHealthActionPlanConfigurationRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataDistrictHealthActionPlanConfigurationRepository
		extends DistrictHealthActionPlanConfigurationRepository, Repository<DistrictHealthActionPlanConfiguration, Integer> {

}
