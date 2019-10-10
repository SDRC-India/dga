import { Component, OnInit } from '@angular/core';
import { CrossTabReportServiceService } from '../api/cross-tab-report-service.service';
import { TimePeriod } from 'src/app/models/TimePeriodModel';
import { AreaModel } from 'src/app/models/AreaModel';
import { IndicatorFormXpathMappingModel } from 'src/app/models/IndicatorFormXpathMappingModel';
import { FormXpathScoreMapping } from 'src/app/models/FormXpathScoreMapping';
declare var $:any;
@Component({
  selector: 'sdrc-cross-tab-report',
  templateUrl: './cross-tab-report.component.html',
  styleUrls: ['./cross-tab-report.component.scss']
})
export class CrossTabReportComponent implements OnInit {

  
sortReverse = 1;
sortType = " ";
allStates=[];
selectedTimePeriod:TimePeriod=new TimePeriod();
selectedTimepriod:TimePeriod=new TimePeriod();
rowIndicator;
colIndicator;
selectedFacility:FormXpathScoreMapping=new FormXpathScoreMapping();
selectedDistrict:AreaModel=new AreaModel();
allTimeperiods=[];
selectedState:AreaModel=new AreaModel();
crosstabDropDownData;
allIndicatorsList:IndicatorFormXpathMappingModel[]=[];
allFacilities:FormXpathScoreMapping[]=[]
allDistricts:AreaModel[]=[]
allIndicators:IndicatorFormXpathMappingModel[]=[];
errorMsg='';
selectedFacilityResult:FormXpathScoreMapping=new FormXpathScoreMapping();
selectedTimepriodResult:TimePeriod=new TimePeriod();
selectedDistrictResult:AreaModel=new AreaModel();
tableValue = [];
tableHeader = [];
lowHeader = [];
upperHead=[];
tableData = [];
columns = [];
selectedcolIndicator:IndicatorFormXpathMappingModel=new IndicatorFormXpathMappingModel();
selectedrowIndicator:IndicatorFormXpathMappingModel=new IndicatorFormXpathMappingModel();

  constructor(private crossTabReportServiceService:CrossTabReportServiceService) { }

  ngOnInit() {
    

		this.crossTabReportServiceService.getStateList().then((data)=> {
		this.allStates = data as any;
		});
  }


	


	
	getAllTimePeriods (stateId) {
		this.selectedTimePeriod=new TimePeriod();;
		this.rowIndicator='';
   		 this.colIndicator='';
		this.selectedFacility=new FormXpathScoreMapping();
		this.selectedDistrict=new AreaModel();;

		this.crossTabReportServiceService.getAllTimeperiod(stateId).then((data) =>{
			this.allTimeperiods = data as any;
		});
	};
	
selectState(state)
	{
		this.selectedState=state;
		this.getAllTimePeriods(state.areaId);
    	this.rowIndicator='';
		this.colIndicator='';
		this.allTimeperiods=[];
		this.selectedTimePeriod=new TimePeriod();
		this.selectedTimepriod=new TimePeriod();
    	this.selectedFacility=new FormXpathScoreMapping();
		this.selectedDistrict=new AreaModel();
		this.allIndicatorsList=[];
		this.allFacilities=[];
		this.allDistricts=[];

	}
	
	

	
	getCrossTabDropDownData(stateId,timePeriodId){
    this.rowIndicator='';
		this.colIndicator='';
    this.selectedFacility=new FormXpathScoreMapping();
		this.selectedDistrict=new AreaModel();
	this.crossTabReportServiceService.getCrossTabDropDownData(stateId,timePeriodId).then((data)=> {
		this.crosstabDropDownData = data as any;
		this.allIndicatorsList=this.crosstabDropDownData.indicatorFormXpathMappingModels;
		this.allFacilities=this.crosstabDropDownData.formXpathScoreMappingModels;
		this.allDistricts=this.crosstabDropDownData.areaList;
	});
	}
	
