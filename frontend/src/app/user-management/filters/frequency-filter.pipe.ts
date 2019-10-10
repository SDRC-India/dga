import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'frequencyFilter'
})
export class FrequencyFilterPipe implements PipeTransform {

  transform(types: any, frequncyId: number): any {
    if(types != undefined && types != null && frequncyId != undefined && frequncyId != null){        
      switch(frequncyId){      
        case 1:
          return types.filter(type => type.typeId === frequncyId)        
      }      
    }
    else {
      return [];
    }
  }

}
