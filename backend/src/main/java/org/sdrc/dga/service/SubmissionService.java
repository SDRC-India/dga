/**
 * 
 */
package org.sdrc.dga.service;

import javax.servlet.http.HttpServletRequest;

import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.model.PostSubmissionModel;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface SubmissionService {

	/**
	 * 
	 * This following method will do all the upload work 
	 * @return an in t value, if it is zero then the upload is successful
	 * @param request, the request object from client request
	 * @param deviceID, the client mobile IMEI number
	 * @param username, the clients username
	 * @author Harsh Pratyush(harsh@sdrc.co.in) on 17-Nov-2017 4:59:15 pm
	 */
	int uploadForm(HttpServletRequest request, String deviceID, CollectUserModel collectUserModel);
	
	/**
	 * This method will be called from PostSubmissionThread to do job which
	 * needs to be done after form submission
	 * 
	 * @param postSubmissionModel
	 *            The thread will pass this argument to this method which
	 *            contains form details which recently got submitted
	* @author Harsh Pratyush(harsh@sdrc.co.in) on 17-Nov-2017 4:59:15 pm
	 */
	void postSubmissionWork(PostSubmissionModel postSubmissionModel);
	
	/**
	 * This method will be responsible for sending notification to user with excel attachment file
	 * 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
	 * @param postSubmissionModel The data which we need while doing the notification work after submission of a form
	 */

	void postSubmissionNotificationWork(PostSubmissionModel postSubmissionModel);


}
