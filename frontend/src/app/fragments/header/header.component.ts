import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/authentication/api/user.service';
import { AccessTokenModel } from 'src/app/models/AccessTokenModel';
import { Title } from '@angular/platform-browser';
import { filter, map, mergeMap,catchError } from 'rxjs/operators';
import { Constants } from 'src/app/models/constants';
import { SummaryReportService } from 'src/app/reports/api/summary-report-service.service';
import { HttpClient } from '@angular/common/http';
import savesAs from "save-as";
import { throwError } from 'rxjs';
declare var $: any;


@Component({
  selector: 'sdrc-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  appName=Constants.APP_NAME;
  allRawDataReports:any;
  isAdmin:boolean = false;
  userName: any;
  userService:UserService;
  app: any;
  constructor(private summaryReportService:SummaryReportService,private appService: UserService,private userServiceProvider: UserService,
    private httpClient: HttpClient,public router:Router, private activatedRoute: ActivatedRoute,
    public auth:UserService,private titleService:Title) {
      this.app = appService;
    this.router.events.pipe(
     filter(event => event instanceof NavigationEnd),
     map(() => this.activatedRoute),
      map((route) => {
        while (route.firstChild) {
         route = route.firstChild;
          this.userService = userServiceProvider;
         };
         this.userService = userServiceProvider;
       return route;
      }),
       filter((route) => route.outlet === 'primary'),
     mergeMap((route) => route.data),
     ).subscribe((event) => this.titleService.setTitle(event['title']));
    
   }

  ngOnInit() {
    
    let userDetails = JSON.parse(localStorage.getItem('access_token'))
    this.userName = userDetails.username;
    if(userDetails!=null){
      let authorities:string[]=userDetails.roles;
      if(authorities.includes('Admin'))
        this.isAdmin=true
    }else{
      this.isAdmin=false;
    }
  }
  
ngAfterViewChecked() {
  if ($(window).width() <= 992) {
  $(".navbar .collapse").removeClass("show");
  $(".navbar-nav .nav-item").not('.dropdown').click(function () {
  $(".collapse").removeClass("show");
  })
  }
  }

  getUserName()
  {
    if(!this.auth.checkLoggedIn())
    return null;

    let acess_token :AccessTokenModel = JSON.parse(localStorage.getItem('access_token'))
    return acess_token.username;
  }
  isLoggedIn(){
    if(!this.auth.checkLoggedIn())
      return false;
    else
      return true;  
  }
  checkRole(role:string){
    let acess_token :AccessTokenModel = JSON.parse(localStorage.getItem('access_token'))
    let roles:Set<string> =new Set();

    acess_token.roles.forEach( element=> {
      roles.add(element);
    });
    if(roles.has(role))
      return true;
    else
      return false;
  }
  
  checkUserAuthorization(data:string)
  {
    const expectedAuthority=data;
    
    if(!this.auth.checkLoggedIn())
    return false;

   let acess_token :AccessTokenModel = JSON.parse(localStorage.getItem('access_token'))
   let authorities:Set<string> =new Set();

   acess_token.authorities.forEach( element=> {
    authorities.add(element["authority"]);
   });


   if(authorities.has(expectedAuthority))
    return true;

    else
   return false;

  }

  logout()
  {
    this.userName = '';
    this.auth.logout();
  }
  
  rawReport(report){
    
     
      this.httpClient.post(Constants.HOME_URL + 'getRawReport', report, {
      
        responseType: "blob"
        
      }).pipe(
        map((res: Blob) => res),
        catchError((res: Blob) => throwError(res))
      ).subscribe(data => {
        //this.spinner.hide();
        savesAs(data, report + "_" + new Date().getTime().toString() + ".xlsx");
      },
        error => {
          
        });
  }


}
