package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_sdmxuser database table.
 * 
 */
@Entity
@Table(name="ut_sdmxuser")
@NamedQuery(name="UtSdmxuser.findAll", query="SELECT u FROM UtSdmxuser u")
public class UtSdmxuser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int sender_NId;

	private String contactName;

	private String department;

	private String email;

	private String fax;

	private String id;

	private byte isSender;

	private String name;

	private String role;

	private String telephone;

	public UtSdmxuser() {
	}

	public int getSender_NId() {
		return this.sender_NId;
	}

	public void setSender_NId(int sender_NId) {
		this.sender_NId = sender_NId;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getIsSender() {
		return this.isSender;
	}

	public void setIsSender(byte isSender) {
		this.isSender = isSender;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}