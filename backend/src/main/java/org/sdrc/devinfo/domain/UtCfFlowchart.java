package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_cf_flowchart database table.
 * 
 */
@Entity
@Table(name="ut_cf_flowchart")
@NamedQuery(name="UtCfFlowchart.findAll", query="SELECT u FROM UtCfFlowchart u")
public class UtCfFlowchart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int CF_FlowChart_NId;

	@Lob
	private String CF_FlowChart;

	public UtCfFlowchart() {
	}

	public int getCF_FlowChart_NId() {
		return this.CF_FlowChart_NId;
	}

	public void setCF_FlowChart_NId(int CF_FlowChart_NId) {
		this.CF_FlowChart_NId = CF_FlowChart_NId;
	}

	public String getCF_FlowChart() {
		return this.CF_FlowChart;
	}

	public void setCF_FlowChart(String CF_FlowChart) {
		this.CF_FlowChart = CF_FlowChart;
	}

}