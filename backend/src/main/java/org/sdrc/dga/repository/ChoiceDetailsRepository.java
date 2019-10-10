/**
 * 
 */
package org.sdrc.dga.repository;

import java.util.List;
import java.util.Set;

import org.sdrc.dga.domain.ChoicesDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface ChoiceDetailsRepository {

	@Transactional
	void save(ChoicesDetails choicesDetails);

	List<ChoicesDetails> findAll();

	List<ChoicesDetails> findByFormFormId(Integer formId);

	List<ChoicesDetails> findByFormFormIdAndLabel(Integer formId, String blockName);

	/**
	 * This method will return the unique number of choices for some set of choice
	 * xpaths
	 * 
	 * @param xPathsRow
	 * @param formId 
	 * @return
	 */
	Set<String> findByXpathIdAndType(List<String> xPathsRow, int formId);

}
