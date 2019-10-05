package org.sdrc.dga.model;
/**
 * This class is responsible for finding all the forms whose media files need to update
 * @author Subhadarshani Patra (subhadarshani@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
public class MediaFilesToUpdate {
	
	private String xFormId;
	private String mediaFile;
	public String getMediaFile() {
		return mediaFile;
	}
	public void setMediaFile(String mediaFile) {
		this.mediaFile = mediaFile;
	}
	public String getxFormId() {
		return xFormId;
	}
	public void setxFormId(String xFormId) {
		this.xFormId = xFormId;
	}
	
	
}
