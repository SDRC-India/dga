/**
 * 
 * 
 * @author Harsh(harsh@sdrc.co.in)
 * 
 */
package org.sdrc.dga.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpiderDataCollection {
	
	private List<List<SpiderDataModel>> dataCollection;
	private List<Map<String,String>> tableData;
	
	public List<List<SpiderDataModel>> getDataCollection() {
		return dataCollection;
	}
	public void setDataCollection(List<List<SpiderDataModel>> dataCollection) {
		this.dataCollection = dataCollection;
	}
	public SpiderDataCollection(){
		dataCollection = new ArrayList<List<SpiderDataModel>>();
	}
	public List<Map<String, String>> getTableData() {
		return tableData;
	}
	public void setTableData(List<Map<String, String>> tableData) {
		this.tableData = tableData;
	}
	
	

}
