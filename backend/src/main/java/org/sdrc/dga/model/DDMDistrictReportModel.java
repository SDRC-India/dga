package org.sdrc.dga.model;

import java.util.List;

import lombok.Data;

@Data
public class DDMDistrictReportModel implements Cloneable{

	private String districtName;
	private String userName;
	private List<ActionItemModel> actionItems;
}
