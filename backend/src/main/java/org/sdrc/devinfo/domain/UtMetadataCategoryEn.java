package org.sdrc.devinfo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ut_metadata_category_en database table.
 * 
 */
@Entity
@Table(name="ut_metadata_category_en")
@NamedQuery(name="UtMetadataCategoryEn.findAll", query="SELECT u FROM UtMetadataCategoryEn u")
public class UtMetadataCategoryEn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int categoryNId;

	private String categoryDescription;

	private String categoryGId;

	private String categoryName;

	private int categoryOrder;

	private String categoryType;

	private byte isMandatory;

	private byte isPresentational;

	private int parentCategoryNId;

	public UtMetadataCategoryEn() {
	}

	public int getCategoryNId() {
		return this.categoryNId;
	}

	public void setCategoryNId(int categoryNId) {
		this.categoryNId = categoryNId;
	}

	public String getCategoryDescription() {
		return this.categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public String getCategoryGId() {
		return this.categoryGId;
	}

	public void setCategoryGId(String categoryGId) {
		this.categoryGId = categoryGId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategoryOrder() {
		return this.categoryOrder;
	}

	public void setCategoryOrder(int categoryOrder) {
		this.categoryOrder = categoryOrder;
	}

	public String getCategoryType() {
		return this.categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public byte getIsMandatory() {
		return this.isMandatory;
	}

	public void setIsMandatory(byte isMandatory) {
		this.isMandatory = isMandatory;
	}

	public byte getIsPresentational() {
		return this.isPresentational;
	}

	public void setIsPresentational(byte isPresentational) {
		this.isPresentational = isPresentational;
	}

	public int getParentCategoryNId() {
		return this.parentCategoryNId;
	}

	public void setParentCategoryNId(int parentCategoryNId) {
		this.parentCategoryNId = parentCategoryNId;
	}

}