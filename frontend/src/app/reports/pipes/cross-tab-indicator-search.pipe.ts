import { Pipe, PipeTransform } from '@angular/core';
import { IndicatorFormXpathMappingModel } from 'src/app/models/IndicatorFormXpathMappingModel';

@Pipe({
  name: 'crossTabIndicatorSearch'
})
export class CrossTabIndicatorSearchPipe implements PipeTransform {

  transform(value: IndicatorFormXpathMappingModel[], keyword:string,exludeIndicator:string): any {
    
    if(keyword && exludeIndicator)
    return value.filter(d=>d.label.toLowerCase().includes(keyword.trim().toLowerCase()) && d.label!=exludeIndicator)
    
    else if(keyword && !exludeIndicator)
    return value.filter(d=>d.label.toLowerCase().includes(keyword.trim().toLowerCase()))

    else if(!keyword && exludeIndicator)
    return value.filter(d=>d.label!=exludeIndicator)

    else
    return value;
  }

}
