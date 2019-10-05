package org.sdrc.dga.model;

import java.util.List;
/**
 * 
 * @author Harsh (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */

public class FeatureModel {

	private int featureId;
	
	private String featureCode;
	
	private String featureName;
	
	private String description;
	
	private String updatedDate;
	
	private String updatedBy;
	
	private List<FeaturePermissionMappingModel> featurePermissionMappings;

	public int getFeatureId() {
		return featureId;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public String getFeatureName() {
		return featureName;
	}

	public String getDescription() {
		return description;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<FeaturePermissionMappingModel> getFeaturePermissionMappings() {
		return featurePermissionMappings;
	}

	public void setFeaturePermissionMappings(List<FeaturePermissionMappingModel> featurePermissionMappings) {
		this.featurePermissionMappings = featurePermissionMappings;
	}
}
