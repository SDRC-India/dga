package org.sdrc.dga.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @author Rajanikanta sahoo
 *
 */

@Entity
@Data
@Table(name="ddm_option_type")
public class DDMOptionType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="option_type_id_pk")
	private int optionTypeId;
	
	
	@Column(nullable=false)
	private String optionTypeName;
	
	
	@Column(nullable=false)
	private byte isLive;
	
	@OneToMany(mappedBy="optionType")
	List<DDMOptions> options;
	
	public String toString() {
		return optionTypeName;
	}
}
