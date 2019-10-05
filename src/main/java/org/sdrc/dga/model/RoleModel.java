package org.sdrc.dga.model;
/**
 * 
 * @author Harsh (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */

import java.util.List;

public class RoleModel {

	private int roleId;
	
	private String roleCode;
	
	private String roleName;
	
	private String description;
	
	private String updatedDate;
	
	private String updatedBy;
	
	private List<RoleFeaturePermissionSchemeModel> roleFeaturePermissionSchemes;

	public int getRoleId() {
		return roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public String getRoleName() {
		return roleName;
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

	public List<RoleFeaturePermissionSchemeModel> getRoleFeaturePermissionSchemes() {
		return roleFeaturePermissionSchemes;
	}
	
	public void setRoleFeaturePermissionSchemes(List<RoleFeaturePermissionSchemeModel> roleFeaturePermissionSchemes) {
		this.roleFeaturePermissionSchemes = roleFeaturePermissionSchemes;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
