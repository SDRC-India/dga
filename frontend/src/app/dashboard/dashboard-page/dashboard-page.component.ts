import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../api/dashboard.service';
import { HttpClient, HttpXsrfTokenExtractor } from '@angular/common/http';
import { Constants } from 'src/app/models/constants';
import { AreaModel } from 'src/app/models/AreaModel';
import { FormXpathScoreMapping } from 'src/app/models/FormXpathScoreMapping';
import { TimePeriod } from 'src/app/models/TimePeriodModel';
import * as d3 from "d3";
import { ProgramModel } from 'src/app/models/ProgramModel';
declare var $: any;
@Component({
  selector: 'sdrc-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent implements OnInit {

  // roleId = $window.roleId;
  selectedGranularitySpider = "";
  selectedSectorName = "CHC";
  isPushpinClicked = false;
  lastVisiDataId = 0;
  isShowTable = false;
  isShowChart = true;
  lastVisitDataId = 0;
  allDistricts = [];
  allStates = [];
  sectors = [];
  pushpinDataCallDone = false;
  percentileFacility = 0;
  progressBarUpdateCalled = false;
  noOfFacilities = 0;
  hoverwindow = [];
  noOfFacilitiesPlanned = 0;
  parentSectors;
  selectedSector: FormXpathScoreMapping = new FormXpathScoreMapping();
  redMarkers;
  orangeMarkers;
  greenMarkers;
  selectedParentSector: FormXpathScoreMapping = new FormXpathScoreMapping();
  selectedDistrict: AreaModel = new AreaModel();
  areaLevelId: number;
  selectedProgram: ProgramModel = new ProgramModel();
  programList: ProgramModel[] = [];

  selectedPushpin;
  columns;
  allTimePeriods = [];

  selectedState: AreaModel = new AreaModel();;


  roleId;
  selectedTimePeriod: TimePeriod = new TimePeriod();

  tableData = [];

  spiderdata;

  facilityMap;
  jsonUrl;

  stateData: any;

  map = {
    center: {
      latitude: 26.344158,
      longitude: 92.673615
    },
    zoom: 7,
    markers: []
  };
  polygons;
  constructor(private dashboardService: DashboardService,
    private httpClient: HttpClient, private httpTokenExtractor: HttpXsrfTokenExtractor) {
  }

  ngOnInit() {
    $("#pdfDownloadBtn").hover(() => {

    });
    this.getAllState();
  }

  onMouseOver(infoWindow, gm) {

    if (gm.lastOpen != null) {
      gm.lastOpen.close();
    }

    gm.lastOpen = infoWindow;

    infoWindow.open();
  }

  selectParentSector(parentSector) {
    this.selectedPushpin = "";
    this.lastVisiDataId = 0;
    this.isShowChart = true;
    this.isShowTable = false;
    this.isPushpinClicked = false;
    this.selectedParentSector = parentSector;
    this.getSectors(parentSector.formXpathScoreId);
    this.getSpiderData(this.selectedParentSector.formId, 0,
      this.selectedDistrict.areaId);

  }
  selectSector(sector) {
    this.selectedSector = sector;
    if (this.allDistricts.length && !this.pushpinDataCallDone) {
      this.map.markers = [];
      this.getPushpinData(this.selectedParentSector.formId,
        this.selectedSector.formXpathScoreId,
        this.selectedDistrict.areaId);

    }

  }

  selectState(state) {
    this.selectedState = state;
    this.jsonUrl = state.areaCode + ".json";
    this.setUpMap().then(data => {
      this.stateData = data;
      this.polygons = this.stateData.polygons;
      this.map.center = this.stateData.center;
      this.getAllDistricts(state);
      this.getAllProgram(state.areaId);
      // if (this.selectedState && this.selectedTimePeriod) {
      //   this.getParentSectors(this.selectedState.areaId, this.selectedTimePeriod.timePeriod_Nid)
      // }
    });


  }

  selectProgram(program) {
    this.selectedProgram = program;
    this.getAllTimePeriods(this.selectedState.areaId, this.selectedProgram.programId);
  }



  selectDistrict(District) {
    this.selectedPushpin = "";
    this.lastVisiDataId = 0;
    this.selectedDistrict = District;
    if (this.sectors.length && !this.pushpinDataCallDone) {
      this.map.markers = [];
      this.getPushpinData(this.selectedParentSector.formId,
        this.selectedSector.formXpathScoreId,
        this.selectedDistrict.areaId);

      this.getSpiderData(this.selectedParentSector.formId, 0,
        this.selectedDistrict.areaId);
    }


  }
  selectTimePeriod(timeperiod) {
    this.selectedPushpin = "";
    this.lastVisiDataId = 0;
    this.selectedTimePeriod = timeperiod;
    if (this.selectedState && this.selectedTimePeriod) {
      this.getParentSectors(this.selectedState.areaId, this.selectedTimePeriod.timePeriod_Nid, this.selectedProgram.programId)
    }

    this.map.markers = [];
    if (this.sectors.length && !this.pushpinDataCallDone) {
      this.map.markers = [];
    }

  }
  resetPushpinDataCallDone() {
    this.pushpinDataCallDone = false;
  }


  getPushpinData(selectedParentSectorFormId,
    selectedSectorFormXpathScoreId, areaId) {
    this.map.markers = [];
    // console.log("1");
    if (!this.selectedTimePeriod.timePeriod_Nid || areaId == undefined || selectedSectorFormXpathScoreId == undefined || selectedParentSectorFormId == undefined)
      return;

    this.pushpinDataCallDone = true;
    this.httpClient
      .get(
        Constants.HOME_URL + 'googleMapData?formId=' + selectedParentSectorFormId
        + '&sector=' + selectedSectorFormXpathScoreId
        + '&areaId=' + areaId + '&timePeriodId='
        + this.selectedTimePeriod.timePeriod_Nid)
      .subscribe(
        response => {
          this.resetPushpinDataCallDone();
          let data = response as any[];
          this.noOfFacilities = data.length;
          this.map.markers = data;
          this.greenMarkers = 0;
          this.redMarkers = 0;
          this.orangeMarkers = 0;
          if (this.map.markers.length == 0) {
            $("#noDataModall").modal("show");
          }
          this.getPlannedFacilities(
            this.selectedParentSector.formId,
            this.selectedTimePeriod.timePeriod_Nid,
            this.selectedDistrict.areaId);

          for (var i = 0; i < this.map.markers.length; i++) {
            if (parseFloat(this.map.markers[i].dataValue) >= 80)
              this.greenMarkers++;
            else if (parseFloat(this.map.markers[i].dataValue) <= 60)
              this.redMarkers++;
            else
              this.orangeMarkers++;
          }
        });
  };
  getSpiderData(selectedParentSectorFormId,
    lastVisitDataId, districtId) {

    if (!this.selectedParentSector.formXpathScoreId || lastVisitDataId == undefined || districtId == undefined || selectedParentSectorFormId == undefined)
      return;

    this.dashboardService.getSpiderData(selectedParentSectorFormId, lastVisitDataId,
      districtId, this.selectedParentSector.formXpathScoreId, this.selectedParentSector.form_meta_id).then(response => {
        let data = response as any;
        this.spiderdata = data;
        this.tableData = data.tableData
        this.columns = [];
        this.columns = Object.keys(this.tableData[0]);
      });
  };
  getPlannedFacilities(selectedParentSectorFormId,
    timePeriodId, districtId) {
    if (!this.selectedParentSector.formXpathScoreId || districtId == undefined || timePeriodId == undefined || this.selectedState.areaId == undefined)
      return;

    this.dashboardService
      .getPlannedFacilities(selectedParentSectorFormId, timePeriodId,
        districtId, this.selectedState.areaId)
      .then(
        (data) => {
          this.facilityMap = data;
          this.noOfFacilitiesPlanned = this.facilityMap.facilityPlanned;
          this.progressBarUpdate();
        });
  };

  getAllDistricts(state) {
    this.dashboardService.getDashboardDistricts(state.areaId).then(response => {
      let data = response as any;
      this.allDistricts = data;
      this.selectDistrict(this.allDistricts[0]);
    });
  };


  getAllState() {
    this.dashboardService.getStateList().then(response => {
      let data = response as any
      this.allStates = data;
      // if(this.allStates.length > 1){
      // 	this.areaCodes=[]
      // 	for(var i=0; i<this.allStates.length;i++)
      // 		{
      // 		this.areaCodes.push(this.allStates[i].areaCode)
      // 		}
      // 	this.getSelectedState();
      // }else{
      // 	this.selectState(this.allStates[0]);
      // }
      this.selectState(this.allStates[0]);

    });
  }
  // getSelectedState(){

  // 	$("#areaModal").modal("show");
  // 	$(".loader").fadeOut();
  // 	this.mapSetup("resources/json/India.json", function() {
  // 		this.getParentSectorSuccessful = true;
  // 		this.checkBasicDataSuccessful();
  // 		scope.isGoogleMapVisible = false;
  // 	});
  // }

  setUpMap() {
    return this.httpClient.get('./assets/data/' + this.jsonUrl).toPromise();

  }


  pushpinClicked(marker) {
    $(".loader").show();
    this.selectedPushpin = '';
    this.isPushpinClicked = true;
    this.selectedPushpin = marker.title;
    this.lastVisiDataId = marker.id;

    this.getSpiderData(this.selectedParentSector.formId,
      marker.id, this.selectedDistrict.areaId);
    $('html, body').animate({
      scrollTop: $("#charts").offset().top
    }, 1000);


  }



  // selectStateFromMap(stateCode)
  // {
  // 	this.selectState($filter('filter')(this.allStates,{'areaCode':stateCode.properties.ID_},true)[0])
  // 	$("#areaModal").modal("hide");
  // };



  getSectors(parentId) {
    this.dashboardService.getSectors(parentId).then((response) => {
      let data = response as any;
      this.sectors = data;
      this.selectSector(this.sectors[0]);
    });
  };
  

  getAllTimePeriods(stateId, programId) {
    this.selectedTimePeriod = new TimePeriod();
    this.dashboardService.getAllTimeperiod(stateId, programId).then((response) => {
      let data = response as any;
      this.allTimePeriods = data;
      this.selectTimePeriod(this.allTimePeriods[0]);

    });
  };


  getAllProgram(stateId) {
    this.selectedProgram = new ProgramModel();

    this.dashboardService.getProgrammeList(stateId).then((response) => {
      let data = response as any;
      this.programList = data;
      this.selectProgram(this.programList[0]);

    });
  }
  getParentSectors(stateid, timperiod, programId) {
    this.selectedParentSector = new FormXpathScoreMapping();
    this.dashboardService.getParentSectors(stateid, timperiod, programId).then((data) => {
      this.parentSectors = data;
     if(this.selectedProgram.programName == 'DGA'){
      this.selectedParentSector = this.parentSectors[1];
      this.getSpiderData(this.selectedParentSector.formId, 0, 0);
      this.getSectors(this.selectedParentSector.formXpathScoreId);
     }else if(this.selectedProgram.programName == 'HWC'){
      this.selectedParentSector = this.parentSectors[0];
      this.getSpiderData(this.selectedParentSector.formId, 0, 0);
      this.getSectors(this.selectedParentSector.formXpathScoreId);
     }
    });
  };
  getWidth(id) {
    return $("#" + id).width();
  }
  progressBarUpdate() {
    let percentage = (this.noOfFacilities
      / this.noOfFacilitiesPlanned * 100).toFixed(2);;

    $(".bar-main-container.emerald .bar-container .bar").width(0);
    $(".bar-main-container.emerald .bar-container .bar")
      .animate(
        {
          "width": percentage + "%"
        },
        {
          duration: 2000,
          step: (currentStep) => {
            $(".bar-percentage")
              .text(
                currentStep.toFixed(2)
                + "%")
          }
        });
    setTimeout(() => {
      $(".bar-percentage").text(percentage + "%")
    }, 2100)
  };



  exportData(apiLink) {
    d3.selectAll("svg").attr("version", 1.1).attr("xmlns",
      "http://www.w3.org/2000/svg");
    let spiderSvg = $("#radarChart").html().replace(/\&nbsp;/g, " ").replace("–", " ");
    let chartSvgs = [];
    chartSvgs.push(spiderSvg);
    let serverURL = apiLink + "?formId=" + this.selectedParentSector.formId +
      "&lastVisitDataId=" + this.lastVisiDataId +
      "&areaId=" + this.selectedDistrict.areaId +
      "&noOfFacilities=" + this.noOfFacilities
      + '&timePeriodId=' + this.selectedTimePeriod.timePeriod_Nid
      + "&parentXpathId=" + this.selectedParentSector.formXpathScoreId
      + "&formMetaId=" + this.selectedParentSector.form_meta_id;

    this.dashboardService.exportData(serverURL, chartSvgs).then(d => {
      if (d == null || d.trim() == "") {
        //this.info = "No Data Found";
        $("#noDataModall").modal("show");
      }
      else {
        var fileName = { "fileName": d };
        this.download(Constants.HOME_URL + "downloadFile", fileName, 'POST');
      }
    }).catch(err => {
    })
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

      inputs += '<input type="hidden" name="_csrf" value="' + this.httpTokenExtractor.getToken() + '" />';
      // send request
      $(
        '<form action="' + url + '" method="' + (method || 'post')
        + '">' + inputs + '</form>').appendTo('body')
        .submit().remove();
      $(".loader").css("display", "none");
    }
  }

  fipReport() {

  }


}



