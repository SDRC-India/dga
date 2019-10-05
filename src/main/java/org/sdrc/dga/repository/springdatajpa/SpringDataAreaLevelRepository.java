package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.AreaLevel;
import org.sdrc.dga.repository.AreaLevelRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAreaLevelRepository extends AreaLevelRepository, JpaRepository<AreaLevel, Integer>{

}
