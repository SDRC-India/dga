/**
 * 
 */
package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.FeaturePermissionMapping;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * Created on 17-Nov-2018
 */
public interface FeaturePermissionMappingRepository {

	List<FeaturePermissionMapping> findAll();

}
