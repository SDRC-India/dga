import { Component, OnInit } from '@angular/core';
import { SummaryReportService } from '../api/summary-report-service.service';
import { SectorModel } from 'src/app/models/SectorModel';
import { TimePeriod } from 'src/app/models/TimePeriodModel';
declare var $:any; 
//declare var tab:HTMLTableElement ;
@Component({
  selector: 'sdrc-summary-report',
  templateUrl: './summary-report.component.html',
  styleUrls: ['./summary-report.component.scss']
})


export class SummaryReportComponent implements OnInit {

  timeperiods = [];
  allTimeperiods=[];
  selectedProgram:SectorModel=new SectorModel();
	selectedFacility:SectorModel=new SectorModel();
	selectedSection:SectorModel=new SectorModel();
	selectedSubsection:SectorModel=new SectorModel();
	selectedTimepriod:TimePeriod=new TimePeriod();
  sectorTimeperiods = [];
  allSectors=[];
  allFacilitiesP=[];
  allPrograms=[]
  allFacilities=[];
  tableData=[];
  columns=[];
  tableDatas = [];
  tableColumns = [];
  errorMsg : string;
  selectedSectionSubsection:SectorModel=new SectorModel();
  sortType='rowId';
  sortType1='';
  sortReverse=1
  sortReverse1=1;
  divisionalTableColumns=[];
  divisionalTableData=[];
  facilityName='';
  firstP='Program *';
  sceoundP='Facility Type *';
  thirdP='Section *';
  fourthP='Subsection *';

  constructor(private summaryReportService:SummaryReportService) { }

  ngOnInit() {

    this.summaryReportService.getAllTimePeriods().then((data)=> {
      this.allTimeperiods = data as any;
      this.sectorTimeperiods = [];
    }).catch(error=>
      {})



      this.summaryReportService.getAllSectors().then((data) =>{
        this.allSectors = data as any;
        this.allFacilities = this.convert(this.allSectors).children;
        this.allFacilitiesP = this.allFacilities;
      }).catch(error=>
        {})


        this.summaryReportService.getAllSectorPrograms().then((data) =>{
          this.allPrograms = data as any;
          // this.allFacilities = this.convert(this.allSectors).children;
        }).catch(error=>
          {})

  }


