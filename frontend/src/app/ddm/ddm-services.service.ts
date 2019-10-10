import { Injectable } from '@angular/core';
import { Constants } from '../models/constants';
import { HttpClient } from '@angular/common/http';
declare var $; 

@Injectable({
  providedIn: 'root'
})
export class DdmServicesService {

  constructor(private httpClient: HttpClient) { }


  // get table data for laQkhya entr
  getLaQkhyaDetails(){
    return this.httpClient.get(Constants.HOME_URL+ 'getLaqshyaData');   
  }

  // download excel 
  downloadExcel(filename){
    if (filename) {
      //filename can be string of parameters or array/object
      filename = typeof filename == 'string' ? filename : $.param(filename);
      //split params into form inputs
      var inputs = '';
      let url = Constants.HOME_URL + 'database/downloadLaqshya';
      $.each(filename.split('&'), function () {
        var pair = this.split('=');
        inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
      });
      //send request
      $('<form action="' + url + '" method="post">' + inputs + '</form>')
        .appendTo('body').submit().remove();
    };
  }
}
