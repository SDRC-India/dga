import { Component, OnInit } from '@angular/core';
import { Constants } from 'src/app/models/constants';
import { Router } from '@angular/router';

@Component({
  selector: 'sdrc-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  copyrightText=Constants.COPY_RIGHT;


  constructor(public router:Router) { }

  ngOnInit() {
  }

}
