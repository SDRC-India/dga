package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.LaqshyaData;
import org.sdrc.dga.repository.LaqshyaDataRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass=LaqshyaData.class,idClass=Integer.class)
public interface SpringDataLaqshyaDataRepository extends LaqshyaDataRepository{

	
}
