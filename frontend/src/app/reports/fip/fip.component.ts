import { Component, OnInit } from '@angular/core';
import { FipService } from '../api/fip.service';
import { FipFacilityModel } from 'src/app/models/FipFacilityModel';
import { FipAreaLevelModel } from 'src/app/models/FipAreaLevelModel';
import { FipDistrictFacilityModel } from 'src/app/models/FipDistrictFacilityModel';
import { FipDistricts } from 'src/app/models/FipDistricts';
import { Constants } from 'src/app/models/constants';
import { AreaModel } from 'src/app/models/AreaModel';
import { HttpXsrfTokenExtractor } from '@angular/common/http';

declare var $: any;
@Component({
	selector: 'sdrc-fip',
	templateUrl: './fip.component.html',
	styleUrls: ['./fip.component.scss']
})
export class FipComponent implements OnInit {
	exportData: any;
	allFacilities: any = [];
	allStates = [];
	allDistricts: any = [];
	selectedFacility: FipFacilityModel = new FipFacilityModel();
	facilityList: any;
	selectedArea: FipAreaLevelModel = new FipAreaLevelModel();

	selectedDistrictFacility: any = [];

	fipDistricts: any = [];
	selectedFacilityType: FipDistrictFacilityModel = new FipDistrictFacilityModel();;
	selectedDistrict: FipDistricts = new FipDistricts();
	errorMsg: string;
	selectedState: AreaModel = new AreaModel();

	selectedProgram;
	programList = [];

	constructor(private fipService: FipService, private httpXsrfTokenExtractor: HttpXsrfTokenExtractor) { }


	getWidth(id) {
		return $("#" + id).width();
	}

	ngOnInit() {

		this.getAllState();

	};


	getAllDistricts(data) {
		this.fipService.getFipDistrict(data).then((data) => {
			this.allDistricts = data as any;
			this.fipDistricts = this.allDistricts;

		}).catch(error => { })
	}



	getAllDistrictFacility(data) {
		this.fipService.getFacilityFormADistrict(data).then((data) => {
			this.allFacilities = data as any;
			this.programList = Object.keys(this.allFacilities);
			this.selectedDistrictFacility = [];
			this.selectedFacilityType = new FipDistrictFacilityModel();
			this.selectProgram(this.programList[0]);

		}).catch(error => { })
	}

	getAllState() {
		this.fipService.getStateList().then(response => {
			let data = response as any
			this.allStates = data;

			if (this.allStates.length == 1)
				this.selectState(this.allStates[0]);

		});
	}

	selectState(state) {
		this.selectedState = state;
		this.selectedProgram = null;
		this.selectedArea = new FipAreaLevelModel();
		this.selectedDistrictFacility = [];
		this.getAllDistrictFacility(this.selectedState.areaId);
		this.getAllDistricts(this.selectedState.areaId);
		this.selectedDistrict = new FipDistricts();;
		this.selectedFacility = new FipFacilityModel();;

	}
	selectDistrict(district) {

		this.selectedDistrict = district;

		let flist = this.selectedDistrict.facilites;
		this.facilityList = flist.filter(e => e.areaLevelId == this.selectedArea.areaLevelId);
		this.selectedFacility = new FipFacilityModel();;

	}
	selectFacilityType(facilityType) {
		this.selectedFacilityType = facilityType;
		this.selectedArea = facilityType.areaLevelModel;

		this.selectedDistrict = new FipDistricts();;
		this.selectedFacility = new FipFacilityModel();;
	}

	selectProgram(program) {
		this.selectedProgram = program;
		this.selectedArea = new FipAreaLevelModel();
		this.selectedFacilityType = new FipDistrictFacilityModel();
		this.selectedDistrictFacility = this.allFacilities[program]
	}
	selectFacility(facility) {
		this.selectedFacility = facility;

	}

	downloadReport() {

		if (!this.selectedFacilityType) {
			this.errorMsg = "Facility type";
			$("#errorMessage").modal("show");
			return false;
		}

		else if (!this.selectProgram) {
			this.errorMsg = "Program";
			$("#errorMessage").modal("show");
			return false;
		}

		else if (!(this.selectedDistrict)) {
			this.errorMsg = "District";
			$("#errorMessage").modal("show");
			return false;
		}
		else if (!this.selectedFacility || this.selectedFacility.areaId == undefined) {
			this.errorMsg = "Facility";
			$("#errorMessage").modal("show");
			return false;
		}
		else {
			$(".loader").show();


			this.fipService.getFipReport(this.selectedFacility.facilityCode, this.selectedFacilityType.xform_meta_id, this.selectedState.areaId).then(response => {

				if (response == null || response["File"] == null || response["File"].trim() == "") {
					$("#noDataModall").modal("show");
				}
				else {
					var fileName = { "fileName": response["File"] };
					this.download(Constants.HOME_URL + "downloadFile", fileName, 'POST');
				}

			})

		}
	}


	download(url, data, method) {
		// url and data options required
		if (url && data) {
			// data can be string of parameters or array/object
			data = typeof data == 'string' ? data : $.param(data);
			// split params into form inputs
			var inputs = '';
			$.each(data.split('&'), function () {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
			});

			inputs += '<input type="hidden" name="_csrf" value="' + this.httpXsrfTokenExtractor.getToken() + '" />';
			// send request
			$(
				'<form action="' + url + '" method="' + (method || 'post')
				+ '">' + inputs + '</form>').appendTo('body')
				.submit().remove();
		}
	}

}


