package org.sdrc.dga.repository;

import javax.transaction.Transactional;

import org.sdrc.dga.domain.DDMQuestionOptionTypeMapping;
import org.springframework.data.repository.RepositoryDefinition;

//@RepositoryDefinition(domainClass=DDMQuestionOptionTypeMapping.class,idClass=Integer.class)
public interface DDMQuestionOptionTypeMappingRepository {

	@Transactional
	DDMQuestionOptionTypeMapping save(DDMQuestionOptionTypeMapping questionOptionTypeMapping);
}
