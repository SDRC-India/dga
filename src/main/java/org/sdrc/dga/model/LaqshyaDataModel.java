package org.sdrc.dga.model;

import lombok.Data;

/**
 * 
 * @author Rajanikanta Sahoo
 *
 */
@Data
public class LaqshyaDataModel implements Cloneable{

	int id;
	String districtName;
	String facilityName;
	String facilityType;
	
	String totalScorePeer;
	
	String totalScoreBaseline;
	
	String serviceProvisionPeer;
	
	String serviceProvisionBaseline;
	
	String patientRightPeer;
	
	String patientRightBaseline;
	
	String inputPeer;
	
	String inputBaseline;
	
	String supportServicesPeer;
	
	String supportServicesBaseline;
	
	String clinicalServicesPeer;
	
	String clinicalServicesBaseline;
	
	String infectionControlPeer;
	
	String infectionControlBaseline;
	
	String qualityManagementPeer;
	
	String qualityManagementBaseline;
	
	String OutcomePeer;
	
	String OutcomeBaseline;
	
	String stateAssesmentDonePeer;
	
	String stateAssesmentDoneBaseline;
	
	String stateCertificationLaborRoomPeer;
	
	String stateCertificationLaborRoomBaseline;
	
	String stateCertificationOTPeer;
	
	String stateCertificationOTBaseline;
	
	String nationalCertificationLaborRoomPeer;
	
	String nationalCertificationLaborRoomBaseline;
	
	String nationalCertificationOTPeer;
	
	String nationalCertificationOTBaseline;
}
