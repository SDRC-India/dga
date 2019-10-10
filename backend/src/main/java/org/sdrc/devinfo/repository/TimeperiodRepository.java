package org.sdrc.devinfo.repository;

import java.util.List;

import org.sdrc.devinfo.domain.UtTimeperiod;
/**
 * @author Harsh
 * @since version 1.0.0.0
 *
 */
public interface TimeperiodRepository {
	
	List<UtTimeperiod> getAllTimePeriod();

	UtTimeperiod findByTimeperiodId(Integer timeperiodId);
	
	List<UtTimeperiod> findByTimePreriodNids(List<Integer> timePeriodNids);
}
