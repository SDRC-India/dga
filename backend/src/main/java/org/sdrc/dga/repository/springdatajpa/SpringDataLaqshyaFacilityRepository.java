package org.sdrc.dga.repository.springdatajpa;


import org.sdrc.dga.domain.LaqshyaFacility;
import org.sdrc.dga.repository.LaqshyaFacilityRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass=LaqshyaFacility.class,idClass=Integer.class)
public interface SpringDataLaqshyaFacilityRepository extends LaqshyaFacilityRepository{

}
