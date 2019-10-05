package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DDMSubmitionData;
import org.sdrc.dga.repository.DDMSubmitionDataRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = DDMSubmitionData.class, idClass = Integer.class)
public interface SpringDataDDMSubmitionDataRepository extends DDMSubmitionDataRepository{

}
