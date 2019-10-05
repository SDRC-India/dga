package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.CollectUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Harsh(harsh@sdrc.co.in)
 *
 */
public interface CollectUserRepository {

	CollectUser findByUsernameAndIsLiveTrue(String userName);

	CollectUser findByUsernameAndPasswordAndIsLiveTrue(String username,
			String password);

	CollectUser findByUserId(Integer userId);
	
	CollectUser findByUsername(String userName);

	@Transactional
	CollectUser save(CollectUser collectUser);

	List<CollectUser> findByIsLiveTrue();
	
	List<CollectUser> findByRoleId(int roleId);
}
