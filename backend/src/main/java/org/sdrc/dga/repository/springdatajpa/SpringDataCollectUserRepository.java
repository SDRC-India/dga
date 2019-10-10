/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import java.util.List;

import org.sdrc.dga.domain.CollectUser;
import org.sdrc.dga.repository.CollectUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Harsh(harsh@sdrc.co.in)
 *
 */
public interface SpringDataCollectUserRepository extends
		JpaRepository<CollectUser, Integer>, CollectUserRepository {

	@Query(value="SELECT * FROM CollectUser where userId in (SELECT DISTINCT UserId "
			+ "FROM UserRoleFeaturePermissionMapping where RoleFeaturePermissionSchemeId "
			+ "in(SELECT RoleFeaturePermissionSchemeId FROM RoleFeaturePermissionScheme where RoleId = :roleId))", nativeQuery=true)
	List<CollectUser> findByRoleId(@Param("roleId") int roleId);
}
