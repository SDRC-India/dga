import { UserManagementComponent } from './user-management/user-management/user-management.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './authentication/login/login.component';
import { HomeComponent } from './static/home/home.component';
import { LoggedInGuard } from './guard/logged-in.guard';
import { AuthorizationGuard } from './guard/authorization.guard';
import { AboutUsComponent } from './static/about-us/about-us.component';
import { TermOfUseComponent } from './static/term-of-use/term-of-use.component';
import { DisclamerComponent } from './static/disclamer/disclamer.component';
import { PrivacyPolicyComponent } from './static/privacy-policy/privacy-policy.component';
import { SiteMapComponent } from './static/site-map/site-map.component';
import { ExceptionComponent } from './static/exception/exception.component';
import { ServerCheckGuard } from './guard/server-check.guard';
import { ChangePasswordComponent } from './user-management/change-password/change-password.component';
import { ResetPasswordComponent } from './user-management/reset-password/reset-password.component';
import { DdmEntryComponent } from './ddm/ddm-entry/ddm-entry.component';
import { DdmReportComponent } from './ddm/ddm-report/ddm-report.component';
import { LaQkhyaComponent } from './ddm/la-qkhya/la-qkhya.component';
import { ContactUsComponent } from './static/contact-us/contact-us.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full',
    data:{title : 'DGA-Home' },
    canActivate: [ServerCheckGuard]
  },
  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full',
    canActivate: [ServerCheckGuard,LoggedInGuard],
    data:{title : 'DGA-Login' }
  },
  {
    path: 'dashboard',
    loadChildren: './dashboard/dashboard.module#DashboardModule',
    pathMatch: 'full',
    canActivate: [ServerCheckGuard,AuthorizationGuard],
    data: { expectedAuthority: 'dashboard',title : 'DGA-Dashboard' }
  },
  {
    path: 'dataTree',
    loadChildren: './data-tree/data-tree.module#DataTreeModule',
    pathMatch: 'full',
    canActivate: [ServerCheckGuard,AuthorizationGuard],
    data: { expectedAuthority: 'dataTree',title : 'DGA-Data Tree' }

  },
  {
    path: 'report',
    loadChildren: './reports/reports.module#ReportsModule',
  }
  ,
  {
    path: 'about-us',
    component: AboutUsComponent,
    pathMatch: 'full',
    data:{title : 'DGA-About Us' },
    canActivate: [ServerCheckGuard]
  },
  {
    path: 'contact-us',
    component: ContactUsComponent,
    pathMatch: 'full',
    data:{title : 'DGA-Contact Us' },
    canActivate: [ServerCheckGuard]
  },
  {
    path: 'termOfUse',
    component: TermOfUseComponent,
    pathMatch: 'full',
    data:{title : 'DGA-Term of Use' },
    canActivate: [ServerCheckGuard]
  },
  {
    path: 'disclamer',
    component: DisclamerComponent,
    pathMatch: 'full',
    data:{title : 'DGA-Disclamer' },
    canActivate: [ServerCheckGuard]
  },
  {
    path: 'privacyPolicy',
    component: PrivacyPolicyComponent,
    pathMatch: 'full',
    data:{title : 'DGA-Policy' },
    canActivate: [ServerCheckGuard]
  },
  {
    path: 'siteMap',
    component: SiteMapComponent,
    pathMatch: 'full',
    data:{title : 'DGA-Site Map' },
    canActivate: [ServerCheckGuard]
  },

  {
    path: 'exception',
    component: ExceptionComponent,
    pathMatch: 'full',
    data:{title : 'DGA-Error' }
  },
  {
    path: 'change-password',
    component: ChangePasswordComponent,
    data:{title : 'DGA-Change Password' },
    pathMatch: 'full'
  },
  {
    path: 'edit-user',
    component: ResetPasswordComponent,
    data:{title : 'DGA-Edit User' },
    pathMatch: 'full'
  },
  {
    path: 'ddm-entry',
    component: DdmEntryComponent,
    pathMatch: 'full',
    
     canActivate: [ServerCheckGuard,AuthorizationGuard],
     data: { expectedAuthority: 'dashboard',title : 'DGA-Action Item Entry' }
  },
  {
    path: 'ddm-report',
    component: DdmReportComponent,
    pathMatch: 'full',
    
     canActivate: [ServerCheckGuard,AuthorizationGuard],
     data: { expectedAuthority: 'dashboard',title : 'DGA-Action Item Report' }
  },
  {
    path: 'add-user',
    component: UserManagementComponent,
    data:{title : 'DGA-Create New User' },
    pathMatch: 'full'
  },
  {
    path: 'laQshya',
    component: LaQkhyaComponent,
    pathMatch: 'full'
  },
  { path: '**', redirectTo: 'exception' }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
