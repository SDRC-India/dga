import { Component, OnInit } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Constants } from 'src/app/models/constants';
import { DhapServiceService } from '../api/dhap-service.service';
import saveAs from 'file-saver';
import { map, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { UserService } from 'src/app/authentication/api/user.service';
declare var $: any;
@Component({
  selector: 'sdrc-dist-health-action-plan',
  templateUrl: './dist-health-action-plan.component.html',
  styleUrls: ['./dist-health-action-plan.component.scss']
})
export class DistHealthActionPlanComponent implements OnInit {

  selectedState:any;
  selectedDistrict:any;
  dhapService: DhapServiceService;
  userService: UserService;
  stateLists: any;
  allArea:any;
  allTime:any;
  selectedTime:any;
  showAreaLoginBased:boolean = false;
  showDistrictLoginBased:boolean = false;
  areaId:any;
  btDisabled:boolean;


  constructor(private http: HttpClient, private dhapProvider: DhapServiceService) {
    this.dhapService = dhapProvider;
   }

  ngOnInit() {
    // if(!this.dhapService.stateLists)
    this.dhapService.getAllAreaForDHAP().subscribe(res=>{
      this.allArea= res;
      this.dhapService.areaDetails=this.allArea;
      this.dhapService.stateLists = this.allArea['State'];
      let userDetails = JSON.parse(localStorage.getItem('access_token'))
      if(userDetails!=null){
        let authorities:string[]=userDetails.roles;
        if(authorities.includes('State')){
          this.showAreaLoginBased=true;
          this.showDistrictLoginBased=false;
          this.selectedState = this.dhapService.stateLists[0].areaId;
          this.getDistrict(this.selectedState);
        }else if(authorities.includes('District')){
          this.showAreaLoginBased=true;
          this.showDistrictLoginBased=true;
          this.selectedState = this.dhapService.stateLists[0].areaId;
          // this.dhapService.districtLists.areaId=this.userService.distUser.areaId;
          // this.userService.distUser.areaId=this.dhapService.districtLists.areaId;
          this.areaId = userDetails.areaModel.areaId;
          this.getDistrict(this.selectedState);
          this.selectedDistrict = this.dhapService.districtLists.filter(d=> this.areaId== d.areaId)[0];
        }else{
          this.showAreaLoginBased=false;
          this.dhapService.districtLists = [];
        }
         
      }
      
      // console.log( this.areaId);
      // this.dhapService.districtLists=this.allArea['District']
    })
    if(!this.dhapService.timePeriods)
      this.dhapService.getAllTimeperiodForDHAP().subscribe(data=>{
        let resData=data;
        this.allTime= resData;
      })
     
  }
  getDistrict(state){
    // if(this.selectedDistrict.areaId==this.userService.distAreaid)
    // this.areaId  = localStorage.getItem('user_details');
    // this.areaId = res['AreaModel'
  //console.log(res)
    this.dhapService.districtLists = this.allArea['District'].filter(d=> state == d.parentAreaId);
   

  }

      downloadReport() {
        // $("#fieldMatch").modal('show');
        // if(    this.selectedState=="" && this.selectedDistrict=="" &&  this.selectedTime==""){
        //     $("#fieldMatch").modal('show');
        // }
        this.http.get(Constants.HOME_URL+'database/exportDHAPExcel?timeperiodId=' + this.selectedTime + '&districtId='
      + this.selectedDistrict.areaId,{responseType: 'text' as 'json'}).subscribe((response:any) => {
        let res = JSON.parse(response)
            if(res.statusCode == 200){
            let fileName = res.filePath;      
            this.dhapService.download('fileName=' + fileName );
            if(this.showAreaLoginBased == true && this.showDistrictLoginBased == true){
              this.selectedState=this.dhapService.stateLists[0].areaId;
              this.getDistrict(this.selectedState);
              this.selectedTime='';
            }else if(this.showAreaLoginBased == true){
              this.selectedState=this.dhapService.stateLists[0].areaId;
              this.getDistrict(this.selectedState);
              this.selectedTime='';
              this.selectedDistrict =''
            }
            else{
              this.selectedState=''
            this.selectedDistrict='';
            this.dhapService.districtLists = [];
            this.selectedTime='';
            
            }
            
            }else if(res.statusCode == 204){
              
            }
          }, err => {
            // alert("Data Not Found")
            $("#errorMatch").modal('show');
          });
        
      
      }

      errorModal(){
        $("#errorMatch").modal('hide');

      }
      fieldModal(){
        $("#fieldMatch").modal('hide');
      }

























    }
    