package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.DDMActionItem;
import org.sdrc.dga.repository.DDMActionItemRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass=DDMActionItem.class,idClass=Integer.class)
public interface SpringDataDDMActionItemRepository extends DDMActionItemRepository{

}
