package org.sdrc.dga.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.sdrc.dga.domain.DDMActionItem;
import org.sdrc.dga.domain.DDMSubmitionData;
import org.springframework.data.repository.RepositoryDefinition;

//@RepositoryDefinition(domainClass=DDMActionItem.class,idClass=Integer.class)
public interface DDMActionItemRepository {

//	@Transactional
	void deleteByActionItemId(int serviceId);

	@Transactional
	<S extends DDMActionItem> List<S> save(Iterable<S> services);
	
	List<DDMActionItem> findByDdmSubmitionData(DDMSubmitionData submitionData);
}
