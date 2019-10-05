package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.DDMOptionType;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * 
 * @author Rajanikanta Sahoo
 *
 */
//@RepositoryDefinition(domainClass=DDMOptionType.class,idClass=Integer.class)
public interface DDMOptionTypeRepository {

DDMOptionType save(DDMOptionType ooptionType);
	
	List<DDMOptionType> findAll();
}
