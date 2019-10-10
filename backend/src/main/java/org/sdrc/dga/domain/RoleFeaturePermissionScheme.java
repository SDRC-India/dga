package org.sdrc.dga.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
public class RoleFeaturePermissionScheme implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="RoleFeaturePermissionSchemeId")
	private int roleFeaturePermissionSchemeId;
	
	@Column(name="SchemeName")
	private String schemeName;
	
	@ManyToOne
	@JoinColumn(name="RoleId")
	private Role role;
	
	@ManyToOne
	@JoinColumn(name="FeaturePermissionId")
	private FeaturePermissionMapping featurePermissionMapping;
	
	@Column(name="AreaCode")
	private String areaCode;
	
	@UpdateTimestamp
	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;
	
	@OneToMany(mappedBy="roleFeaturePermissionScheme", fetch=FetchType.EAGER)
	private List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings;
	
	@ManyToOne
	@JoinColumn(name="AreaId")
	private Area area;

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public int getRoleFeaturePermissionSchemeId() {
		return roleFeaturePermissionSchemeId;
	}

	public void setRoleFeaturePermissionSchemeId(int roleFeaturePermissionSchemeId) {
		this.roleFeaturePermissionSchemeId = roleFeaturePermissionSchemeId;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public FeaturePermissionMapping getFeaturePermissionMapping() {
		return featurePermissionMapping;
	}

	public void setFeaturePermissionMapping(FeaturePermissionMapping featurePermissionMapping) {
		this.featurePermissionMapping = featurePermissionMapping;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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

	public List<UserRoleFeaturePermissionMapping> getUserRoleFeaturePermissionMappings() {
		return userRoleFeaturePermissionMappings;
	}

	public void setUserRoleFeaturePermissionMappings(
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
		this.userRoleFeaturePermissionMappings = userRoleFeaturePermissionMappings;
	}

}
