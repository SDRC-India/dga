import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Constants } from 'src/app/models/constants';


@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private httpClient:HttpClient) { }

  getPlannedFacilities(formId, timePeriodId, districtId,stateId)
  {
   return this.httpClient.get(Constants.HOME_URL+'getPlannedFacilities?formId=' + formId +
    "&timeperiodId=" + timePeriodId +
    "&areaId=" + districtId +"&stateId="+stateId).toPromise()
  }

  getSpiderData(formId, lastVisitDataId, districtId,parentXpathid,formMetaId)
  {
    return this.httpClient.get(Constants.HOME_URL+'spiderData?formId=' + formId +
    "&lastVisitDataId=" + lastVisitDataId +
    "&areaId=" + districtId +"&parentXpathId="+parentXpathid+"&formMetaId="+formMetaId).toPromise()
  }

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

  getParentSectors(stateid,timeperiod,programId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getParentSectors?timeperiodId='+timeperiod+'&stateId='+stateid +'&programId='+programId).toPromise()
  }

  getSectors(parentId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getSectors?parentId='+parentId).toPromise()
  }

  getAllTimeperiod(stateId,programId)
  {
    return this.httpClient.get(Constants.HOME_URL+'getAllTimePeriod?stateId='+stateId+'&programId='+programId).toPromise()
  }

  exportData(severUrl:string,svgDatas:any[])
  {
    return this.httpClient.post(Constants.HOME_URL+severUrl,svgDatas,{responseType:'text'}).toPromise()
  }

  getFipReport(areaCode,formMetaId,stateId){
    return this.httpClient.get(Constants.HOME_URL+'getFIPReport?areaCode='+areaCode+'&formMetaId='+formMetaId+'&stateId='+stateId).toPromise()
  }
}
