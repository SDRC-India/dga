import { Component, OnInit } from '@angular/core';
import { CommonService } from 'src/app/common/api/common.service';

@Component({
  selector: 'sdrc-exception',
  templateUrl: './exception.component.html',
  styleUrls: ['./exception.component.scss']
})
export class ExceptionComponent implements OnInit {

  constructor(public commonService:CommonService) { 
  }

  ngOnInit() {
  }

}
