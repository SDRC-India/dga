import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'searchPipe'
})
export class SearchPipePipe implements PipeTransform {

  transform(tableData: any[], searchText: string): any[] {
    if (!tableData) return [];
    if (!searchText) return tableData;
    searchText = searchText.toLowerCase();
    return tableData.filter(details => {   
      return JSON.stringify(Object.values(details)).toLowerCase().includes(searchText);
    });
  }
}

