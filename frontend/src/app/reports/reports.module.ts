import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { SummaryReportComponent } from './summary-report/summary-report.component';
import { CrossTabReportComponent } from './cross-tab-report/cross-tab-report.component';
import { FormsModule } from '@angular/forms';

import { FipComponent } from './fip/fip.component';


import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { CrossTabIndicatorSearchPipe } from './pipes/cross-tab-indicator-search.pipe';
import { TableDataSortPipe } from '../pipes/table-data-sort.pipe';
import { RawDataReportComponent } from './rawData-report/raw-data-report/raw-data-report.component';
import { DistHealthActionPlanComponent } from './dist-health-action-plan/dist-health-action-plan.component';
import { MatSelectModule, MatInputModule, MatFormFieldModule } from '@angular/material';
@NgModule({
  imports: [
    CommonModule,
    ReportsRoutingModule,
    FormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  declarations: [CrossTabIndicatorSearchPipe,
    FipComponent,
    SummaryReportComponent,
    RawDataReportComponent,
    CrossTabReportComponent,TableDataSortPipe, RawDataReportComponent, DistHealthActionPlanComponent]
})
export class ReportsModule { }
