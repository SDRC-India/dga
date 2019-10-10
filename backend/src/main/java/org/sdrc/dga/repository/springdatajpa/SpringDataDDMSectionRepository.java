package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DDMSections;
import org.sdrc.dga.repository.DDMSectionRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass=DDMSections.class,idClass=Integer.class)
public interface SpringDataDDMSectionRepository extends DDMSectionRepository{

}
