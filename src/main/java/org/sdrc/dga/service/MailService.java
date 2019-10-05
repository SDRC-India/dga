package org.sdrc.dga.service;

import org.sdrc.dga.model.Mail;


/**
 * 
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 *
 */
public interface MailService {
	
	
	String sendMail(Mail mail) throws Exception;
	
}
