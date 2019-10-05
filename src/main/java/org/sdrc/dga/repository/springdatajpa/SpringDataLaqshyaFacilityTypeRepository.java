package org.sdrc.dga.repository.springdatajpa;


import org.sdrc.dga.domain.LaqshyaFacilityType;
import org.sdrc.dga.repository.LaqshyaFacilityTypeRepository;
import org.springframework.data.repository.RepositoryDefinition;


@RepositoryDefinition(domainClass=LaqshyaFacilityType.class,idClass=Integer.class)
public interface SpringDataLaqshyaFacilityTypeRepository extends LaqshyaFacilityTypeRepository{

}
