package org.sdrc.dga.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @author Rajanikanta Sahoo
 *
 */

@Entity
@Data
@Table(name="ddm_options")
public class DDMOptions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="option_id_pk")
	private int optionId;
	
	
	@Column(nullable=false)
	private String optionName;
	
	@Column(nullable=false)
	private int optionOrder;
	
	
	@Column(nullable=false)
	private byte isLive;
	
	@ManyToOne
	@JoinColumn(name="parent_key")
	private DDMOptions parentKey;
	
	@ManyToOne
	@JoinColumn(name="option_type_fk")
	private DDMOptionType optionType;

	public DDMOptions(int optionId) {
		super();
		this.optionId = optionId;
	}
	public DDMOptions() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String toString() {
		return optionName;
	}
}
