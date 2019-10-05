package org.sdrc.dga.model;

import java.util.List;

import lombok.Data;

@Data
public class LaqshyaDatas implements Cloneable{

	String[] headers;
	List<TableDetails> tableDetails;
}
