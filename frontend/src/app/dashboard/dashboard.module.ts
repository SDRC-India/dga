import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardPageComponent } from './dashboard-page/dashboard-page.component';
import { FormsModule } from '@angular/forms';
import { AgmCoreModule } from '@agm/core';
import { RadarChartComponent } from './radar-chart/radar-chart.component';
@NgModule({
  declarations: [DashboardPageComponent, RadarChartComponent],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    FormsModule,
    AgmCoreModule.forRoot({
      apiKey: '****************************'
    })
  ]
})
export class DashboardModule { }
