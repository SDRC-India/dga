package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DDMOptionType;
import org.sdrc.dga.repository.DDMOptionRepository;
import org.sdrc.dga.repository.DDMOptionTypeRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass=DDMOptionType.class,idClass=Integer.class)
public interface SpringDataDDMOptionTypeRepository extends DDMOptionTypeRepository{

}
