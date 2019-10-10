import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DataTreePageComponent } from './data-tree-page/data-tree-page.component';

const routes: Routes = [{path:'',component:DataTreePageComponent}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DataTreeRoutingModule { }
