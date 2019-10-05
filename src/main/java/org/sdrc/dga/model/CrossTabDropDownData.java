package org.sdrc.dga.model;

import java.util.List;

/**
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class CrossTabDropDownData {

	private List<IndicatorFormXpathMappingModel> indicatorFormXpathMappingModels;
	
	private List<AreaModel> areaList;
	
	private List<TimePeriodModel>  timePeriodModels;
	
	
	private List<FormXpathScoreMappingModel> formXpathScoreMappingModels;

	public List<IndicatorFormXpathMappingModel> getIndicatorFormXpathMappingModels() {
		return indicatorFormXpathMappingModels;
	}

	public void setIndicatorFormXpathMappingModels(
			List<IndicatorFormXpathMappingModel> indicatorFormXpathMappingModels) {
		this.indicatorFormXpathMappingModels = indicatorFormXpathMappingModels;
	}

	public List<AreaModel> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<AreaModel> areaList) {
		this.areaList = areaList;
	}

	public List<TimePeriodModel> getTimePeriodModels() {
		return timePeriodModels;
	}

	public void setTimePeriodModels(List<TimePeriodModel> timePeriodModels) {
		this.timePeriodModels = timePeriodModels;
	}

	public List<FormXpathScoreMappingModel> getFormXpathScoreMappingModels() {
		return formXpathScoreMappingModels;
	}

	public void setFormXpathScoreMappingModels(
			List<FormXpathScoreMappingModel> formXpathScoreMappingModels) {
		this.formXpathScoreMappingModels = formXpathScoreMappingModels;
	}
	
	
	
}
