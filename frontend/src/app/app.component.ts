import { Component, OnInit } from '@angular/core';
import { LoadingBarService } from '@ngx-loading-bar/core';
import { NgxSpinnerService } from 'ngx-spinner';
declare var $: any;

@Component({
  selector: 'sdrc-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(public loader: LoadingBarService,private spinner: NgxSpinnerService) {

    this.loader.progress$.subscribe(data=>{
      if(data>0)
      {
        this.spinner.show();
      }
      else
      {
        this.spinner.hide();
      }
    })


    $(window).scroll(function () {
  
      if ($(window).scrollTop() > 120 && $(window).width() < 2000) {
        $('#header').addClass('navbar-fixed');
      }
      if ($(window).scrollTop() < 120 && $(window).width() < 2000) {
        $('#header').removeClass('navbar-fixed');
      }
    });
  }

  ngOnInit()
  {
    $(".main-content").css("min-height", $(window).height() -150)
  }
}
