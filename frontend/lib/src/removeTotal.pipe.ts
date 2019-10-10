import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'removeTotalPipe'
})
export class RemoveTotalPipe implements PipeTransform {
  transform(items: any[], removeTotal: any): any {
    if (!items) return [];
      if (removeTotal) {
        return items.filter(item => {
          return item['District'] != "" && item['District']!='Total'
        })
      }
      else{
        // items[items.length - 1][Object.keys(items[0])[0]] = "";
        // items[items.length - 1][Object.keys(items[0])[1]] = "Total";
        return items;
      }     
  }
}

