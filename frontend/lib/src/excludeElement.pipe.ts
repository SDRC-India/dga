import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'excludeElementPipe'
})
export class ExcludeElementPipe implements PipeTransform {
    transform(items: any[], filter: any): any {    
      if(items && items.length){
        const filteredItems = items.filter(item => item != filter)  
        return filteredItems;
      }    
      else{
        return [];
      }
    }
}

