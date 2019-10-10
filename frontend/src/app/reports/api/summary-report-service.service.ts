import { Injectable } from '@angular/core';
import { Constants } from 'src/app/models/constants';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SummaryReportService {

  constructor(private httpClient: HttpClient) { }

  getAllSectors() {
    return this.httpClient.get(Constants.HOME_URL + 'getAllSectors').toPromise()
  }

  getAllSectorPrograms() {
    return this.httpClient.get(Constants.HOME_URL + 'getAllSectorPrograms').toPromise()
  }


  getAllTimePeriods() {
    return this.httpClient.get(Constants.HOME_URL + 'getAllTimePeriods').toPromise()
  }

  getAllTimeperiod(stateId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllTimePeriod?stateId='+stateId).toPromise()
  }

  getSummaryApi(facilityId, sectionId, timeperiodId,programId,sectionNid) {
    return this.httpClient.get(Constants.HOME_URL + 'getSummary?checklistId=' + facilityId + '&sectionId=' + sectionId + '&timperiodNid=' + timeperiodId + '&programNid='+programId+'&sectionNid='+sectionNid).toPromise()
  }

  getRawData(division, checklistId, sectionId, timperiodNid,programId,sectionNid) {
    // ,programId,sectionNid
    return this.httpClient.get(Constants.HOME_URL + 'getRawData?division=' + division +
      "&checklistId=" + checklistId +
      "&sectionId=" + sectionId +
      "&timperiodNid=" + timperiodNid
      + '&programNid='+programId+'&sectionNid='+sectionNid).toPromise();
  }

  getRawDataList(){
    return this.httpClient.get(Constants.HOME_URL+'getRawDataList').toPromise()
  }

  getRawReport(id){
    return this.httpClient.get(Constants.HOME_URL+'getRawReport?id='+id).toPromise()
  }
}
