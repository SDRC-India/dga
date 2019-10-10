import { Pipe, PipeTransform } from '@angular/core';
import { FileDetector } from 'selenium-webdriver/remote';

@Pipe({
  name: 'optionFilter',
  pure:false
})
export class OptionFilterPipe implements PipeTransform {

  transform(options: any[], parentColumn: any, field: any): any {
     if(parentColumn){
      // console.log(parentColumn.indexNumberTrack+"  ***   "+field.indexNumberTrack);
     }
   
    
    if(field.optionsParentColumn){
      if(field.indexNumberTrack != undefined){
        // field.optionsParentColumn=field.optionsParentColumn+'_'+field.indexNumberTrack.split('_')[1];
      }
      return options.filter(option => {
        if(parentColumn && parentColumn.value)
          return parentColumn.value.indexOf(option.parentKey) != -1
        else 
          return false;
      });
    }
    else{
      return options;
    }
  }

}
