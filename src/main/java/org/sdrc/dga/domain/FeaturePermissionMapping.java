package org.sdrc.dga.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class FeaturePermissionMapping implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FeaturePermissionId")
	private int featurePermissionId;
	
	@ManyToOne
	@JoinColumn(name="FeatureId")
	private Feature feature;
	
	@ManyToOne
	@JoinColumn(name="PermissionId")
	private Permission permission;
	
	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;
	
	@OneToMany(mappedBy="featurePermissionMapping")
	private List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes;

	public int getFeaturePermissionId() {
		return featurePermissionId;
	}

	public void setFeaturePermissionId(int featurePermissionId) {
		this.featurePermissionId = featurePermissionId;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<RoleFeaturePermissionScheme> getRoleFeaturePermissionSchemes() {
		return roleFeaturePermissionSchemes;
	}

	public void setRoleFeaturePermissionSchemes(List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes) {
		this.roleFeaturePermissionSchemes = roleFeaturePermissionSchemes;
	}

}
