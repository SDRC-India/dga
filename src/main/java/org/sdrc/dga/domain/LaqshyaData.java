package org.sdrc.dga.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class LaqshyaData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	String districtName;
	 
	@ManyToOne
	@JoinColumn(name="districtId")
	private Area districtId;
	
	@ManyToOne
	@JoinColumn(name="laqshyaFacilityTypeId")
	private LaqshyaFacilityType facilityType;
	
	String facilityName;
	
//	@ManyToOne
//	@JoinColumn(name="laqshyaFacilityId")
//	LaqshyaFacility facility;
	
	
	
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
	
	boolean isLive;
}
