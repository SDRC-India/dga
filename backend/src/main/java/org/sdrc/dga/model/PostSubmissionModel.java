package org.sdrc.dga.model;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * This model class will keep the properties which we need after submission of a particular form
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * @since version 1.0.0.0 
 */
public class PostSubmissionModel {
	
	private String instanceId;
	private Map<String, List<File>> formFilesMap;
	private CollectUserModel collectUserModel;
	private XFormModel xFormModel;
	private List<String> toEmailIds;
	private LastVisitDataModel lastVisitDataModel;
	private int userProgramXFormMappingId;
	private String submissionFileString;
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public Map<String, List<File>> getFormFilesMap() {
		return formFilesMap;
	}
	public void setFormFilesMap(Map<String, List<File>> formFilesMap) {
		this.formFilesMap = formFilesMap;
	}
	public CollectUserModel getCollectUserModel() {
		return collectUserModel;
	}
	public void setCollectUserModel(CollectUserModel collectUserModel) {
		this.collectUserModel = collectUserModel;
	}
	public XFormModel getxFormModel() {
		return xFormModel;
	}
	public void setxFormModel(XFormModel xFormModel) {
		this.xFormModel = xFormModel;
	}
	public List<String> getToEmailIds() {
		return toEmailIds;
	}
	public void setToEmailIds(List<String> toEmailIds) {
		this.toEmailIds = toEmailIds;
	}
	public LastVisitDataModel getLastVisitDataModel() {
		return lastVisitDataModel;
	}
	public void setLastVisitDataModel(LastVisitDataModel lastVisitDataModel) {
		this.lastVisitDataModel = lastVisitDataModel;
	}
	public int getUserProgramXFormMappingId() {
		return userProgramXFormMappingId;
	}
	public void setUserProgramXFormMappingId(int userProgramXFormMappingId) {
		this.userProgramXFormMappingId = userProgramXFormMappingId;
	}
	public String getSubmissionFileString() {
		return submissionFileString;
	}
	public void setSubmissionFileString(String submissionFileString) {
		this.submissionFileString = submissionFileString;
	}	
	
}
