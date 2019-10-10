import { Component, OnInit, ViewChild } from '@angular/core';
import { FormComponent } from 'lib/public_api';
import * as status from 'http-status-codes';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';
import { CommonService } from 'src/app/common/api/common.service';
declare var $: any;
@Component({
  selector: 'sdrc-ddm-entry',
  templateUrl: './ddm-entry.component.html',
  styleUrls: ['./ddm-entry.component.scss']
})
export class DdmEntryComponent implements OnInit {

  formDetails: any[] = [];
  selectedSection: any;
  sectionName: any;
  formId = 1;
  submissionId=0;
  formSections: any = [];
  finalize = false;
  valid = false;
  isDownload: boolean;
  validationMsg: string;
  @ViewChild('sdrcForm') sdrcForm: FormComponent;
  constructor(private commonService: CommonService,private toastr: ToastrService,private location: Location) { }

  ngOnInit() {
    
    this.getQuestions();
    
  }

  getQuestions(){
    this.commonService.getSubmissionId().subscribe((data) => {
      this.submissionId=data;
      this.commonService.submissionId=this.submissionId;
      this.getQuestionList(this.formId);
    });
  }

  getQuestionList(formId) {
    this.commonService.getQuestionList(formId).subscribe((data) => {
      this.formDetails = data;
      this.formDetails = this.formDetails.sort((n1, n2) => {
        if (n1.sectionOrder > n2.sectionOrder) {
          return 1;
        }
        if (n1.sectionOrder < n2.sectionOrder) {
          return -1;
        }
        return 0;
      });
      this.defaultViewforSection();
    });
  }

  defaultViewforSection() {
    // Default view of section
    this.selectedSection = this.formDetails[0];
    this.sectionName = this.formDetails[0].name;
    this.formSections = this.formDetails[0].questions;
    
    this.formSections = this.formSections.sort((n1, n2) => {
      if (n1.key > n2.key) {
        return 1;
      }
      if (n1.key < n2.key) {
        return -1;
      }
      return 0;
    });
  }
  saveForm(data)
  {
    if (this.finalize && this.valid) {
    const submissionData = this.makeNewFormList(this.formDetails);
    this.commonService.finalizeForm(submissionData).subscribe((data) => {
      if (data.statusCode === status.OK) {
        $('#successModal').modal('show');
        //  this.toastr.success(data.message, 'Success', {
        //  });
      } else if (data.statusCode === status.NOT_MODIFIED) {
         this.toastr.warning(data.message, 'Warning', {

         });
      } else if (data.statusCode === status.CONFLICT) {
        // this.toastr.error(data.message, 'Error', {

        // });
      }

      // this.location.back();
    });
  }
  }
  goBack(){
    $('#successModal').modal('hide');
    this.location.back();
  }
  async finalizeClicked(){
    this.finalize = true;
    // // $('#submit-button').click();
    this.valid = false;
    for (let i = 0; i < this.formDetails.length; i++) {
      let section = this.formDetails[i]
      if (this.selectedSection !== section) {
        this.selectedSection = section;
        this.formSections = [];
        this.sectionName = section.name;
        this.formSections = section.questions;

        this.sdrcForm.questionArray = this.formSections;
        this.sdrcForm.numberOfColumn = 1;
        this.sdrcForm.ngOnChanges();

        this.formSections = this.formSections.sort((n1, n2) => {
          if (n1.key > n2.key) {
            return 1;
          }
          if (n1.key < n2.key) {
            return -1;
          }
          return 0;
        });
      }
      await this.wait(100);
      $('#submit-button').click();
      if (!this.sdrcForm.form.valid && !this.sdrcForm.form.disabled) {
       
        return false;
      }
    }
    this.valid = true;
    $('#submit-button').click();
    return true;
  }
  makeNewFormList(formlist) {
    const newformlist = [];
    for (const item of formlist) {
      // tslint:disable-next-line:prefer-for-of
      for (let index = 0; index < item.questions.length; index++) {
        newformlist[newformlist.length] = Object.assign(item.questions[index]);
      }
    }
    return newformlist;
  }

  wait(ms: number) {
    return new Promise((resolve) => {
      setTimeout(resolve, ms);
    });
  }

  async htmltoPDF() {
    this.isDownload = true

    await this.wait(100);
    $('#submit-button').click();
    if (!this.sdrcForm.form.valid && !this.sdrcForm.form.disabled) {

      //Showing error modal
      $("#errorModel").modal('show');
      this.validationMsg = "Fill up minimum information to download."

    } else {
      this.commonService.htmltoPDF(this.formDetails, 1);
    }
    this.isDownload = false
  }
}