	selectFacility (facility){
		this.selectedFacility = facility;
		
		this.allIndicators=this.allIndicatorsList.filter( (item)=> {
				if(this.selectedFacility.form_meta_id==2)
			  return item.dhXpath != '0';
				
				else if	(this.selectedFacility.form_meta_id==1)
					return item.chcXpath != '0';
				
				else if	(this.selectedFacility.form_meta_id==3)
					return item.phcXpath != '0';

					else if	(this.selectedFacility.form_meta_id==5)
					return item.hscXpath != '0';
				
				else if	(this.selectedFacility.formId==0)
					return (item.phcXpath != '0'&&item.chcXpath != '0'&&item.dhXpath != '0'&&item.hscXpath != '0')||(item.phcXpath == '0'&&item.chcXpath == '0'&&item.dhXpath == '0'&&item.hscXpath != '0');
					
		});
		this.rowIndicator='';
		this.colIndicator='';
	};
	
selectDistrict(district){
		this.selectedDistrict = district;
	}
	selectTimeperiod (timeperiod){
		this.selectedTimepriod = timeperiod;
		this.selectedDistrict=new AreaModel();
		if(this.selectedState && this.selectedTimepriod)
		{
		this.getCrossTabDropDownData(this.selectedState.areaId,this.selectedTimepriod.timePeriod_Nid)
		}
	};
	

	
	
getTableData(){
		
		this.tableValue = [];
		this.tableHeader = [];
		this.lowHeader = [];
		this.upperHead=[];
		this.tableData = [];
		this.columns = [];
		
		this.selectedcolIndicator=this.allIndicators.filter(d=>d.label==this.rowIndicator)[0]
		this.selectedrowIndicator=this.allIndicators.filter(d=>d.label==this.colIndicator)[0]
		
		if(!this.selectedState.areaId){
			this.errorMsg = "State";
			$("#errorMessage").modal("show");
			return false;
		}
		
		else if(!this.selectedTimepriod.timePeriod){
			this.errorMsg = "Time Period";
			$("#errorMessage").modal("show");
			return false;
		}
		
		else if(!this.selectedFacility.label){
			this.errorMsg = "Facility type";
			$("#errorMessage").modal("show");
			return false;
		}
		else if(!this.selectedDistrict.areaName){
			this.errorMsg = "State/District";
			$("#errorMessage").modal("show");
			return false;
		}

		else if(!this.selectedcolIndicator)
		{
			this.errorMsg = "Row Indicator";
			$("#errorMessage").modal("show");
			return false;
		}
		
		else if(!this.selectedrowIndicator)
			{
			this.errorMsg = "Column Indicator";
			$("#errorMessage").modal("show");
			return false;
			}

		else
			{
			this.sortReverse = 1;
			this.sortType = " ";
			this.selectedFacilityResult=this.selectedFacility;
			this.selectedTimepriodResult=this.selectedTimepriod;
			this.selectedDistrictResult=this.selectedDistrict;
			
			let crossTabDataModel={
				colIndicatorFormXpathMappingId:this.selectedcolIndicator.indicatorFormXpathMappingId,

				colDhXpath:this.selectedFacility.form_meta_id==2||this.selectedFacility.formId==0?this.selectedcolIndicator.dhXpath:0,

				colChcXpath:this.selectedFacility.form_meta_id==1||this.selectedFacility.formId==0?this.selectedcolIndicator.chcXpath:0,

				colPhcXpath:this.selectedFacility.form_meta_id==3||this.selectedFacility.formId==0?this.selectedcolIndicator.phcXpath:0,

				colHscXpath:this.selectedFacility.form_meta_id==5||this.selectedFacility.formId==0?this.selectedcolIndicator.hscXpath:0,
					
				coLabel:this.selectedcolIndicator.label,

				rowIndicatorFormXpathMappingId:this.selectedrowIndicator.indicatorFormXpathMappingId,
					
				rowDhXpath:this.selectedFacility.form_meta_id==2||this.selectedFacility.formId==0?this.selectedrowIndicator.dhXpath:0,
					
				rowChcXpath:this.selectedFacility.form_meta_id==1||this.selectedFacility.formId==0?this.selectedrowIndicator.chcXpath:0,

				rowPhcXpath:this.selectedFacility.form_meta_id==3||this.selectedFacility.formId==0?this.selectedrowIndicator.phcXpath:0,

				rowHscXpath:this.selectedFacility.form_meta_id==5||this.selectedFacility.formId==0?this.selectedrowIndicator.hscXpath:0,
					
				rowLabel:this.selectedrowIndicator.label,

				facilityTypeId:this.selectedFacility.formId,

				districtId:this.selectedDistrict.areaId,
					
				timePeriodId:this.selectedTimepriod.timePeriod_Nid,
				
				colIndicatorFormXpathMappingType:this.selectedcolIndicator.type,
				
				rowIndicatorFormXpathMappingType:this.selectedrowIndicator.type
			};
			
			this.crossTabReportServiceService.getCrossTabTableData(crossTabDataModel).then((data)=> {
				this.tableValue = data as any;
				if(this.tableValue['data'].length)
					{
				this.tableHeader = data['header'];
				if(this.selectedTimepriodResult.timePeriod_Nid==0)
				this.lowHeader = data['lowHeader'];
				
				this.upperHead=Object.keys(this.tableHeader);
				this.tableData = data['data'];
				this.columns = Object.keys(this.tableData[0]);
					}
				else
					{
					$("#noDataModall").modal("show");
					}
				
			});
			}

		

	}
	
