/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.FeaturePermissionMapping;
import org.sdrc.dga.repository.FeaturePermissionMappingRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * Created on 17-Nov-2018
 */

@RepositoryDefinition(domainClass=FeaturePermissionMapping.class,idClass=Integer.class)
public interface SpringDataFeaturePermissionMappingRepository extends FeaturePermissionMappingRepository {

}
