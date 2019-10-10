package org.sdrc.dga.model;

import java.util.List;
/**
 * 
 * @author Harsh (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */

public class FeaturePermissionMappingModel {
	
	private int featurePermissionId;
	
	private FeatureModel feature;
	
	private PermissionModel permission;
	
	private String updatedDate;
	
	private String updatedBy;
	
	private List<RoleFeaturePermissionSchemeModel> roleFeaturePermissionSchemeModels;

	public int getFeaturePermissionId() {
		return featurePermissionId;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public List<RoleFeaturePermissionSchemeModel> getRoleFeaturePermissionSchemeModels() {
		return roleFeaturePermissionSchemeModels;
	}
	
	public void setRoleFeaturePermissionSchemeModels(
			List<RoleFeaturePermissionSchemeModel> roleFeaturePermissionSchemeModels) {
		this.roleFeaturePermissionSchemeModels = roleFeaturePermissionSchemeModels;
	}

	public void setFeaturePermissionId(int featurePermissionId) {
		this.featurePermissionId = featurePermissionId;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public FeatureModel getFeature() {
		return feature;
	}

	public PermissionModel getPermission() {
		return permission;
	}

	public void setFeature(FeatureModel feature) {
		this.feature = feature;
	}

	public void setPermission(PermissionModel permission) {
		this.permission = permission;
	}

}
