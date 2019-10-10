package org.sdrc.devinfo.repository.springdatajpa;

import java.util.List;

import org.sdrc.devinfo.domain.UtIndicatorClassificationsEn;
import org.sdrc.devinfo.repository.IndicatorClassificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DevInfoIndicatorClassificationRepositorySpringData extends JpaRepository<UtIndicatorClassificationsEn, Integer>,IndicatorClassificationRepository{
	
@Override
@Query("SELECT ic from UtIndicatorClassificationsEn ic where ic.IC_Type='SC' ")
public List<UtIndicatorClassificationsEn> getAllSectors();

@Override
@Query("SELECT ic from UtIndicatorClassificationsEn ic where ic.IC_Type='SR' AND ic.IC_Parent_NId=-1")
public List<UtIndicatorClassificationsEn> getAllPrograms();
	
}
