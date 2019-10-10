/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.Program;
import org.sdrc.dga.repository.ProgrammRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass=Program.class,idClass=Integer.class)
public interface SpringDataProgrammRepository extends ProgrammRepository {
}
