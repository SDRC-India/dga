package org.sdrc.devinfo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ut_area_map_layer database table.
 * 
 */
@Entity
@Table(name="ut_area_map_layer")
@NamedQuery(name="UtAreaMapLayer.findAll", query="SELECT u FROM UtAreaMapLayer u")
public class UtAreaMapLayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int layer_NId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date end_Date;

	@Lob
	private byte[] layer_dbf;

	@Lob
	private byte[] layer_Shp;

	@Lob
	private byte[] layer_Shx;

	private String layer_Size;

	private int layer_Type;

	private double maxX;

	private double maxY;

	private int metadata_NId;

	private double minX;

	private double minY;

	@Temporal(TemporalType.TIMESTAMP)
	private Date start_Date;

	@Temporal(TemporalType.TIMESTAMP)
	private Date update_Timestamp;

	public UtAreaMapLayer() {
	}

	public int getLayer_NId() {
		return this.layer_NId;
	}

	public void setLayer_NId(int layer_NId) {
		this.layer_NId = layer_NId;
	}

	public Date getEnd_Date() {
		return this.end_Date;
	}

	public void setEnd_Date(Date end_Date) {
		this.end_Date = end_Date;
	}

	public byte[] getLayer_dbf() {
		return this.layer_dbf;
	}

	public void setLayer_dbf(byte[] layer_dbf) {
		this.layer_dbf = layer_dbf;
	}

	public byte[] getLayer_Shp() {
		return this.layer_Shp;
	}

	public void setLayer_Shp(byte[] layer_Shp) {
		this.layer_Shp = layer_Shp;
	}

	public byte[] getLayer_Shx() {
		return this.layer_Shx;
	}

	public void setLayer_Shx(byte[] layer_Shx) {
		this.layer_Shx = layer_Shx;
	}

	public String getLayer_Size() {
		return this.layer_Size;
	}

	public void setLayer_Size(String layer_Size) {
		this.layer_Size = layer_Size;
	}

	public int getLayer_Type() {
		return this.layer_Type;
	}

	public void setLayer_Type(int layer_Type) {
		this.layer_Type = layer_Type;
	}

	public double getMaxX() {
		return this.maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return this.maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public int getMetadata_NId() {
		return this.metadata_NId;
	}

	public void setMetadata_NId(int metadata_NId) {
		this.metadata_NId = metadata_NId;
	}

	public double getMinX() {
		return this.minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return this.minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public Date getStart_Date() {
		return this.start_Date;
	}

	public void setStart_Date(Date start_Date) {
		this.start_Date = start_Date;
	}

	public Date getUpdate_Timestamp() {
		return this.update_Timestamp;
	}

	public void setUpdate_Timestamp(Date update_Timestamp) {
		this.update_Timestamp = update_Timestamp;
	}

}