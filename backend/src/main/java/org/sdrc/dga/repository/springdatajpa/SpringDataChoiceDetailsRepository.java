/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import java.util.List;
import java.util.Set;

import org.sdrc.dga.domain.ChoicesDetails;
import org.sdrc.dga.repository.ChoiceDetailsRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@RepositoryDefinition(domainClass=ChoicesDetails.class,idClass=Integer.class)
public interface SpringDataChoiceDetailsRepository extends
		ChoiceDetailsRepository {
	
	
	@Override
	@Query(value="select distinct choicesdet0_.choiceValue as col_0_0_ from ChoicesDetails choicesdet0_ "
			+ "cross join RawFormXpaths rawformxap1_ where "
			+ "choicesdet0_.formId=:formId and "
			+ "(rawformxap1_.xpath in :xPathsRow) "
			+ "and 'select_one '+choicesdet0_.choicName=rawformxap1_.type order by choicesdet0_.choiceValue DESC",nativeQuery=true)
	public Set<String> findByXpathIdAndType(@Param("xPathsRow")List<String> xPathsRow, @Param("formId") int formId);

}
