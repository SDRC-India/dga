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

import lombok.Data;
/**
 * 
 * @author Rajanikanta Sahoo
 *
 */
@Entity
@Data
@Table(name="ddm_section")
public class DDMSections {

	@Id
    @Column(name = "section_id_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sectionId;

    @Column(name = "section_name", nullable = false, length = 700)
    private String sectionName;

    @Column(nullable = false)
    private byte isLive;
    
    @ManyToOne
    @JoinColumn(name = "form_id_fk", nullable = false)
    private Form form;
    
	@Column(nullable = false)
    private int sectionOrder;

    @JoinColumn
    @ManyToOne
    private DDMSections parentSection;
    
 
	
}
