import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import {  MatInputModule, MatIconModule, MatFormFieldModule, MatSelectModule } from '@angular/material';
import { NgxPaginationModule, PaginatePipe } from 'ngx-pagination';

import { ReactiveFormsModule,FormsModule } from '@angular/forms'; 
import { UserManagementComponent } from './user-management/user-management.component';
import { UserSideMenuComponent } from './user-side-menu/user-side-menu.component';
import { UserManagementService } from './services/user-management.service';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { EditUserDetailsComponent } from './edit-user-details/edit-user-details.component';
import { FrequencyFilterPipe } from './filters/frequency-filter.pipe';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { TableModule } from 'table-lib/public_api';
import { AreaFilterPipe } from './filters/area-filter.pipe';

@NgModule({
  imports: [
    CommonModule
    //,HttpModule
    //,HttpClientModule
    ,FormsModule
    ,UserManagementRoutingModule
    ,ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    TableModule,
    NgxPaginationModule
  ],
  declarations: [
    UserManagementComponent,
    ResetPasswordComponent,
    UserSideMenuComponent,
    EditUserDetailsComponent,
    FrequencyFilterPipe,
    ChangePasswordComponent,
    AreaFilterPipe
  ],
  providers:[UserManagementService]
})
export class UserManagementModule { }
