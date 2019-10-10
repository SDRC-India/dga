package org.sdrc.dga.util;

import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.AreaLevel;
import org.sdrc.dga.domain.LastVisitData;
import org.sdrc.dga.domain.Program;
import org.sdrc.dga.domain.XForm;
import org.sdrc.dga.model.AreaLevelModel;
import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.LastVisitDataModel;
import org.sdrc.dga.model.ProgramModel;
import org.sdrc.dga.model.XFormModel;
import org.springframework.stereotype.Component;

@Component
public class DomainToModelConverter {
	
	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param areaLevel The Entity
	 * @return AreaLevelModel The Model
	 */
	public AreaLevelModel toAreaLevelModel(AreaLevel areaLevel) {
		
		AreaLevelModel areaLevelModel = new AreaLevelModel();
		
		areaLevelModel.setAreaLevelId(areaLevel.getAreaLevelId());
		areaLevelModel.setAreaLevelName(areaLevel.getAreaLevelName());
		
		return areaLevelModel;
	}

	/**
	 * 
	 * @param xForm
	 * @return
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 */
	public XFormModel toXFormModel(XForm xForm) {
		
		XFormModel xFormModel = new XFormModel();
		
		xFormModel.setAreaLevelModel(toAreaLevelModel(xForm.getAreaLevel()));
		xFormModel.setAreaXPath(xForm.getAreaXPath());
		xFormModel.setDateOfVisitXPath(xForm.getDateOfVisitXPath());
		xFormModel.setFormId(xForm.getFormId());
		xFormModel.setLive(xForm.isLive());
		xFormModel.setOdkServerURL(xForm.getOdkServerURL());
		xFormModel.setPassword(xForm.getPassword());
		xFormModel.setSecondaryAreaXPath(xForm.getSecondaryAreaXPath());
		xFormModel.setLocationXPath(xForm.getLocationXPath());
		xFormModel.setUsername(xForm.getUsername());
		xFormModel.setxFormId(xForm.getxFormId());
		xFormModel.setToEmailIdsXPath(xForm.getToEmailIdsXPath());
		xFormModel.setCcEmailIds(xForm.getCcEmailIds());
		xFormModel.setxFormTitle(xForm.getxFormIdTitle());
		xFormModel.setSendRawData(xForm.isSendRawData());
		xFormModel.setRootElement(xForm.getxFormId());
		xFormModel.setXform_meta_id(xForm.getXform_meta_id());
		xFormModel.setTimeperiodId(xForm.getTimePeriod().getTimePeriodId());
		return xFormModel;
	}

	/**
	 * 
	 * @param area
	 * @return
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 */
	public AreaModel toAreaModel(Area area){
		
		AreaModel areaModel = new AreaModel();
		if(area != null){
			areaModel.setAreaId(area.getAreaId());
			areaModel.setAreaCode(area.getAreaCode());
			areaModel.setAreaLevelId(area.getAreaLevel().getAreaLevelId());
			areaModel.setAreaName(area.getAreaName());
			areaModel.setParentAreaId(area.getParentAreaId());
			return areaModel;
		}
		return null;
		
	}

	public ProgramModel toProgramModel(Program program) {
		
		
		ProgramModel programModel = new ProgramModel();
		
		programModel.setLive(program.getIsLive());
		programModel.setProgramId(program.getProgramId());
		programModel.setProgramName(program.getProgramName());
		
		
		return programModel;
	}
	
	public LastVisitDataModel toLastVisitDataModel(LastVisitData lastVisitData) {
		
		LastVisitDataModel lastVisitDataModel = new LastVisitDataModel();
		
		if(lastVisitData != null){
			lastVisitDataModel.setLastVisitDataId(lastVisitData.getLastVisitDataId());
			lastVisitDataModel.setAreaModel(toAreaModel(lastVisitData.getArea()));
			return lastVisitDataModel;
		}
		return null;
	}
	
			
}
