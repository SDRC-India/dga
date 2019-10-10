package org.sdrc.dga.service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.sdrc.dga.model.LaqshyaDataModel;
import org.sdrc.dga.model.LaqshyaDatas;
import org.sdrc.dga.model.ResponseModel;

public interface LaqshyaService {

	public boolean configureLaqshyaFacility() throws Exception;
	
	public LaqshyaDatas getLaqshyaData(Principal auth);
//	public LaqshyaDatas getLaqshyaData();
	
	public ResponseModel saveLaqshyaData(LaqshyaDatas laqshyaModel,Principal auth);
//	public ResponseModel saveLaqshyaData();
	
	public String getLaqshyaReport(Principal auth) throws IOException;
	public String getLaqshyaReport() throws IOException;
	
}
