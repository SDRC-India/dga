package org.sdrc.dga.model;

import java.util.List;
/**
 * This class is responsible for collecting programXFormModelList and MediaFilesToUpdateList
 * @author Subhadarshani Patra (subhadarshani@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
public class ModelToCollectApplication {
	
	private List<ProgramXFormsModel> programXFormModelList;
	private List<MediaFilesToUpdate> listOfMediaFilesToUpdate;

	public List<ProgramXFormsModel> getProgramXFormModelList() {
		return programXFormModelList;
	}
	public void setProgramXFormModelList(
			List<ProgramXFormsModel> programXFormModelList) {
		this.programXFormModelList = programXFormModelList;
	}
	public List<MediaFilesToUpdate> getListOfMediaFilesToUpdate() {
		return listOfMediaFilesToUpdate;
	}
	public void setListOfMediaFilesToUpdate(
			List<MediaFilesToUpdate> listOfMediaFilesToUpdate) {
		this.listOfMediaFilesToUpdate = listOfMediaFilesToUpdate;
	}
	
}
