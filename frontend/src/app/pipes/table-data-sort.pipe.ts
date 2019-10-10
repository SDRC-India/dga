import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'tableDataSort'
})
export class TableDataSortPipe implements PipeTransform {

  transform(value: any[], sortOn:string , sortType:number): any {

    return value.sort((a, b)=>{
        if(!a.rowId && a.District)
        return 0;

      if(a[sortOn] < b[sortOn]){
          return -1 * sortType;
      }
      else if( a[sortOn] > b[sortOn]){
          return 1 * sortType;
      }
      else{
          return 0;
      }
  });
  }

}
