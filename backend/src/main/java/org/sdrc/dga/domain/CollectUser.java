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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

/**
 * This is an Entity class, this represents users who will use the SDRC Coolect android app
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @author Sarita
 * @since version 1.0.0.0
 * 
 */

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"IsLive","Username"})})
public class CollectUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UserId")
	private Integer userId;
	
	@Column(name="Name", nullable = false)
	private String name;
	
	@Column(name="Username", nullable = false)
	private String username;
	
	@Column(name="Password", nullable = false)
	private String password;
	
	@Column(name="EmailId")
	private String emailId;
	
	@Column(name="IsLive", nullable = false)
	private boolean isLive;
	
	@Column(name="CreatedBy")
	private String createdBy;

	@CreatedDate
	@Column(name="CreatedDate")
	private Timestamp createdDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;

	@UpdateTimestamp
	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@OneToMany(mappedBy="collectUser")
	private List<User_Program_XForm_Mapping> user_Program_XForm_Mappings;
	
	@OneToMany(mappedBy="userDetail", fetch=FetchType.EAGER)
	private List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings;
	
	@OneToMany(mappedBy="user")
	private List<LastVisitData> lastVisitData;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<User_Program_XForm_Mapping> getUser_Program_XForm_Mappings() {
		return user_Program_XForm_Mappings;
	}

	public void setUser_Program_XForm_Mappings(
			List<User_Program_XForm_Mapping> user_Program_XForm_Mappings) {
		this.user_Program_XForm_Mappings = user_Program_XForm_Mappings;
	}

	public List<UserRoleFeaturePermissionMapping> getUserRoleFeaturePermissionMappings() {
		return userRoleFeaturePermissionMappings;
	}

	public void setUserRoleFeaturePermissionMappings(
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
		this.userRoleFeaturePermissionMappings = userRoleFeaturePermissionMappings;
	}

	public List<LastVisitData> getLastVisitData() {
		return lastVisitData;
	}

	public void setLastVisitData(List<LastVisitData> lastVisitData) {
		this.lastVisitData = lastVisitData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
