package org.sdrc.dga.model;
/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class UserProgramXFormModel {
	
	private Integer userId;
	private String name;
	private String username;
	private String password;
	private String email;
	private String[] programs;
	private String[] xForms;
	private boolean isLive;
	private boolean isAttached;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String[] getPrograms() {
		return programs;
	}
	public void setPrograms(String[] programs) {
		this.programs = programs;
	}
	public String[] getxForms() {
		return xForms;
	}
	public void setxForms(String[] xForms) {
		this.xForms = xForms;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public boolean isAttached() {
		return isAttached;
	}
	public void setAttached(boolean isAttached) {
		this.isAttached = isAttached;
	}
	
}
