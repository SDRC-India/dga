import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormComponent } from './form/form.component';
import { MatButtonModule, MatCheckboxModule, MatInputModule, MatSelectModule, MatDatepickerModule, MatNativeDateModule, MatRadioModule, MatListModule, MAT_CHECKBOX_CLICK_ACTION, MatChipsModule, MatIconModule, MatTooltipModule, MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule} from '@angular/material';
import { OptionFilterPipe } from './option-filter.pipe'
import { TrimOnBlurModule } from 'ng2-trim-on-blur';
import { ModalBoxComponent } from './modal-box/modal-box.component';
@NgModule({
  imports: [
    CommonModule,
     FormsModule,
     ReactiveFormsModule,
     MatButtonModule,
     MatCheckboxModule,
     MatInputModule,
     MatSelectModule,
     MatDatepickerModule,
     MatNativeDateModule,
     MatRadioModule,
     MatListModule,
     MatChipsModule,
     MatIconModule,
     MatTooltipModule,
     TrimOnBlurModule,
     MatDialogModule
  ],
  declarations: [FormComponent, OptionFilterPipe, ModalBoxComponent],
  entryComponents: [ModalBoxComponent],
  providers: [
    {provide: MAT_CHECKBOX_CLICK_ACTION, useValue: 'check'}, { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: true } }
  ],
  exports: [FormComponent, OptionFilterPipe]
})
export class FormModule {
}
