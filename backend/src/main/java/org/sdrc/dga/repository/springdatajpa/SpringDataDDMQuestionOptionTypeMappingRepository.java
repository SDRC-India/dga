package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DDMQuestionOptionTypeMapping;
import org.sdrc.dga.repository.DDMQuestionOptionTypeMappingRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass=DDMQuestionOptionTypeMapping.class,idClass=Integer.class)
public interface SpringDataDDMQuestionOptionTypeMappingRepository extends DDMQuestionOptionTypeMappingRepository{

}