   convert(array){
    let map = {};
    for(let i = 0; i < array.length; i++){
      let obj = array[i];
        if(obj.iC_Parent_NId == -1)
          obj.iC_Parent_NId =  null;
        if(!(obj.iC_NId in map)){
            map[obj.iC_NId] = obj;
            map[obj.iC_NId].children = [];
        }

        if(typeof map[obj.iC_NId].iC_Name == 'undefined'){
            map[obj.iC_NId].iC_NId = String(obj.iC_NId);
            map[obj.iC_NId].iC_Name = obj.iC_Name;
            map[obj.iC_NId].iC_Info = obj.iC_Info;
            map[obj.iC_NId].iC_Parent_NId= String(obj.iC_Parent_NId);
        }

        let parent = obj.iC_Parent_NId || '-';
        if(!(parent in map)){
            map[parent] = {};
            map[parent].children = [];
        }

        map[parent].children.push(map[obj.iC_NId]);
    }
    return map['-'];
}


getSectorTimeperiod(idList){
  this.sectorTimeperiods = [];
  this.selectedTimepriod = new TimePeriod();;
  for(let i=0; i<idList.length; i++){
    for(let j=0; j<this.allTimeperiods.length; j++){
      if(idList[i] == this.allTimeperiods[j].timePeriod_Nid){
        this.sectorTimeperiods.push(this.allTimeperiods[j]);
      }
    }	
  }
}




selectFacility (facility){
  this.selectedFacility = facility;
  this.selectedSection  =new SectorModel();
  this.selectedSubsection  =new SectorModel();
  this.selectedTimepriod =new TimePeriod();
  this.tableData=[];
  this.columns=[];
}

selectProgram (program){
  this.selectedProgram = program;
  this.selectedSection  =new SectorModel();
  this.selectedSubsection  =new SectorModel();
  this.selectedFacility=new SectorModel();
  this.selectedTimepriod =new TimePeriod();
  this.tableData=[];
  this.columns=[];
  this.allFacilities=[];
  this.updateFacility(this.selectedProgram.iC_NId);

  if(this.selectedProgram.iC_NId==235){
    
    this.sceoundP='Type *';
    
  }else{
    
    this.sceoundP='Facility Type *';
    
  }
  
}
updateFacility(ic_info){
  for(let i=0;i<this.allFacilitiesP.length;i++){
    if(this.allFacilitiesP[i].iC_info==ic_info){
      this.allFacilities.push(this.allFacilitiesP[i])
    }
    
  }
}

selectSection (section){
  this.selectedSection = section;
  this.selectedSubsection  =new SectorModel();
  this.selectedTimepriod =new TimePeriod();
  this.getSectorTimeperiod(section.utTimeperiods);
  this.tableData=[];
  this.columns=[];
}

selectSubsection (subsection){
  this.selectedSubsection = subsection;
  this.getSectorTimeperiod(subsection.utTimeperiods);
  this.tableData=[];
  this.columns=[];
}

selectTimeperiod (timeperiod){
  this.selectedTimepriod = timeperiod;
  this.tableData=[];
  this.columns=[];
}

getTableData()
{
  if(!this.selectedProgram.iC_Name){
    this.errorMsg = "Program";
    $("#errorMessage").modal("show");
    return false;
  }
 else if(!this.selectedFacility.iC_NId){
    this.errorMsg = "Facility type";
    $("#errorMessage").modal("show");
    return false;
  }
  else if(!this.selectedSection.iC_NId){
    this.errorMsg = "Section";
    $("#errorMessage").modal("show");
    return false;
  }
  else if(!this.selectedTimepriod.timePeriod_Nid){
    this.errorMsg = "Time period";
    $("#errorMessage").modal("show"); 
    return false;
  }

  if(this.selectedSubsection.iC_NId){
    this.selectedSectionSubsection = this.selectedSubsection; 
  }
  else
    this.selectedSectionSubsection = this.selectedSection;
    
  if(Object.keys(this.selectedFacility).length && Object.keys(this.selectedSectionSubsection).length && Object.keys(this.selectedTimepriod).length){
    // console.log(this.selectedSection.iC_NId+","+this.selectedSectionSubsection.iC_NId+","+this.selectProgram+","+this.selectFacility);
    
    this.summaryReportService.getSummaryApi(this.selectedFacility.iC_NId,this.selectedSectionSubsection.iC_NId,this.selectedTimepriod.timePeriod_Nid,this.selectedProgram.iC_NId,this.selectedSection.iC_NId).then(response =>{
      let data = response as any
      
      this.tableData = data;
      let set = new Set();
      console.log(this.selectedSection.iC_NId);
      for(let i=0;i<this.tableData.length;i++){
        for(let j=0;j<Object.keys(data[i]).length;j++){
          set.add(Object.keys(data[i])[j])
        }
        
      }
      
      if(this.selectedSection.iC_NId == 230 || this.selectedSection.iC_NId == 205){
        this.columns = Array.from(set);;
      }else{
        this.columns = Object.keys(data[0]);
      }
      
      if(this.tableData.length>0){
        for(let i=0; i<this.tableData.length; i++){
          this.columns.forEach(element=>{
            if(!isNaN(Number(this.tableData[i][element])))
            this.tableData[i][element]=Number(this.tableData[i][element])
          })
  
          if(this.tableData[i]['District'] == "Total"){
            this.tableData[i]['District'] = "Chattisgarh";
          }
        }
      }
      // else{
      //   $("#noDataModall").modal("show");
      // }
      
 
      this.tableDatas = this.tableData;
      this.tableColumns = this.columns;
      this.tableColumns.shift();
      
    }).catch(error=>{
      $("#noDataModall").modal("show");
    });
    ;
  }
// if(this.tableDatas.length==0){
//   $("#noDataModall").modal("show");
// }
      }


