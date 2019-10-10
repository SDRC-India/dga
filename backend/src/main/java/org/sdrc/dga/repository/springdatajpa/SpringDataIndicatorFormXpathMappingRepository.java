/**
 * 
 */
package org.sdrc.dga.repository.springdatajpa;

import java.util.List;

import org.sdrc.dga.domain.IndicatorFormXpathMapping;
import org.sdrc.dga.repository.IndicatorFormXpathMappingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@RepositoryDefinition(domainClass=IndicatorFormXpathMapping.class,idClass=Integer.class)
public interface SpringDataIndicatorFormXpathMappingRepository extends
		IndicatorFormXpathMappingRepository {

	
	@Override
	@Query("SELECT indi.indicatorFormXpathMappingId,indi.label,indi.type,indi.sector,indi.dhXpath"
			+ ",indi.chcXpath,indi.phcXpath,indi.hscXpath FROM IndicatorFormXpathMapping indi WHERE indi.indicatorFormXpathMappingId IN "
			+ " (SELECT MAX(indi1.indicatorFormXpathMappingId) FROM IndicatorFormXpathMapping indi1 GROUP BY indi1.label )"
			+ " ORDER BY indi.label ASC ")
	public List<Object []> findDistinctLabels();
	
}
