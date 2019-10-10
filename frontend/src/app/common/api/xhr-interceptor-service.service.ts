import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpErrorResponse, HttpRequest, HttpHandler} from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { Constants } from 'src/app/models/constants';
import { catchError } from 'rxjs/operators';



@Injectable({
  providedIn: 'root'
})
export class XhrInterceptorServiceService implements HttpInterceptor{


  constructor(private router: Router) { }
  
  private handleAuthError(err: HttpErrorResponse): Observable<any> {
    //handle your auth error or rethrow
    if (err.status === 401 || err.status === 403) {
      this.deleteCookies();
        this.router.navigateByUrl('/login');
        // if you've caught / handled the error, you don't want to rethrow it unless
        // you also want downstream consumers to have to handle it as well.
        return of(err.message);
    }
    return throwError(err);
  }
 
  intercept(req: HttpRequest<any>, next: HttpHandler) {

    if(req.url.includes('assests'))
    {
      return next.handle(req);
    }
    else
    {
      
      const xhr= req.clone({
        headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
      });



    
    if(xhr.url != Constants.HOME_URL + 'user')
      return next.handle(xhr).pipe(catchError(x=> this.handleAuthError(x)));
    
    else
      return next.handle(xhr);
  }
  }
  deleteCookies(){
    localStorage.clear();
  }



}

