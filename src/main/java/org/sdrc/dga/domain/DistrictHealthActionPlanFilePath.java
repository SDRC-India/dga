/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 16-Sep-2019 10:09:28 AM
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
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="DHAP_FilePath", uniqueConstraints=@UniqueConstraint(columnNames={"areaId_fk","timePeriodId_fk","filePath"}))
public class DistrictHealthActionPlanFilePath {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="areaId_fk")
	private Area area;
	
	@ManyToOne
	@JoinColumn(name="timePeriodId_fk")
	private TimePeriod timePeriod;
	
	@Column
	private String filePath;
}
