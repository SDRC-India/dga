package org.sdrc.dga.model;

import java.util.List;

import lombok.Data;
/**
 * 
 * @author Rajanikanta Sahoo
 *
 */
@Data
public class DDMReportModel implements Cloneable{

	private String districtName;
	private List<ActionItemModel> actionItems;
}
