package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.RawFormXapths;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Harsh Pratyush
 *
 */
public interface RawFormXapthsRepository {

	@Transactional
	void save(RawFormXapths rawFormXapths);

	List<RawFormXapths> findAll();

	List<RawFormXapths> findByFormFormId(Integer formId);

	List<RawFormXapths> findByFormFormIdAndTypeIgnoreCaseIn(
			Integer formId, List<String> xpathTypes);
	
	RawFormXapths findByLabelAndFormFormId(String label, Integer formId);

}
