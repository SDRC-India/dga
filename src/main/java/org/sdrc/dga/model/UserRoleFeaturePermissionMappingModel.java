package org.sdrc.dga.model;

/**
 * 
 * @author Harsh (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */

public class UserRoleFeaturePermissionMappingModel {

	private int userRoleFeaturePermissionId;
	
	private RoleFeaturePermissionSchemeModel roleFeaturePermissionSchemeModel;
	
	private CollectUserModel userDetailModel;
	
	private String updatedDate;
	
	private String updatedBy;

	public int getUserRoleFeaturePermissionId() {
		return userRoleFeaturePermissionId;
	}

	public RoleFeaturePermissionSchemeModel getRoleFeaturePermissionSchemeModel() {
		return roleFeaturePermissionSchemeModel;
	}

	public CollectUserModel getUserDetailModel() {
		return userDetailModel;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUserRoleFeaturePermissionId(int userRoleFeaturePermissionId) {
		this.userRoleFeaturePermissionId = userRoleFeaturePermissionId;
	}

	public void setRoleFeaturePermissionSchemeModel(RoleFeaturePermissionSchemeModel roleFeaturePermissionSchemeModel) {
		this.roleFeaturePermissionSchemeModel = roleFeaturePermissionSchemeModel;
	}

	public void setUserDetailModel(CollectUserModel userDetailModel) {
		this.userDetailModel = userDetailModel;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	
}
