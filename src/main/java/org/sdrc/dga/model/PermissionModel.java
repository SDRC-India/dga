package org.sdrc.dga.model;
/**
 * 
 * @author Harsh (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */

import java.util.List;

public class PermissionModel {

	private int permissionId;
	
	private String permissionCode;
	
	private String permissionName;
	
	private String description;
	
	private String updatedDate;
	
	private String updatedBy;
	
	private List<FeaturePermissionMappingModel> featurePermissionMappings;

	public int getPermissionId() {
		return permissionId;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public String getPermissionName() {
		return permissionName;
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

	public List<FeaturePermissionMappingModel> getFeaturePermissionMappings() {
		return featurePermissionMappings;
	}
	
	public void setFeaturePermissionMappings(List<FeaturePermissionMappingModel> featurePermissionMappings) {
		this.featurePermissionMappings = featurePermissionMappings;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
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

}
