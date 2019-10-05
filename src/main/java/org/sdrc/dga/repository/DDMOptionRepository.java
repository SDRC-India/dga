package org.sdrc.dga.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.sdrc.dga.domain.DDMOptions;
import org.springframework.data.repository.RepositoryDefinition;

//@RepositoryDefinition(domainClass = DDMOptions.class, idClass = Integer.class)
public interface DDMOptionRepository {

	List<DDMOptions> save(List<DDMOptions> options);

	@Transactional
	DDMOptions save(DDMOptions optionObject);

	/**
	 * @param cciTypeId
	 * @return
	 */
	List<DDMOptions> findByOptionTypeOptionTypeId(int cciTypeId);
	
	List<DDMOptions> findAll();
}
