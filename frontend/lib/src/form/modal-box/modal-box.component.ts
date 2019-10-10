import { Component, OnInit, Inject } from '@angular/core';
//import { SubmissionService } from 'src/app/pages/submission/service/submission.service';
import { MAT_DIALOG_DATA } from '@angular/material';
declare var $: any;
@Component({
  selector: 'scps-modal-box',
  templateUrl: './modal-box.component.html',
  styleUrls: ['./modal-box.component.scss']
})
export class ModalBoxComponent {
 //constructor(@Inject(MAT_DIALOG_DATA) public data: any, private submissionService: SubmissionService) {
    constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    setTimeout(() => {
      $('.prev-embed').width('100%');
      $('.prev-embed').height('100%');
    }, 100);
  }
}
