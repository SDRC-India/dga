/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.dga.repository.UserRoleFeaturePermissionMappingRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * Created on 17-Nov-2018
 */

@RepositoryDefinition(domainClass=UserRoleFeaturePermissionMapping.class,idClass=Integer.class)
public interface SpringDataUserRoleFeaturePermissionMappingRepository
		extends UserRoleFeaturePermissionMappingRepository {

}
