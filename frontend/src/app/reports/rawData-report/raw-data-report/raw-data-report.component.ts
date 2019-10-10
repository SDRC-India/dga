import { Component, OnInit } from '@angular/core';
import { FipFacilityModel } from 'src/app/models/FipFacilityModel';
import { FipAreaLevelModel } from 'src/app/models/FipAreaLevelModel';
import { AreaModel } from 'src/app/models/AreaModel';
import { FipService } from '../../api/fip.service';
import { HttpXsrfTokenExtractor, HttpClient } from '@angular/common/http';
import { FipDistrictFacilityModel } from 'src/app/models/FipDistrictFacilityModel';
import { Constants } from 'src/app/models/constants';
import { TimePeriod } from 'src/app/models/TimePeriodModel';
import savesAs from "save-as";
import { throwError } from 'rxjs';
import { filter, map, mergeMap,catchError } from 'rxjs/operators';


declare var $: any;
@Component({
  selector: 'sdrc-raw-data-report',
  templateUrl: './raw-data-report.component.html',
  styleUrls: ['./raw-data-report.component.scss']
})
export class RawDataReportComponent implements OnInit {
  exportData: any;
	allFacilities: any = [];
	allStates = [];
	allDistricts: any = [];
	selectedFacility: FipFacilityModel = new FipFacilityModel();
	facilityList: any;
	selectedArea: FipAreaLevelModel = new FipAreaLevelModel();
  	selectedFacilityType: FipDistrictFacilityModel = new FipDistrictFacilityModel();;
	selectedDistrictFacility: any = [];
  	selectedTimePeriod: TimePeriod = new TimePeriod();
  	allTimePeriods = [];
	fipDistricts: any = [];
	backTimePeriod=[];
	

	errorMsg: string;
	selectedState: AreaModel = new AreaModel();

	selectedProgram;
	programList = [];
  constructor(private fipService: FipService,private httpClient: HttpClient, private httpXsrfTokenExtractor: HttpXsrfTokenExtractor) { }

  ngOnInit() {
    
		this.selectedProgram = null;
		this.selectedArea = new FipAreaLevelModel();
		this.selectedDistrictFacility = [];
		this.getAllDistrictFacility(2);
		
		this.selectedFacility = new FipFacilityModel();;
  }

  getWidth(id) {
		return $("#" + id).width();
  }
  
  getAllDistrictFacility(data) {
		  this.fipService.getFacilityFormADistrictForRawData(data).then((data) => {
			this.allFacilities = data as any;
			this.programList = Object.keys(this.allFacilities);
			this.selectedDistrictFacility = [];
			this.selectedFacilityType = new FipDistrictFacilityModel();
			

		}).catch(error => { })
  }
  
  selectFacilityType(facilityType) {
		this.selectedFacilityType = facilityType;
		this.selectedArea = facilityType.areaLevelModel;
		this.selectedTimePeriod = new TimePeriod();
		if(this.selectedFacilityType.formId==17){
			this.allTimePeriods=[];
			this.allTimePeriods.push(this.backTimePeriod[0]);
		}
			
		else
			this.allTimePeriods = this.backTimePeriod;
		// this.selectedFacility = new FipFacilityModel();;
	}

	selectProgram(program) {
		this.selectedProgram = program;
		this.selectedArea = new FipAreaLevelModel();
		this.selectedFacilityType = new FipDistrictFacilityModel();
		this.selectedTimePeriod = new TimePeriod();
    	this.selectedDistrictFacility = this.allFacilities[program]
    	this.fipService.getAllTimeperiod(2,program).then((response) => {
			let data = response as any;
	  this.allTimePeriods = data;
	  this.backTimePeriod = this.allTimePeriods;
      // this.selectTimePeriod(this.allTimePeriods[0]);

		}).catch(error => { })
  }
  
  selectTimePeriod(timeperiod) {
    this.selectedTimePeriod = timeperiod;
    
  }

	selectFacility(facility) {
		this.selectedFacility = facility;
		
	}

	downloadReport() {
		if (!this.selectedProgram) {
			this.errorMsg = "Program";
			$("#errorMessage").modal("show");
			return false;
		}
		else if (!this.selectedFacilityType.formId) {
			this.errorMsg = "Facility type";
			$("#errorMessage").modal("show");
			return false;
		}

		else if (!this.selectedProgram) {
			this.errorMsg = "Program";
			$("#errorMessage").modal("show");
			return false;
		}

		
		else if (!this.selectedFacilityType.formId || !this.selectedTimePeriod.timePeriod_Nid ) {
			this.errorMsg = "TimePeriod";
			$("#errorMessage").modal("show");
			return false;
		}
		else {
			$(".loader").show();


			let facilitesType :String;
			let fileversion :String;
			if(this.selectedProgram=='UHC')
			facilitesType= this.selectedFacilityType.xFormTitle;
			else
			facilitesType= this.selectedArea.areaLevelName;
			let report=[this.selectedProgram,facilitesType,this.selectedTimePeriod.timePeriod_Nid];

			this.httpClient.post(Constants.HOME_URL + 'getRawDataReport', report, {
      
				responseType: "blob"
				
			  }).pipe(
				map((res: Blob) => res),
				catchError((res: Blob) => throwError(res))
			  ).subscribe(data => {
				  console.log(data)
				//this.spinner.hide();
				if(report[1]=="HSC"||report[1]=="UHC")
				fileversion="3";
				else
				fileversion=report[2];
				savesAs(data, report[0] + "_"+report[1]+ "_v"+fileversion + "_Raw Data_r1.xlsx");
			  },
				error => {
				  
				});

		}
	}

// 
	// download(url, data, method) {
	// 	// url and data options required
	// 	if (url && data) {
	// 		// data can be string of parameters or array/object
	// 		data = typeof data == 'string' ? data : $.param(data);
	// 		// split params into form inputs
	// 		var inputs = '';
	// 		$.each(data.split('&'), function () {
	// 			var pair = this.split('=');
	// 			inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
	// 		});

	// 		inputs += '<input type="hidden" name="_csrf" value="' + this.httpXsrfTokenExtractor.getToken() + '" />';
	// 		// send request
	// 		$(
	// 			'<form action="' + url + '" method="' + (method || 'post')
	// 			+ '">' + inputs + '</form>').appendTo('body')
	// 			.submit().remove();
	// 	}
	// }

}
