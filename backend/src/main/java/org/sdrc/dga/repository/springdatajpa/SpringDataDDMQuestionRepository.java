package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DDMQuestion;
import org.sdrc.dga.repository.DDMQuestionRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = DDMQuestion.class, idClass = Integer.class)
public interface SpringDataDDMQuestionRepository extends DDMQuestionRepository{

}
