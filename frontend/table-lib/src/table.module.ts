import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from './table/table.component';
import { NgxPaginationModule, PaginatePipe } from 'ngx-pagination';
//import { Ng2SearchPipeModule} from 'ng2-search-filter'; 
import { FormsModule } from '@angular/forms';
import { SearchPipePipe } from './search-pipe.pipe';
//import { PDFExportModule } from '@progress/kendo-angular-pdf-export';
import { SortPipePipe } from './sort-pipe.pipe';
import { ExcludeElementPipe } from './excludeElement.pipe';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgxPaginationModule,
    //Ng2SearchPipeModule,
   // PDFExportModule
  ],
  declarations: [TableComponent, SearchPipePipe,SortPipePipe, ExcludeElementPipe],

  providers: [],
  exports: [TableComponent, SearchPipePipe, SortPipePipe, ExcludeElementPipe]
})
export class TableModule { }
