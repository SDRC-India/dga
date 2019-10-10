import { Injectable } from '@angular/core';
import { Constants } from 'src/app/models/constants';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CrossTabReportServiceService {

  constructor(private httpClient:HttpClient) { }

  getStateList()
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllState').toPromise()
  }

  getAllTimeperiod(stateId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllTimePeriod?stateId='+stateId+'&programId='+1).toPromise()
  }

  getCrossTabDropDownData(stateid,timeperiod)
  {
    return this.httpClient.get(Constants.HOME_URL+'crossTabDropDownData?timePeriodId='+timeperiod+'&stateId='+stateid).toPromise()
  }

  getCrossTabTableData(crossTabDataModel){
    return this.httpClient.post(Constants.HOME_URL+'getCrossTabTableData',crossTabDataModel).toPromise()

	};
}


