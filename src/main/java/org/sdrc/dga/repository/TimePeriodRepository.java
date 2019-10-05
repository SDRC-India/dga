package org.sdrc.dga.repository;

import java.util.List;
import java.util.Set;

import org.sdrc.dga.domain.TimePeriod;

/**
 * 
 * @author Harsh Pratyush
 *
 */
public interface TimePeriodRepository {
	
	List<TimePeriod> findAll();

	TimePeriod findByTimePeriodId(int timeperiodId);

	List<TimePeriod> findByOrderByStartDateDesc();

	List<TimePeriod> findByOrderByTimePeriodIdAsc();

	Set<String> findTimePeriods();

	List<TimePeriod> findByOrderByTimePeriodIdDesc();
	
	TimePeriod findTop1ByOrderByTimePeriodIdDesc();

}
