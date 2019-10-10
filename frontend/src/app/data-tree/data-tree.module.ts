import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DataTreeRoutingModule } from './data-tree-routing.module';
import { DataTreePageComponent } from './data-tree-page/data-tree-page.component';
import { BubbleChartComponent } from './bubble-chart/bubble-chart.component';
import { SdrcDataTreeComponent } from './sdrc-data-tree/sdrc-data-tree.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [DataTreePageComponent, BubbleChartComponent, SdrcDataTreeComponent],
  imports: [
    CommonModule,
    DataTreeRoutingModule,
    FormsModule
  ]
})
export class DataTreeModule { }
