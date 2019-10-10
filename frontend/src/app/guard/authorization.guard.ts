import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '../authentication/api/user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationGuard implements CanActivate {

  constructor(private userService: UserService, private router: Router)

  {
    
  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

      if(localStorage.getItem('access_token'))
      {
      let acess_token  = JSON.parse(localStorage.getItem('access_token'))
      let authorities:Set<string> =new Set();
      acess_token.authorities.forEach( element=> {
       authorities.add(element["authority"]);
      });

      const expectedAuthority = next.data.expectedAuthority;

      if(this.userService.checkLoggedIn() && authorities.has(expectedAuthority)){
        return true;
      }else{
        this.router.navigateByUrl("/");
      }
    }
    else
    {
      this.router.navigateByUrl("/");
    }
  }
}
