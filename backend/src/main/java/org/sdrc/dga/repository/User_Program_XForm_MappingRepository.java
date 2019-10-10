package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.User_Program_XForm_Mapping;
import org.springframework.transaction.annotation.Transactional;

/**
 * This repository will help us to get data from User Program-XForm mapping
 * table.
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
public interface User_Program_XForm_MappingRepository {

	/**
	 * This method will take the user id and give all the forms and programs
	 * assigned to the user.
	 * 
	 * @param username
	 *            username of the Collect Android app user
	 * @param password
	 *            password of the Collect Android app user
	 * @return List<org.sdrc.dga.domain.User_Program_XForm_Mapping>
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @since version 1.0.0.0
	 */

	List<User_Program_XForm_Mapping> findByUser(String username);

	@Transactional
	void save(User_Program_XForm_Mapping user_Program_XForm_Mapping);

}
