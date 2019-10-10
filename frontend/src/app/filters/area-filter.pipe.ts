import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'areaFilter'
})
export class AreaFilterPipe implements PipeTransform {

  transform(areas: any, areaLevelId: number, parentAreaId: number): IArea[] {
    if(areas != undefined && areas != null && areaLevelId != undefined && areaLevelId != null && parentAreaId != undefined && parentAreaId != null ){      
    

      switch(areaLevelId){
        case 1:
          let countries = areas.Country ? areas.Country: areas.COUNTRY;
          return countries.filter(area => area.parentAreaId === parentAreaId)
        case 2:
          let states = areas.State ? areas.State: areas.STATE
          return states.filter(area => area.parentAreaId === parentAreaId)
        case 3:
          let districts = areas.District ? areas.District: areas.DISTRICT;
          return districts.filter(area => area.parentAreaId == parentAreaId)
        case 4:
          let blocks = areas.Block ? areas.Block: areas.BLOCK
          return blocks.filter(area => area.parentAreaId == parentAreaId)
        case 5:
          let GP = 'Gram Panchayat';
          return areas[GP].filter(area => area.parentAreaId == parentAreaId)  
      }      
    }
    else {
      return [];
    }
  }

}