/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import java.util.List;

import org.sdrc.dga.domain.FacilityPlanning;
import org.sdrc.dga.repository.FacilityPlanningRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Harsh(harsh@sdrc.co.in)
 *
 */
public interface SpringDataFacilityPlanningRepository extends
		FacilityPlanningRepository,
		JpaRepository<FacilityPlanning, Integer> {
	
	@Override
	@Query("SELECT DISTINCT(fp.timeperiodNId) FROM FacilityPlanning fp")
	public List<Integer> findAllTimePeriodNids();

}
