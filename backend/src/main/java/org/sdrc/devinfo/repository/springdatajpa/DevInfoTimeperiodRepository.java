package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtTimeperiod;
import org.sdrc.devinfo.repository.TimeperiodRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DevInfoTimeperiodRepository extends TimeperiodRepository,
		JpaRepository<UtTimeperiod, Integer> {
	@Override
	@Query("SELECT tp from UtTimeperiod tp")
	public List<UtTimeperiod> getAllTimePeriod();
	
	@Override
	@Query("SELECT tp from UtTimeperiod tp where tp.timePeriod_NId=:timeperiodId")
	public UtTimeperiod findByTimeperiodId(@Param("timeperiodId")Integer timeperiodId);
	
	@Override
	@Query("SELECT tp from UtTimeperiod tp where tp.timePeriod_NId in :timePeriodNids ")
	public List<UtTimeperiod> findByTimePreriodNids(@Param("timePeriodNids")
			List<Integer> timePeriodNids);

}
