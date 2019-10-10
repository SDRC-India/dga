/**
 * 
 */
package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.Role;

/**
 * 
 * @author Harsh Pratyush(harsh@sdrc.co.in), Pratyush(pratyush@sdrc.co.in)
 * Created on 17-Nov-2018
 */
public interface RoleRepository {

	List<Role> findAll();
	
	List<Role> findAllByRoleIdNotIn(List<Integer> roldIds);
}
