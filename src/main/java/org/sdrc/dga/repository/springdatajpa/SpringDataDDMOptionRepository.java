package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DDMOptions;
import org.sdrc.dga.repository.DDMOptionRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = DDMOptions.class, idClass = Integer.class)
public interface SpringDataDDMOptionRepository extends DDMOptionRepository{

}
