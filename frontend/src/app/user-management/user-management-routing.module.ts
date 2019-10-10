import { AuthorizationGuard } from './../guard/authorization.guard';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserManagementComponent } from './user-management/user-management.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { EditUserDetailsComponent } from './edit-user-details/edit-user-details.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

const routes: Routes = [
      { 
        path: 'user-management', 
        pathMatch: 'full', 
        component: UserManagementComponent,
        canActivate: [AuthorizationGuard],
        data: { 
          expectedRoles: ["UserMgmt,Edit"]
        }
      },
      { 
        path: 'reset-password', 
        pathMatch: 'full', 
        component: ResetPasswordComponent,
        canActivate: [AuthorizationGuard],
        data: { 
          expectedRoles: ["UserMgmt,Edit"]
        }
      },
      { 
        path: 'edit-user', 
        pathMatch: 'full', 
        component: EditUserDetailsComponent,
        canActivate: [AuthorizationGuard],
        data: { 
          expectedRoles: ["UserMgmt,Edit"]
        }
      },
      { 
        path: 'change-password', 
        pathMatch: 'full', 
        component: ChangePasswordComponent,
        canActivate: [AuthorizationGuard],
        data: { 
          expectedRoles: ["UserMgmt,Edit"]
        }
      }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserManagementRoutingModule { }
