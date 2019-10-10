import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpEvent, HttpRequest } from '@angular/common/http';
import { Constants } from 'src/app/models/constants';
import { Observable } from 'rxjs';
declare var $; 


@Injectable({
  providedIn: 'root'
})
export class DhapServiceService {

  formFieldsAll: any;
  stateLists: any;
  areaDetails: any;
  districtLists:any;
  timePeriods:any;

  constructor(private httpClient: HttpClient) { }

  getAllAreaForDHAP(){
    return this.httpClient.get(Constants.HOME_URL + 'database/getAllAreaForDHAP');   
  }

  getAllTimeperiodForDHAP(){
    return this.httpClient.get(Constants.HOME_URL + 'database/getAllTimeperiodForDHAP');
  }

  excelFileDownload(areaId,timePeriodId){
    return this.httpClient.get(Constants.HOME_URL+'database/exportDHAPExcel?timeperiodId='+timePeriodId+'&districtId='+areaId);
  }
  // downloadExcel(file): Observable<any>{
  //   var body = { fileName: file };
    
  //   return this.httpClient.post(Constants.HOME_URL + 'database/downloadDHAP', body, {
  //     responseType: "blob",
  //     headers: new HttpHeaders().append("Content-Type", "application/json")}
  //     );
  // }

  download(data) {
    if (data) {
      //data can be string of parameters or array/object
      data = typeof data == 'string' ? data : $.param(data);
      //split params into form inputs
      var inputs = '';
      let url = Constants.HOME_URL + 'database/downloadDHAP';
      $.each(data.split('&'), function () {
        var pair = this.split('=');
        inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
      });
      //send request
      $('<form action="' + url + '" method="post">' + inputs + '</form>')
        .appendTo('body').submit().remove();
    };
  }


}
