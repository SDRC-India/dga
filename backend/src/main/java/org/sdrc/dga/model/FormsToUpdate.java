package org.sdrc.dga.model;
/**
 * This class is responsible for receving the userstring and formsToDownloadMediaFileList from Android
 * @author Subhadarshani Patra (subhadarshani@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
import java.util.List;

public class FormsToUpdate {
	private List<FormsToDownloadMediafiles> formsToDownloadMediafiles;
	private String userString;

	public String getUserString() {
		return userString;
	}

	public void setUserString(String userString) {
		this.userString = userString;
	}

	public List<FormsToDownloadMediafiles> getFormsToDownloadMediafiles() {
		return formsToDownloadMediafiles;
	}

	public void setFormsToDownloadMediafiles(
			List<FormsToDownloadMediafiles> formsToDownloadMediafiles) {
		this.formsToDownloadMediafiles = formsToDownloadMediafiles;
	}
	
	
}
