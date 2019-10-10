import { Component, OnInit } from '@angular/core';
import { DdmServicesService } from '../ddm-services.service';
import { HttpClient } from '@angular/common/http';
import { Constants } from 'src/app/models/constants';
import { Toast, ToastrService } from 'ngx-toastr';
declare var $: any;

@Component({
  selector: 'sdrc-la-qkhya',
  templateUrl: './la-qkhya.component.html',
  styleUrls: ['./la-qkhya.component.scss']
})
export class LaQkhyaComponent implements OnInit {
  tableData: any;
  tableBodyData: any;
  successMessage: string;
  showDistrictColumn:boolean = false;
  errorMsg: string;
  ddmService: DdmServicesService
  constructor(private ddmServiceProvider: DdmServicesService, private http: HttpClient, private toaster:ToastrService) {
    this.ddmService = ddmServiceProvider;
  }

  ngOnInit() {
    let userDetails = JSON.parse(localStorage.getItem('access_token'))
    if(userDetails!=null){
      let authorities:string[]=userDetails.roles;
      if(authorities.includes('DDM')){
        this.showDistrictColumn=false;
      }else{
        this.showDistrictColumn = true;
      }
    }
    this.ddmService.getLaQkhyaDetails().subscribe(res => {
      this.tableData = res;
      this.tableBodyData = this.tableData.tableDetails;
    })
  }

  // download excel
  tableToExcel() {
    this.http.get(Constants.HOME_URL+'getLaqshyaReport',
    {responseType: 'text' as 'json'}).subscribe((response:any) => {
      let res = JSON.parse(response)
          if(res.statusCode == 200){
          let fileName = res.File;      
          this.ddmService.downloadExcel('fileName=' + fileName );
          }else{
            this.errorMsg = "Error in downloading excel file."
            $("#errorModal").modal('show');
          }
        }, err => {
          this.errorMsg = "Error in downloading excel file."
          $("#errorModal").modal('show');
        }); 
  }

  // emit value from peer input feild if not vali
  emitPeer(td) {
    if (td.peer > 100) {
      td.peer = td.peer.toString().substring(0, 1);
      $(this).val(td.peer);
    } else if (td.peer == 0) {
      td.peer = "";
    }
    let letters = /^\d{1,6}(\.\d{1,2})?$/;
    let numbers = /^[1-9]\d*(\.\d{1,30})?$/;
    if(numbers.test(td.peer)){
    if(td.peer != 0 && !td.peer.match(letters)){
      td.peer = Math.round(td.peer)
    }else if(td.peer == 0 && !td.peer.match(letters)){
      td.peer = '';
    }
  }else{
    td.peer = '';
  }
  }

  // emit value from baseline input feild if not vali
  emitBase(td) {
    if (td.baseLine > 100) {
      td.baseLine = td.baseLine.toString().substring(0, 1);
      $(this).val(td.baseLine);
    } else if (td.baseLine == 0) {
      td.baseLine = "";
    }
    let letters = /^\d{1,6}(\.\d{1,2})?$/;
    let numbers = /^[1-9]\d*(\.\d{1,30})?$/;
    if(numbers.test(td.baseLine)){
    if(td.baseLine != 0 && !td.baseLine.match(letters)){
      td.baseLine = Math.round(td.baseLine)
    }else if(td.baseLine == 0 && !td.baseLine.match(letters)){
      td.baseLine = '';
    }
  }else{
    td.baseLine = '';
  }
  }

// submit laQkhya data
  submit() {
    this.http.post(Constants.HOME_URL + 'saveLaqshyaData', this.tableData).subscribe((res:any)=>{
      if(res.statusCode == 200){
        $("#successModal").modal('show');
      }else{
        this.errorMsg = "Error in submitting data."
        $("#errorModal").modal('show');
        // this.toaster.error("Error", "Error")
      }
    },error=>{
      this.errorMsg = "Error in submitting data."
      $("#errorModal").modal('show');
    })
  }

  reloadSubmittedData(){
    this.ddmService.getLaQkhyaDetails().subscribe(res => {
      this.tableData = res;
      this.tableBodyData = this.tableData.tableDetails;
    })
  }

}
