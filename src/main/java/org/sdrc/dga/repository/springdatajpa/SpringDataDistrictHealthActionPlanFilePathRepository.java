/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 16-Sep-2019 10:20:19 AM
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DistrictHealthActionPlanFilePath;
import org.sdrc.dga.repository.DistrictHealthActionPlanFilePathRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataDistrictHealthActionPlanFilePathRepository
		extends DistrictHealthActionPlanFilePathRepository, Repository<DistrictHealthActionPlanFilePath, Integer> {

}
