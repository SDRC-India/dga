package org.sdrc.devinfo.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte isAdmin;

	private byte isLoggedIn;

	private String user_Name;
	@Id
	private BigDecimal user_NId;

	private String user_PWD;

	public User() {
	}

	public byte getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(byte isAdmin) {
		this.isAdmin = isAdmin;
	}

	public byte getIsLoggedIn() {
		return this.isLoggedIn;
	}

	public void setIsLoggedIn(byte isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getUser_Name() {
		return this.user_Name;
	}

	public void setUser_Name(String user_Name) {
		this.user_Name = user_Name;
	}

	public BigDecimal getUser_NId() {
		return this.user_NId;
	}

	public void setUser_NId(BigDecimal user_NId) {
		this.user_NId = user_NId;
	}

	public String getUser_PWD() {
		return this.user_PWD;
	}

	public void setUser_PWD(String user_PWD) {
		this.user_PWD = user_PWD;
	}

}