	getWidth(id)
  {
		// console.log($("#"+id).width());
   return $("#"+id).width();
  }
    
 exportTableData(id) {
    	var htmls = "";
        var uri = 'data:application/vnd.ms-excel;base64,';
        var template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>'; 
        var base64 = function(s) {
            return window.btoa(unescape(encodeURIComponent(s)));
        };

        var format = function(s, c) {
            return s.replace(/{(\w+)}/g, function(m, p) {
                return c[p];
            });
        };

       var tab_text=  "<h2>Crosstab Report</h2><h3>Facility Type : "+this.selectedFacilityResult.label+"</h3><h3>Crosstab Type :"+this.selectedrowIndicator.label+" V/S "+this.selectedcolIndicator.label+" </h3><h3>District/State: "+this.selectedDistrictResult.areaName+" </h3><h3>TimePeriod: "+this.selectedTimepriodResult.timePeriod;
      tab_text+="<table border='2px'><tr bgcolor='#87AFC6'>";
       
       var textRange; var j=0;
	   var tab:HTMLTableElement = <HTMLTableElement>document.getElementById(id); // id of table

        for(j = 0 ; j < tab.rows.length ; j++) 
        {     
            tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
            //tab_text=tab_text+"</tr>";
        }

        tab_text=tab_text+"</table>";
        tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
        tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
        tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params


        var ctx = {
            worksheet : 'Worksheet',
            table : tab_text
        };


        var link = document.createElement("a");
        link.download = "Crosstab _"+this.selectedFacilityResult.label+"_"+this.selectedrowIndicator.label+"_VS_"+this.selectedcolIndicator.label+"_"+this.selectedDistrictResult.areaName+"_"+this.selectedTimepriodResult.timePeriod+"_"+new Date().toLocaleDateString()+".xls";
        
        var exceldata = new Blob([(format(template, ctx))], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" }); 
        if (window.navigator.msSaveBlob) { // IE 10+
            window.navigator.msSaveOrOpenBlob(exceldata, link.download);
        }
        
        else {
            link.href = window.URL.createObjectURL(exceldata); // set url for link download
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
        
    };
}
