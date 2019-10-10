import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'areaFilter'
})
export class AreaFilterPipe implements PipeTransform {

  transform(areas: any, roleId: number, areaId :number): IArea[] {
    
    if(areas != undefined && areas != null && roleId != undefined && roleId != null && areaId != null && areaId != undefined){      
    
      switch(areaId){       
        case 1:
          return areas.State.filter(area => area.parentAreaId == areaId)
        case 2:
          return areas.District.filter(area => area.parentAreaId == areaId) 
      }      
    }
    else {
      return areas;
    }
  }

}
