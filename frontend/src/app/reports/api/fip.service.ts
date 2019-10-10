import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Constants } from 'src/app/models/constants';

@Injectable({
  providedIn: 'root'
})
export class FipService {

  constructor(private httpClient:HttpClient) { }

  getStateList()
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllState').toPromise()
  }
  
  getFacilityFormADistrict(stateId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getFacilityFormADistrict?stateId='+stateId).toPromise()
  }

  getFacilityFormADistrictForRawData(stateId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getFacilityFormADistrictForRawData?stateId='+stateId).toPromise()
  }

  getFipDistrict(stateId){
    return this.httpClient.get(Constants.HOME_URL+'getFipDistrict?stateId='+stateId).toPromise()
  }

  getFipReport(areaCode,formMetaId,stateId){
    return this.httpClient.get(Constants.HOME_URL+'getFIPReport?areaCode='+areaCode+'&formMetaId='+formMetaId+'&stateId='+stateId).toPromise()
  }

  getAllTimeperiod(stateId,programId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllTimePeriodForRawData?stateId='+stateId+'&programId='+programId).toPromise()
  }

  getFipRawDataReport(programId,facilityType,timePeriod){
    return this.httpClient.get(Constants.HOME_URL+'getRawDataReport?programId='+programId+'&facilityType='+facilityType+'&timePeriod='+timePeriod).toPromise()
  }
}
