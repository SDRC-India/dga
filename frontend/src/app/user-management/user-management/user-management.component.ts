import { Component, OnInit, HostListener } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Constants } from 'src/app/models/constants';
import { UserManagementService } from '../services/user-management.service';
declare var $: any;

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {  
  form: FormGroup;
  formFields: any;
  sdrcForm: FormGroup;
  
  payLoad = '';
  
  natAreaDetails: any;
  stateList: any;
  parentAreaId: number;
  paramModal: any;
  validationMsg: any;
  btnDisable: boolean = false;
  UserForm:FormGroup;
  firstFieldVariable:any;
  selectedRoleId: number;
  selectedStateId: number;
  allArea:any;
  selectedDistrict:any;
  selectedState:any;
  selectedFacilityId: number;
  facilities: any;

  fullName: string;
  userName: string;
  password: string;
  mobile: number;
  allStates:any;
  stateLists: any;
  userManagementService: UserManagementService;
  
  constructor(private http: HttpClient, private userManagementProvider: UserManagementService) {
    this.userManagementService = userManagementProvider;
   }

  ngOnInit() {    
    if(!this.userManagementService.formFieldsAll)
      this.userManagementService.getUserRoles().subscribe(data=>{
        this.userManagementService.formFieldsAll = data;
      }) 
    if(!this.userManagementService.areaDetails)   
      this.userManagementService.getAreaDetails().subscribe(data=>{
        this.allArea=data;
        this.userManagementService.areaDetails = this.allArea;
        this.userManagementService.areaForGuest = this.allArea['allArea'];
      }) 
    if((window.innerWidth)<= 767){
      $(".left-list").attr("style", "display: none !important"); 
      $('.mob-left-list').attr("style", "display: block !important");
    }
  }

  getArea(roleId:number){
    this.userManagementService.getAreaDetails().subscribe(data=>{
      this.userManagementService.areaDetails = data;
    })
    if(roleId==1 || roleId==3 || roleId==6){//state
      this.userManagementService.stateLists = this.allArea['State'];
    }
    // else if(roleId==3 || roleId==6){//district
    //   this.userManagementService.areaDetails = this.allArea['District'];
    // }else
     if(roleId==5){//district
      this.userManagementService.areaDetails = this.allArea['allArea'];
    }
  } 
  getDistrict(state){
    this.userManagementService.districtLists = this.userManagementService.areaDetails['District'].filter(d=> state.areaId == d.parentAreaId)
  }
  submitForm(roleId:any, form: NgForm){ 
    console.log(this.selectedState);
    
     let userDetails = {
      "userName":this.userName,
      "password":this.password,
      "designationIds":[roleId],
      "facilityId": this.selectedFacilityId ? this.selectedFacilityId: null,
      "mblNo":this.mobile,
      "areaId":roleId == 1?this.selectedState.areaId:this.selectedDistrict?this.selectedDistrict.areaId:null,
      "areaCode":roleId == 1?this.selectedState.areaCode:this.selectedDistrict?this.selectedDistrict.areaCode:null,
      "name":this.fullName,
      
     }
     this.http.post(Constants.HOME_URL+'createUser', userDetails).subscribe((data) => {
       this.validationMsg = data;    
        $("#successMatch").modal('show');       
        form.resetForm();
     }, err=>{
      $("#oldPassNotMatch").modal('show');
        this.validationMsg = err.error;
      

    });
  }
  successModal(){
    $("#successMatch").modal('hide');
  }

  showLists(){    
    $(".left-list").attr("style", "display: block !important"); 
    $('.mob-left-list').attr("style", "display: none !important");
  }
  ngAfterViewInit(){
    $("input, textarea, .select-dropdown").focus(function() {
      $(this).closest(".input-holder").parent().find("> label").css({"color": "#4285F4"})
      
    })
    $("input, textarea, .select-dropdown").blur(function(){
      $(this).closest(".input-holder").parent().find("> label").css({"color": "#333"})
    })
    $('body,html').click(function(e){   
      if((window.innerWidth)<= 767){
      if(e.target.className == "mob-left-list" || $(e.target).closest(".left-list").length){
        return;
      } else{ 
          // $(".left-list").attr("style", "display: none !important"); 
          // $('.mob-left-list').attr("style", "display: block !important");  
      }
     }
    });  
  }

  userRole(district){
  //  this.selectedStateId
    
  }

}
