package org.sdrc.dga.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.sdrc.dga.domain.DDMQuestion;
import org.springframework.data.repository.RepositoryDefinition;

//@RepositoryDefinition(domainClass = DDMQuestion.class, idClass = Integer.class)
public interface DDMQuestionRepository {

	List<DDMQuestion> findAll();

	@Transactional
	DDMQuestion save(DDMQuestion question);

//	List<Question> findByFormFormId(int formId);

	List<DDMQuestion> findByFormFormIdAndIsLiveTrue(int formId);

	DDMQuestion findByColumnn(String columnn);

	List<DDMQuestion> findByFormFormIdAndIsLiveTrueOrderByQuestionOrderAsc(int formId);

	DDMQuestion findByColumnnAndFormFormId(String dependentColumn, int numericCellValue);
}
