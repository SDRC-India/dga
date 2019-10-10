import { Component, OnInit } from '@angular/core';
import { CommonService } from 'src/app/common/api/common.service';

@Component({
  selector: 'sdrc-ddm-report',
  templateUrl: './ddm-report.component.html',
  styleUrls: ['./ddm-report.component.scss']
})
export class DdmReportComponent implements OnInit {

  actionItems: any[] = [];
  districtName: string;
  heading:string;
  headeText:string;
  tableColumns= ["slNo","district","facilityType","facility","actionItem","status","remark"];
  tableColumns1= ["Sl No","District","Facility Type","Facility","Action Item","Status","Remark"];
  constructor(private commonService: CommonService) { }

  ngOnInit() {
    this.setColumn();
    this.commonService.getDDMReportData().subscribe((data) => {
      if(data){
        this.districtName = data.districtName;
        this.actionItems = data.actionItems;
        // this.heading="District Name :"+this.districtName;
        this.headeText="Action Items ";
      }
      
      // console.log(this.actionItems)
    });
  }
  setColumn(){

  }

  submit(event){}
  deleteButton(event){}

}
