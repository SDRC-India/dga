/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 10-Sep-2019 10:39:25 AM
 */
package org.sdrc.dga.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sdrc.dga.util.FacilityType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="DHAP_Aggregation")
@Getter
@Setter
public class DistrictHealthActionPlanAggregation {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="dhapConfigurationId_fk")
	private DistrictHealthActionPlanConfiguration dhapConfiguration;
	
	private String indicatorName;
	
	@Column
	private String sector;
	
	@OneToMany(mappedBy="dhapAggregation")
	private List<DistrictHealthActionPlanAggregationDetails> dhapAggregationDetails;
	
	private String requiredUnit;
	
	@Column
	private FacilityType faciltyType;
	
	@ManyToOne
	@JoinColumn(name="timeperiodId_fk", nullable=false)
	private TimePeriod timeperiod;
}
