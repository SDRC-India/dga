import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../api/user.service';

declare var $: any;
@Component({
  selector: 'sdrc-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

 userService: UserService;
  constructor(private app: UserService, private http: HttpClient, private router: Router) { 
    this.userService=app
  }

  ngOnInit() {
   
  }



  login(){
    this.app.authenticate(this.userService.credentials, () => {
      if(this.userService.authenticated == true){
        this.router.navigateByUrl('');
      }
      else{
        $(".error-message").fadeIn("slow");
        setTimeout(function(){
          $(".error-message").fadeOut("slow");
        }, 5000)
      }
    });
    return false;
  }

}
