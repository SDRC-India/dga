package org.sdrc.dga.thread;

import org.sdrc.dga.model.PostSubmissionModel;
import org.sdrc.dga.service.SubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This thread will do all the job that needs to be done after submission of a
 * particular form
 * 
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * @since version 1.0.0.0
 */
@Component
@Scope("prototype")
public class PostSubmissionThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(PostSubmissionThread.class);
	
	private PostSubmissionModel postSubmissionModel;

	public PostSubmissionModel getPostSubmissionModel() {
		return postSubmissionModel;
	}

	public void setPostSubmissionModel(PostSubmissionModel postSubmissionModel) {
		this.postSubmissionModel = postSubmissionModel;
	}

	@Autowired
	private SubmissionService submissionService;

	@Override
	public void run() {

		try {
			logger.info("IN POST SUBMISSION THREAD...");
			submissionService.postSubmissionWork(getPostSubmissionModel());
			logger.info("POST SUBMISSION THREAD COMPLETED...");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR : Exception while calling postSubmissionWork ", e);
		}
		

	}

}
