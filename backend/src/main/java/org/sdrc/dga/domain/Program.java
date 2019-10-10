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

/**
 * This domain class or entity class will keep all the Programs.
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @author Sarita
 * @since version 1.0.0.0
 *
 */
@Entity
public class Program  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ProgramId")
	private int programId;
	
	@Column(name = "ProgramName", nullable = false, unique = true)
	private String programName;
	
	@Column(name="IsLive", nullable = false)
	private Boolean isLive;
	
	@Column(name="CreatedBy")
	private String createdBy;

	@Column(name="CreatedDate")
	private Timestamp createdDate;
	
	@Column(name="UpdatedBy")
	private String updatedBy;

	@Column(name="UpdatedDate")
	private Timestamp updatedDate;
	
	@OneToMany(mappedBy="program",fetch=FetchType.EAGER)
	private List<Program_XForm_Mapping> program_XForm_Mappings;
	

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
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

	public List<Program_XForm_Mapping> getProgram_XForm_Mappings() {
		return program_XForm_Mappings;
	}

	public void setProgram_XForm_Mappings(
			List<Program_XForm_Mapping> program_XForm_Mappings) {
		this.program_XForm_Mappings = program_XForm_Mappings;
	}

	@Override
	public String toString() {
		return "Program [programId=" + programId + ", programName=" + programName + ", isLive=" + isLive
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", program_XForm_Mappings=" + program_XForm_Mappings + "]";
	}
	
}
