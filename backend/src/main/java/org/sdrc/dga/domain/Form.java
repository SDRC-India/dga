package org.sdrc.dga.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;


/**
 * @author RajaniKanta Sahoo(rajanikanta@sdrc.co.in)
 */
@Entity
@Data
public class Form {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer formId;

	@Column(name = "name")
	private String name;

	@CreationTimestamp
	@Column(name = "created_date")
	private Date createdDate;
	
	private Integer formVersion; 
	
	public Form() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Form(int formId) {
		super();
		this.formId = formId;
	}

}
