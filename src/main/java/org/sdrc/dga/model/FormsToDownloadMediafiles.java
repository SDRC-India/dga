package org.sdrc.dga.model;
/**
 * This class is responsible for getting all the downloaded forms as  response from android
 * @author Subhadarshani Patra (subhadarshani@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */

public class FormsToDownloadMediafiles {
	String formId;
	String downloadOrUpdateDate;
		public String getFormId() {
		return formId;
		}

		public void setFormId(String formId) {
		this.formId = formId;
		}

		public String getDownloadOrUpdateDate() {
		return downloadOrUpdateDate;
		}

		public void setDownloadOrUpdateDate(String downloadOrUpdateDate) {
		this.downloadOrUpdateDate = downloadOrUpdateDate;
		}
}
