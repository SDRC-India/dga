import { Injectable } from '@angular/core';
import { Constants } from 'src/app/models/constants';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataTreeService {

  constructor(private httpClient:HttpClient) { }


  getDashboardDistricts(stateId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllDistrict?stateId='+stateId).toPromise()
  }
  getStateList()
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllState').toPromise()
  }

  getProgrammeList(stateId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllProgramme?stateId='+stateId).toPromise()
  }

  getAllTimeperiod(stateId,programId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllTimePeriod?stateId='+stateId+'&programId='+programId).toPromise()
  }

  getTreeData (stateId,timePeriodId,programId)
  {
    return this.httpClient.get(Constants.HOME_URL+'treeData?stateId='+stateId+'&timePeriodId='+timePeriodId+'&programId='+programId).toPromise()
  }
  getBubbleChartData (sectorId, areaId,timeperiodId){
    return this.httpClient.get(Constants.HOME_URL+'bubbleChartData?sectorId='+sectorId + '&areaId=' + areaId + '&timeperiodId='+timeperiodId).toPromise()
  }

  getforceLayoutData(sectorId, areaId,timeperiodId)
  {
    return this.httpClient.get(Constants.HOME_URL+'forceLayoutData?sectorId='
    + sectorId
    + '&areaId='
    + areaId
    +'&timeperiodId='+timeperiodId).toPromise()
  }

}
