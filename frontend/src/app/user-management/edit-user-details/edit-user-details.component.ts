import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { UserManagementService } from '../services/user-management.service';
import { Router, RoutesRecognized } from '@angular/router';
import { filter, pairwise } from 'rxjs/operators';
import { Constants } from 'src/app/models/constants';
declare var $: any;

@Component({
  selector: 'app-edit-user-details',
  templateUrl: './edit-user-details.component.html',
  styleUrls: ['./edit-user-details.component.scss']
})

export class EditUserDetailsComponent implements OnInit {  
  form: FormGroup;
  formFields: any;
  sdrcForm: FormGroup;
  
  payLoad = '';
  
  validationMsg: any;
  UserForm:FormGroup;
  selectedDistrictId:number;
  selectedBlockId: number;
  selectedGramPanchayatId: number;
  selectedtypeDetailsId: number;
  selectedFacilityId: number;
  facilities: any;


  userManagementService: UserManagementService;

  constructor(private http: HttpClient, private userManagementProvider: UserManagementService, private router: Router) {
    this.userManagementService = userManagementProvider;
   }

  ngOnInit() {   
    this.router.events
    .pipe(filter((e: any) => e instanceof RoutesRecognized)
    ).subscribe((e: any) => {
        if(this.router.url =="/edit-user" && e.url != '/reset-password'){
          this.userManagementService.resetPasswordDetails ={};
        }
    }); 
    if(!this.userManagementService.editUserDetails){
      this.router.navigateByUrl("reset-password");
    }
    else{
      this.selectedDistrictId = this.userManagementService.editUserDetails.areaId;
      this.selectedFacilityId = this.userManagementService.editUserDetails.facilityId;
    }
    if((window.innerWidth)<= 767){
      $(".left-list").attr("style", "display: none !important"); 
      $('.mob-left-list').attr("style", "display: block !important");
    }
    this.getFacilities();
  }

  getFacilities(){
    if(this.selectedDistrictId)
    this.userManagementService.getFacilitiesByDistrictId(this.selectedDistrictId).subscribe(res=>{
      this.facilities = res;
    })
  }
 
  updateUserDetails(roleId:any){ 

    let areaId =""
     let userDetails = {  
      "id": this.userManagementService.editUserDetails.userId,
      "name":this.userManagementService.editUserDetails.name,
      "mblNo":this.userManagementService.editUserDetails.mobileNumber,
      "areaId":this.selectedDistrictId,
      "facilityId": this.selectedFacilityId?this.selectedFacilityId: null,
      "designationIds":[this.userManagementService.editUserDetails.designationId]
     }
     this.http.post(Constants.HOME_URL+'updateUser', userDetails).subscribe((data) => {
       this.validationMsg = data;    
        $("#successMatch").modal('show');     
     }, err=>{
      $("#oldPassNotMatch").modal('show');
      this.validationMsg = err.error;     
    });
  }
  successModal(){
    $("#successMatch").modal('hide');
    this.router.navigateByUrl("reset-password");
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
    // $('body,html').click(function(e){   
    //   if((window.innerWidth)<= 767){
    //   if(e.target.className == "mob-left-list" || $(e.target).closest(".left-list").length){
    //     return;
    //   } else{ 
    //       // $(".left-list").attr("style", "display: none !important"); 
    //       // $('.mob-left-list').attr("style", "display: block !important");  
    //   }
    //  }
    // }); 
  }

}

