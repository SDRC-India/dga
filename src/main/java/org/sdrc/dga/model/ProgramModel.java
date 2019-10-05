package org.sdrc.dga.model;
/**
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class ProgramModel {

	private Integer programId;
	private String programName;
	private boolean isLive;
	
	public ProgramModel(Integer programId, String programName, boolean isLive) {
		super();
		this.programId = programId;
		this.programName = programName;
		this.isLive = isLive;
	}
	
	public Integer getProgramId() {
		return programId;
	}
	public void setProgramId(Integer programId) {
		this.programId = programId;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public ProgramModel() {
		super();
	}

}