      exportTableData(id){
        let htmls = "";
        let uri = 'data:application/vnd.ms-excel;base64,';
        let template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>'; 
        let base64 = function(s) {
            return window.btoa(unescape(encodeURIComponent(s)));
        };

        let format = function(s, c) {
            return s.replace(/{(\w+)}/g, function(m, p) {
                return c[p];
            });
        };

        
        let tab_text=  "<h2>Summary Report</h2><h3>Facility Type : "+this.selectedFacility.iC_Name+"</h3><h3>Section : "+this.selectedSection.iC_Name+"</h3>"+(this.selectedSection.children.length && this.selectedSubsection.iC_Name?"<h3>Sub-Section : "+this.selectedSubsection.iC_Name+"</h3>":"")+"<h3> Timeperiod : "+this.selectedTimepriod.timePeriod+(id=='dataTable'?"</h3>":"</h3><h3>District: "+this.facilityName);
       tab_text+="<table border='2px'><tr bgcolor='#87AFC6'>";
       let textRange; let j=0;
        let tab:HTMLTableElement = <HTMLTableElement>document.getElementById(id); // id of table

        for(j = 0 ; j < tab.rows.length ; j++) 
        {     
            tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
            //tab_text=tab_text+"</tr>";
        }

        tab_text=tab_text+"</table>";
        tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
        tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
        tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params


        let ctx = {
            worksheet : 'Worksheet',
            table : tab_text
        };

        let link = document.createElement("a");
        if(id=='dataTable')
        	{
        link.download = "Summary Report _"+this.selectedFacility.iC_Name+"_"+this.selectedSection.iC_Name+(this.selectedSection.children.length && this.selectedSubsection.iC_Name?"_"+this.selectedSubsection.iC_Name:"")+"_"+this.selectedTimepriod.timePeriod+new Date().toLocaleDateString()+".xls";
        	}
        else
        	{
        	link.download = "Summary Report _"+this.selectedFacility.iC_Name+"_"+this.selectedSection.iC_Name+"_"+(this.selectedSection.children.length && this.selectedSubsection.iC_Name?"_"+this.selectedSubsection.iC_Name:"")+"_"+this.selectedTimepriod.timePeriod+"_"+this.facilityName+new Date().toLocaleDateString()+".xls";
        	}
        let exceldata = new Blob([(format(template, ctx))], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" }); 
        if (window.navigator.msSaveBlob) { // IE 10+
            window.navigator.msSaveOrOpenBlob(exceldata, link.download);
        }
        
        else {
            link.href = window.URL.createObjectURL(exceldata); // set url for link download
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
      }


     getRawData(row){
       this.facilityName=row.District;
      //  ,this.selectedProgram.iC_NId,this.selectedSection.iC_NId
        this.summaryReportService.getRawData(row.rowId, this.selectedFacility.iC_NId, this.selectedSectionSubsection.iC_NId, this.selectedTimepriod.timePeriod_Nid,this.selectedProgram.iC_NId,this.selectedSection.iC_NId).then((data)=>{
          this.divisionalTableData = data as any;
          this.divisionalTableColumns = Object.keys(this.divisionalTableData[0]);
          this.sortType1= this.divisionalTableColumns[0];
          for(let i=0; i<this.divisionalTableData.length; i++){
            this.divisionalTableColumns.forEach(element=>{
              if(!isNaN(Number(this.divisionalTableData[i][element])))
              this.divisionalTableData[i][element]=Number(this.divisionalTableData[i][element])
            })
    
        
          }
         
        $("#divisionTable").modal("show");
      }).catch(error=>{
        $("#noDataModall").modal("show");
      });
      
      };

      getWidth(id)
    {
      return $("#"+id).width();
    }
}
