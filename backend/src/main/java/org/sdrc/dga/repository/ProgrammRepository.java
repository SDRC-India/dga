/**
 * 
 */
package org.sdrc.dga.repository;

import org.sdrc.dga.domain.Program;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface ProgrammRepository {

	Program findByProgramId(int i);
	
	Program findByProgramName(String name);

}
