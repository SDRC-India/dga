import { Injectable } from '@angular/core';
import { HttpClient,HttpEvent, HttpRequest } from '@angular/common/http';
import { Constants } from 'src/app/models/constants';
import { Router } from '@angular/router';
import { IQuestion } from 'lib/public_api';
import { Observable,throwError } from 'rxjs';
import { filter, map, catchError } from 'rxjs/operators';
import saveAs from "save-as";
import { Toast, ToastrService } from 'ngx-toastr';



@Injectable({
  providedIn: 'root'
})
export class CommonService {
  formId;
  submissionId = 0;
  public isServerDown:boolean=false;
  constructor(private httpClient:HttpClient, private router: Router) { }

  checkServerHealth()
  {
   return this.httpClient.get(Constants.HOME_URL+'health').toPromise().then(response=>{
     this.isServerDown=false;
    return true;
  }).catch(error=>{
    this.isServerDown=true;;
    this.router.navigateByUrl("/exception");
    return false;
  });
  }

  getQuestionList(formId): Observable<any> {
    this.formId = formId;
    return this.httpClient.get(Constants.HOME_URL + 'getQuestion?formId=' + formId + '&submissioId=' + this.submissionId).pipe(
      map((res: Response) => res)
      
    );

  }

 
  getSubmissionId(): Observable<any> {
   
    return this.httpClient.get(Constants.HOME_URL + 'sgetSubmissionId').pipe(
      map((res: Response) => res)
      //catchError((res: Response) => throwError(res))
    );

  }

  getDDMReportData(): Observable<any> {
   
    return this.httpClient.get(Constants.HOME_URL + 'getDDMReportData').pipe(
      map((res: Response) => res)
  
    );

  }

  finalizeForm(submissionData: IQuestion[]): any {
    return this.httpClient.post(Constants.HOME_URL + 'submitData', submissionData ).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }

  htmltoPDF(formData: any[], formId: number) {
    let fileName = '';

      fileName = formData[0].questions.filter(d => d.columnName == 'q1')[0].value

    this.httpClient.post(Constants.HOME_URL + 'exportSubmissionToPDF', formData, {
      responseType: "blob"
    }).pipe(
      map((res: Blob) => res),
      catchError((res: Blob) => throwError(res))
    ).subscribe(data => {
      saveAs(data, fileName + "_" + new Date().getTime().toString() + ".pdf");
    },
      error => {
        // this.toaster.error("Error with server", "Error")
      });
  }

  uploadFile(file,columnName): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    formdata.append('formId', this.formId);
    formdata.append('columnName', columnName);

    const req = new HttpRequest('POST', Constants.HOME_URL + 'uploadFile', formdata, {
      reportProgress: true    });

    return this.httpClient.request(req);
  }

}
