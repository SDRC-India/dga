/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 10-Sep-2019 1:09:57 PM
 */
package org.sdrc.dga.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="DHAP_Aggregation_Details")
@Getter
@Setter
public class DistrictHealthActionPlanAggregationDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="dhapAggregation_id_fk")
	private DistrictHealthActionPlanAggregation dhapAggregation;
	
	@Column
	private String district;
	
	@Column
	private String block;
	
	@Column
	private String facility;
	
	@Column
	private int unit;
}
