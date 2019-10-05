package org.sdrc.dga.service;

import java.io.FileNotFoundException;

import org.sdrc.dga.model.PostSubmissionModel;

/**
 * 
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */
public interface MasterRawDataService {

	/**
	 * This method will be used to read the xfrom excel and persist the
	 * corresponding xpath in Database
	 * 
	 * @return
	 * @throws Exception 
	 */
	public boolean generateXpath() throws Exception;

	/**
	 * it will perisit raw data for each xpath for each submission
	 * 
	 * @return
	 * @throws Exception 
	 */
	public boolean persistRawData() throws Exception;
	
	/**
	 * it will perisit raw data for each xpath for submission
	 * 
	 * @return
	 * @throws Exception 
	 */
	public boolean persistData(PostSubmissionModel postSubmissionModel) throws Exception;

/**
 * 
 * @return
 * @throws Exception
 */
	boolean updateRawDataPhase1() throws Exception;
	
		/**
		 * 
		 * @return
		 * @throws FileNotFoundException
		 * @throws Exception
		 */
	String getAllFormXpathScoreMappingUptoQuestionLevel() throws FileNotFoundException, Exception;
	
	/**
	 * This method will insert cross tab inidcators in database from the excel sheet
	 * @return
	 * @throws Exception
	 */
	
	boolean insertCrossTabIndicatorXpath() throws Exception;
	
	/**
	 * This method will keep all the images of submission in following folder structure
	 * Facility Type -> District -> Time Period -> images of all the facility submission except signatures
	 * @return
	 * @throws Exception
	 */
	boolean createFoldersOfImages() throws Exception;
	
	
	boolean updateLatitudeLogitudeOfSubmission();
	
	boolean updateArea() throws Exception;
	
	boolean updateRawXpaths() throws Exception;
	
//	boolean generateIndicatorXpathMapping() throws Exception;

}
