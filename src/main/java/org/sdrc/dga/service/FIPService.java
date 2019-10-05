/**
 * 
 */
package org.sdrc.dga.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.sdrc.dga.model.FipDistrict;
import org.sdrc.dga.model.XFormModel;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface FIPService {
	

	/**
	 * 	 * This method will generated Excel sheet FIP report and return the generated file path
	 * @param areaCode
	 * @param formMetaId
	 * @throws IOException 
	 * @return
	 */
	public String generateFIP(String areaCode, int formMetaId,int stateId) throws IOException;
	

	/**
	 * This method will return the forms for the state
	 * @param stateId
	 * @return
	 */
	public Map<String,List<XFormModel>> getFacilityFormADistrict(Integer stateId);
	
	public List<FipDistrict> getFipDistrict(int stateId); 
}
