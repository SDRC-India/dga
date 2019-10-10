import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SummaryReportComponent } from './summary-report/summary-report.component';
import { AuthorizationGuard } from '../guard/authorization.guard';
import { CrossTabReportComponent } from './cross-tab-report/cross-tab-report.component';
import { FipComponent } from './fip/fip.component';
import { ServerCheckGuard } from '../guard/server-check.guard';
import { RawDataReportComponent } from './rawData-report/raw-data-report/raw-data-report.component';
import { DistHealthActionPlanComponent } from './dist-health-action-plan/dist-health-action-plan.component';

const routes: Routes = [
  {
    path: 'summary',
    component: SummaryReportComponent,
    pathMatch: 'full',
    canActivate: [ServerCheckGuard,AuthorizationGuard],
    data:{ expectedAuthority: 'report',title : 'DGA-Summary Report'}
  },

  {
    path: 'crossTab',
    component: CrossTabReportComponent,
    pathMatch: 'full',
    canActivate: [ServerCheckGuard,AuthorizationGuard],
    data:{ expectedAuthority: 'CrossTab' ,title : 'DGA-Cross Tab Report'}
  }
  ,
  {
    path: 'fip',
    component: FipComponent,
    pathMatch: 'full',
    canActivate: [ServerCheckGuard,AuthorizationGuard],
    data:{ expectedAuthority: 'FIP',title : 'DGA-Facility Improvement Plan'}
  }
  ,
  {
    path: 'rawData',
    component: RawDataReportComponent,
    pathMatch: 'full',
    canActivate: [ServerCheckGuard,AuthorizationGuard],
    data:{ expectedAuthority: 'report',title : 'DGA-Raw Data Report'}
  }
  ,
  {
    path: 'dhap',
    component: DistHealthActionPlanComponent,
    canActivate: [ServerCheckGuard,AuthorizationGuard],
    pathMatch: 'full',
    data:{ expectedAuthority: 'FIP',title : 'DGA-District Health Action Plan'}
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule { }
