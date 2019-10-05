package org.sdrc.devinfo.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the user_access database table.
 * 
 */
@Entity
@Table(name = "user_access")
@NamedQuery(name = "UserAccess.findAll", query = "SELECT u FROM UserAccess u")
public class UserAccess implements Serializable {
	private static final long serialVersionUID = 1L;

	private String access_To;

	private String DB_Prefix;

	@Temporal(TemporalType.TIMESTAMP)
	private Date last_Login;

	@Temporal(TemporalType.TIMESTAMP)
	private Date last_Logout;
	@Id
	private BigDecimal user_Access_NId;
	
	private BigDecimal user_NId;

	public UserAccess() {
	}

	public String getAccess_To() {
		return this.access_To;
	}

	public void setAccess_To(String access_To) {
		this.access_To = access_To;
	}

	public String getDB_Prefix() {
		return this.DB_Prefix;
	}

	public void setDB_Prefix(String DB_Prefix) {
		this.DB_Prefix = DB_Prefix;
	}

	public Date getLast_Login() {
		return this.last_Login;
	}

	public void setLast_Login(Date last_Login) {
		this.last_Login = last_Login;
	}

	public Date getLast_Logout() {
		return this.last_Logout;
	}

	public void setLast_Logout(Date last_Logout) {
		this.last_Logout = last_Logout;
	}

	public BigDecimal getUser_Access_NId() {
		return this.user_Access_NId;
	}

	public void setUser_Access_NId(BigDecimal user_Access_NId) {
		this.user_Access_NId = user_Access_NId;
	}

	public BigDecimal getUser_NId() {
		return this.user_NId;
	}

	public void setUser_NId(BigDecimal user_NId) {
		this.user_NId = user_NId;
	}

}