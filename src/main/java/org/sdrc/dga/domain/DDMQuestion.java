package org.sdrc.dga.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;

import org.hibernate.annotations.CreationTimestamp;


import lombok.Data;
/**
 * 
 * @author Rajanikanta Sahoo
 *
 */
@Entity
@Data
@Table(name="ddm_question")
public class DDMQuestion {

	@Id
    @Column(name = "question_id_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;

    @Column(nullable = false)
    private String questionNameId;

    @Column(length = 700)
    private String questionName;

    @Column(nullable = false)
    private int questionOrder;

    @Column(nullable = false)
    private String columnn;

    @Column(nullable = false)
    private boolean isLive;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = false)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    private String updatedBy;

    @Column(nullable = false)
    private String controlType;

    @Column(nullable = false)
    private String inputType;

    @ManyToOne
    @JoinColumn(name = "section_id_fk", nullable = false)
    private DDMSections section;

    @ManyToOne
    @JoinColumn(name = "form_id_fk", nullable = false)
    private Form form;

    @Column(nullable = false)
    private byte reviewHeader;

    private String reviewName;

    @Column(name = "dependecy", nullable = false)
    private Boolean dependecy;

   @Column(name = "dependent_column")

    private String dependentColumn;

    @Column(name = "dependent_condition", length = 700)
    private String dependentCondition;

    @OneToOne(mappedBy = "question")
    private DDMQuestionOptionTypeMapping questionOptionTypeMapping;
    
    
    private String fileExtensions;
    
    
    private String constraints;
    
    @Column(nullable=false)
	private byte saveMandatory ;

    @Column(nullable=false)
	private byte finalizeMandatory ;
    
    @Column(nullable=false)
    private byte approvalProcess;
    
    
    private String groupId;
    
    
    @Column(nullable=false)
    private byte isTriggable;
    
    
    @Column(length=700)
    private String features;
    
    @Column(length=700)
    private String defaultSetting;
    
    @Column(length=700)
    private String placeHolder;
    
    
    private String constraintString;
    
    private String questionSerial;
}
