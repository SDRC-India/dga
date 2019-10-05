package org.sdrc.dga.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @author Rajanikanta Sahoo
 *
 */
@Entity
@Data
@Table(name="irf_question_option_type_mapping")
public class DDMQuestionOptionTypeMapping {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name="option_type_fk")
    private DDMOptionType optionType;


    @OneToOne
    @JoinColumn(name="question_id_fk")
    private DDMQuestion question;
}
