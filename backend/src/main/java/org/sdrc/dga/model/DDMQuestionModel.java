package org.sdrc.dga.model;

import java.util.List;
/**
 * 
 * @author Rajanikanta Sahoo
 *
 */

public class DDMQuestionModel implements Cloneable{

	private Boolean allChecked = false;
	private String columnName;
	private String controlType;
	private String[] dependentCondition;
	private String[] fileExtension;
	private String fileExtensionValidationMessage;
	private Double fileSize;
	private Object[] fileValues;
	private int key;
	private String label;
	private String maxDate;
	private int maxLength;
	private String minDate;
	private int minLength;
	private Boolean multiple = false;
	private List<DDMOptionModel> options;
	private Object optionsParentColumn;
	private String pattern;
	private Boolean required = false;
	private Boolean selectAllOption = false;
	private String type;
	private boolean disabled = false;
	private boolean triggable = false;
	private String[] parentColumns;
	private List<List<DDMQuestionModel>> childQuestionModels;
	private String groupParentId;
	private String indexNumberTrack;
	private String serialNumb;
	private Object[] deletedFileValue;
	private String siNo;
	private String currentDate;
	private String placeHolder;
	private boolean removable =true; 
	private boolean infoAvailable=false;
	private String infoMessage;
	
	
	

	
	

	/**
	 * @return the infoAvailable
	 */
	public boolean isInfoAvailable() {
		return infoAvailable;
	}

	/**
	 * @return the infoMessage
	 */
	public String getInfoMessage() {
		return infoMessage;
	}

	/**
	 * @param infoAvailable the infoAvailable to set
	 */
	public void setInfoAvailable(boolean infoAvailable) {
		this.infoAvailable = infoAvailable;
	}

	/**
	 * @param infoMessage the infoMessage to set
	 */
	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	/**
	 * @return the placeHolder
	 */
	public String getPlaceHolder() {
		return placeHolder;
	}

	/**
	 * @param placeHolder the placeHolder to set
	 */
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	public String getSiNo() {
		return siNo;
	}

	public void setSiNo(String siNo) {
		this.siNo = siNo;
	}

	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	public String getIndexNumberTrack() {
		return indexNumberTrack;
	}

	public void setIndexNumberTrack(String indexNumberTrack) {
		this.indexNumberTrack = indexNumberTrack;
	}

	public String getGroupParentId() {
		return groupParentId;
	}

	public void setGroupParentId(String groupParentId) {
		this.groupParentId = groupParentId;
	}

	public String[] getParentColumns() {
		return parentColumns;
	}

	public void setParentColumns(String[] parentColumns) {
		this.parentColumns = parentColumns;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isTriggable() {
		return triggable;
	}

	public void setTriggable(boolean triggable) {
		this.triggable = triggable;
	}

	public Boolean getAllChecked() {
		return allChecked;
	}

	public void setAllChecked(Boolean allChecked) {
		this.allChecked = allChecked;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String[] getDependentCondition() {
		return dependentCondition;
	}

	public void setDependentCondition(String[] dependentCondition) {
		this.dependentCondition = dependentCondition;
	}

	public String[] getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String[] fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFileExtensionValidationMessage() {
		return fileExtensionValidationMessage;
	}

	public void setFileExtensionValidationMessage(String fileExtensionValidationMessage) {
		this.fileExtensionValidationMessage = fileExtensionValidationMessage;
	}

	public Double getFileSize() {
		return fileSize;
	}

	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}

	public Object[] getFileValues() {
		return fileValues;
	}

	public void setFileValues(Object[] fileValues) {
		this.fileValues = fileValues;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getMinDate() {
		return minDate;
	}

	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public Boolean getMultiple() {
		return multiple;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

	public List<DDMOptionModel> getOptions() {
		return options;
	}

	public void setOptions(List<DDMOptionModel> options) {
		this.options = options;
	}

	public Object getOptionsParentColumn() {
		return optionsParentColumn;
	}

	public void setOptionsParentColumn(Object optionsParentColumn) {
		this.optionsParentColumn = optionsParentColumn;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getSelectAllOption() {
		return selectAllOption;
	}

	public void setSelectAllOption(Boolean selectAllOption) {
		this.selectAllOption = selectAllOption;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	private Object value;

	public List<List<DDMQuestionModel>> getChildQuestionModels() {
		return childQuestionModels;
	}

	public void setChildQuestionModels(List<List<DDMQuestionModel>> childQuestionModels) {
		this.childQuestionModels = childQuestionModels;
	}

	public String getSerialNumb() {
		return serialNumb;
	}

	public void setSerialNumb(String serialNumb) {
		this.serialNumb = serialNumb;
	}

	public Object[] getDeletedFileValue() {
		return deletedFileValue;
	}

	public void setDeletedFileValue(Object[] deletedFileValue) {
		this.deletedFileValue = deletedFileValue;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * @return the removable
	 */
	public boolean isRemovable() {
		return removable;
	}

	/**
	 * @param removable the removable to set
	 */
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}
}
