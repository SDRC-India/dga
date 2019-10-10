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
import javax.persistence.OneToOne;

/**
 * This domain class or entity class represents the mapping of Programs and XForms.
 * Meaning, which xform belongs to which program or with in which program which xForms are there
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @author Sarita
 * @since version 1.0.0.0
 *
 */


@Entity
public class Program_XForm_Mapping implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ProgramXFormMapping_Id")
	private int programXFormMappingId;
	
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
	
	@ManyToOne
	@JoinColumn(name="ProgramId")
	private Program program;
	
	@OneToOne
	@JoinColumn(name="FormId")
	private XForm xForm;
	
	@OneToMany(mappedBy="program_XForm_Mapping")
	private List<User_Program_XForm_Mapping> user_Program_XForm_Mappings;

	public int getProgramXFormMappingId() {
		return programXFormMappingId;
	}

	public void setProgramXFormMappingId(int programXFormMappingId) {
		this.programXFormMappingId = programXFormMappingId;
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

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public XForm getxForm() {
		return xForm;
	}

	public void setxForm(XForm xForm) {
		this.xForm = xForm;
	}

	public List<User_Program_XForm_Mapping> getUser_Program_XForm_Mappings() {
		return user_Program_XForm_Mappings;
	}

	public void setUser_Program_XForm_Mappings(
			List<User_Program_XForm_Mapping> user_Program_XForm_Mappings) {
		this.user_Program_XForm_Mappings = user_Program_XForm_Mappings;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
