package org.sdrc.dga.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
public class UserRoleFeaturePermissionMapping implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UserRoleFeaturePermissionId")
	private int userRoleFeaturePermissionId;
	
	@ManyToOne
	@JoinColumn(name="RoleFeaturePermissionSchemeId")
	private RoleFeaturePermissionScheme roleFeaturePermissionScheme;
	
	@ManyToOne
	@JoinColumn(name="UserId")
	private CollectUser userDetail;
	
	@UpdateTimestamp
	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;

	public int getUserRoleFeaturePermissionId() {
		return userRoleFeaturePermissionId;
	}

	public void setUserRoleFeaturePermissionId(int userRoleFeaturePermissionId) {
		this.userRoleFeaturePermissionId = userRoleFeaturePermissionId;
	}

	public RoleFeaturePermissionScheme getRoleFeaturePermissionScheme() {
		return roleFeaturePermissionScheme;
	}

	public void setRoleFeaturePermissionScheme(RoleFeaturePermissionScheme roleFeaturePermissionScheme) {
		this.roleFeaturePermissionScheme = roleFeaturePermissionScheme;
	}

	public CollectUser getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(CollectUser userDetail) {
		this.userDetail = userDetail;
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

}
