package org.sdrc.dga.model;

import java.util.List;

import lombok.Data;

@Data
public class TableDetails implements Cloneable{

	String districtName;
	String nameOfFacility;
	int serialNo;
	int id;
	List<ValueArray> valueArray;
}
