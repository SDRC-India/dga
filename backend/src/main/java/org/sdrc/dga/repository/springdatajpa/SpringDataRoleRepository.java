/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.Role;
import org.sdrc.dga.repository.RoleRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * Created on 17-Nov-2018
 */

@RepositoryDefinition(domainClass=Role.class,idClass=Integer.class)
public interface SpringDataRoleRepository extends RoleRepository {

}
