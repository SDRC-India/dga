package org.sdrc.dga.model;

import lombok.Data;

@Data
public class ActionItemModel implements Cloneable{

	int SlNo;
	String FacilityType;
	String Facility;
	String ActionItem;
	String  Status;
	String Remark;
	String District;
}
