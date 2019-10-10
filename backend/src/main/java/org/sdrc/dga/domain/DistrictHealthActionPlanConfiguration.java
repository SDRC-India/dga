/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 09-Sep-2019 3:24:51 PM
 */
package org.sdrc.dga.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sdrc.dga.util.FacilityType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="DHAP_Configuration", uniqueConstraints=@UniqueConstraint(columnNames={"formId_fk","rawFormXapths","timeperiodId_fk","choiceDetailsLabel"}))
@Getter
@Setter
public class DistrictHealthActionPlanConfiguration {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false)
	private String sector;
	
	@Column(nullable=false)
	private String indicator;
	
	@Column(nullable=false)
	private FacilityType faciltyType;
	
	@Column(name="rawFormXapths")
	private String rawFormXapths;
	
	@ManyToOne
	@JoinColumn(name="formId_fk", nullable=false)
	private XForm form;
	
	@Column
	private String standard;
	
	@Column
	private String formula;
	
	@ManyToOne
	@JoinColumn(name="timeperiodId_fk", nullable=false)
	private TimePeriod timeperiod;

	@Column(name="choiceDetailsLabel")
	private Integer choiceDetailsLabel;
}
