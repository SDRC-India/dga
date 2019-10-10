package org.sdrc.dga.repository.springdatajpa;

import java.util.List;

import org.sdrc.dga.domain.User_Program_XForm_Mapping;
import org.sdrc.dga.repository.User_Program_XForm_MappingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
public interface SpringDataUser_Program_XForm_MappingRepository extends
		User_Program_XForm_MappingRepository,
		Repository<User_Program_XForm_Mapping, Integer> {
	@Override
	@Query("SELECT upxm FROM User_Program_XForm_Mapping upxm WHERE upxm.collectUser.username = :username"
			+ " AND upxm.isLive = True AND upxm.collectUser.isLive = True AND upxm.program_XForm_Mapping.isLive = True"
			+ " AND upxm.program_XForm_Mapping.program.isLive = True AND upxm.program_XForm_Mapping.xForm.isLive = True")
	List<User_Program_XForm_Mapping> findByUser(
			@Param("username") String username);

}
