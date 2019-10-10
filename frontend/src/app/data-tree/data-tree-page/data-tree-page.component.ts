import { Component, OnInit } from '@angular/core';
import { AreaModel } from 'src/app/models/AreaModel';
import { TimePeriod } from 'src/app/models/TimePeriodModel';
import { DataTreeService } from '../api/data-tree.service';
import { ProgramModel } from 'src/app/models/ProgramModel';
declare var $: any;
@Component({
  selector: 'sdrc-data-tree-page',
  templateUrl: './data-tree-page.component.html',
  styleUrls: ['./data-tree-page.component.scss']
})
export class DataTreePageComponent implements OnInit {

  imagePath:string='./assets/images/Data tree.jpg';
  parentArea;
  bubbleDataModel;
  allStates = [];
  isBkBtn = false;
  isColor = true;
  isValue = true;
  selectedDistrict: AreaModel = new AreaModel();
  selectedState: AreaModel = new AreaModel();
  selectedTimePeriod: TimePeriod = new TimePeriod();
  allTimePeriods = [];
  allDistricts = [];
  redBubbleCount = 0;
  yellowBubbleCount = 0;
  greenBubbleCount = 0;
  selectedSector;
  isTrendVisible;
  dataTreeData;
  isBubbleVisible = false;

  selectedProgram : ProgramModel = new ProgramModel();
  programList:ProgramModel[]=[];
  
  constructor(private dataTreeService: DataTreeService) {
    
   }

   getWidth(id)
   {
    return $("#"+id).width();
   }
   
  ngOnInit() {
    this.getAllState();
  }

  forceLayoutHover($event)
  {
    this.isTrendVisible=$event as boolean;
  }
  setImagePath()
  {
    this.imagePath='./assets/images/Data tree.jpg';
  }
  forceLayoutCalled()
  {
    this.imagePath='./assets/images/SingleClickHelp.png';
    this.setBackTrue();
    this.setColorFalse();
    this.setValueFalse();
  }
  setBackTrue() {
    this.isBkBtn = true;
  };
  setBackFalse() {
    this.isBkBtn = false;
  };
  setColorTrue() {
    this.isColor = true;
  };
  setColorFalse() {
    this.isColor = false;
  };
  setValueTrue() {
    this.isValue = true;
  };
  setValueFalse() {
    this.isValue = false;
  };

  closeViz() {
    this.isTrendVisible = false;
  };

  showViz() {
    this.isTrendVisible = true;

  };

  selectState(state) {
    this.selectedDistrict=new AreaModel();
    this.selectedState = state;
    this.getAllDistricts(state);
    this.getAllProgram(state.areaId);
  }


  selectProgram(program)
  {
    this.selectedProgram=program;
    this.selectedTimePeriod=new TimePeriod();
    this.getAllTimePeriods(this.selectedState.areaId,this.selectedProgram.programId);
  }


  getAllState() {
    $('#foot').addClass('static_foot')
    this.dataTreeService.getStateList().then(response => {
      let data = response as any
      this.allStates = data;
      this.selectState(this.allStates[0]);
      $('#foot').removeClass('static_foot')
    });
  }

  getAllDistricts(state) {
    this.dataTreeService.getDashboardDistricts(state.areaId).then(response => {
      let data = response as any;
      this.allDistricts = data;
      this.selectDistrict(this.allDistricts[0]);
    });
  };

  getAllTimePeriods(stateId,programId) {
    this.selectedTimePeriod = new TimePeriod();
    this.dataTreeService.getAllTimeperiod(stateId,programId).then((response) => {
      let data = response as any;
      this.allTimePeriods = data;
      this.selectTimePeriod(this.allTimePeriods[0]);

    });
  };


  getAllProgram(stateId)
  {
    this.selectedProgram = new ProgramModel();

    this.dataTreeService.getProgrammeList(stateId).then((response) => {
      let data = response as any;
      this.programList = data;
      this.selectProgram(this.programList[0]);

    });
  }

  selectDistrict(District) {

    this.selectedDistrict = District;
    this.getbubbledata(null)
  }
  selectTimePeriod(timeperiod) {
    this.selectedTimePeriod = timeperiod;
    this.dataTreeService.getTreeData(this.selectedState.areaId, this.selectedTimePeriod.timePeriod_Nid,this.selectedProgram.programId).then((data) => {
      this.dataTreeData = data;
    });
  }



  getbubbledata($event) {
    if ($event)
      this.selectedSector = $event;
    // this.bubbleDataModel = [];
    if (this.selectedDistrict.areaId != undefined && this.selectedSector)
      this.dataTreeService.getBubbleChartData(this.selectedSector.Id, this.selectedDistrict.areaId, this.selectedTimePeriod.timePeriod_Nid)
        .then((data) => {
          // $(".loader").fadeOut();
          this.setBackFalse();
          this.setColorTrue();
          this.setValueTrue();
        
          this.imagePath='./assets/images/Data tree.jpg';
          this.isBubbleVisible = true;
          this.bubbleDataModel = data as any;
          this.redBubbleCount = 0;
          this.yellowBubbleCount = 0;
          this.greenBubbleCount = 0;
          if (this.bubbleDataModel.length) {
            for (var i = 0; i < this.bubbleDataModel.length; i++) {
              if (this.bubbleDataModel[i].value < 60) {
                this.redBubbleCount++;
              }
              else if (this.bubbleDataModel[i].value >= 60 && this.bubbleDataModel[i].value < 80) {
                this.yellowBubbleCount++;
              }
              else {
                this.greenBubbleCount++;
              }
            }
          }
          else {
            $("#noDataModall").modal("show");
          }
        });
  };

}
