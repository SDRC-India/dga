package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.DDMSections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

//@RepositoryDefinition(domainClass=DDMSections.class,idClass=Integer.class)
public interface DDMSectionRepository {

	List<DDMSections> findAll();

	DDMSections save(DDMSections section);
}
