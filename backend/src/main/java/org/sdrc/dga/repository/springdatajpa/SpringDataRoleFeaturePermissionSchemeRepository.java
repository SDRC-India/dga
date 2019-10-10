/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import org.sdrc.dga.domain.RoleFeaturePermissionScheme;
import org.sdrc.dga.repository.RoleFeaturePermissionSchemeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * Created on 20-Nov-2018
 */
public interface SpringDataRoleFeaturePermissionSchemeRepository extends
RoleFeaturePermissionSchemeRepository,JpaRepository<RoleFeaturePermissionScheme, Integer> {
	
}
