import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CommonService } from '../common/api/common.service';

@Injectable({
  providedIn: 'root'
})
export class ServerCheckGuard implements CanActivate {

  constructor(private commonSerive:CommonService )
  {

  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

      return this.commonSerive.checkServerHealth();
  }
}
