package org.sdrc.dga.model;

import java.util.List;

/**
 * This model class will keep information of Programs and XForms..
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */
public class ProgramXFormsModel {

	private ProgramModel programModel;
	private List<XFormModel> xFormsModel;

	public ProgramModel getProgramModel() {
		return programModel;
	}

	public void setProgramModel(ProgramModel programModel) {
		this.programModel = programModel;
	}

	public List<XFormModel> getxFormsModel() {
		return xFormsModel;
	}

	public void setxFormsModel(List<XFormModel> xFormsModel) {
		this.xFormsModel = xFormsModel;
	}

}
