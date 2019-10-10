/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.RawFormXapths;
import org.sdrc.dga.repository.RawFormXapthsRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush
 *
 */
@RepositoryDefinition(domainClass=RawFormXapths.class,idClass=Integer.class)
public interface SpringDataRawFormXapthsRepository extends
		RawFormXapthsRepository {

}
