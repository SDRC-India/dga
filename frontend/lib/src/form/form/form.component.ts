import { Component, OnInit, Input, OnChanges, Output, EventEmitter, ViewChild, ElementRef, AfterViewInit, SecurityContext } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, FormGroupDirective, NgForm } from '@angular/forms';
import { ErrorStateMatcher, MatChipInputEvent, MatDialog } from '@angular/material';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import LocationPicker from "location-picker";
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { LoadingBarService } from '@ngx-loading-bar/core';
import { DomSanitizer } from '@angular/platform-browser';

// import { InformationDialogComponent } from 'src/app/pages/data-entry/dialog/information-dialog/information-dialog.component';
import { ModalBoxComponent } from '../modal-box/modal-box.component';
import { Constants } from 'src/app/models/constants';
import { CommonService } from 'src/app/common/api/common.service';

// import { CommonService } from 'src/app/service/common.service';
declare var $: any;

@Component({
    selector: 'sdrc-form',
    templateUrl: './form.component.html',
    styleUrls: ['./form.component.css']
})
export class FormComponent implements OnChanges {

    @Input() questionArray: IQuestion[];
    photoContent: any;
    fileExtension: string[] = [];
    fileExtensionError: boolean = false;
    fileExtensionMessage: any;
    fileExtensionValidationMessage: string;
    photoForm: FormGroup;
    questionMapByColumnName: any;
    questionColumnMap: Map<String, IQuestion> = new Map();
    selectedFiles: any[] = [];
    @Input() numberOfColumn: any;
    @ViewChild('inputFile') myInputVariable: ElementRef;

    @ViewChild('f') form;

    beginSeletor: number;
    sdrcForm: FormGroup;
    confirmValidParentMatcher = new ConfirmValidParentMatcher();
    toppings = new FormControl();
    @Output() onSubmit: EventEmitter<any> = new EventEmitter<any>();
    @Output() onButtonClicked: EventEmitter<any> = new EventEmitter<any>();
    locationPicker: LocationPicker;

    backupFg:FormGroup;
    backupFgArray:FormGroup[]=[];

    //tag properties started    
    /**
     * This property will keep all the tags
     */
    tags: Tag[] = [];
    selectable = true;
    removable = true;
    addOnBlur = true;
    
    /**
     *
     * Enter key and comma will separate the tags
     * 
     */
    readonly separatorKeysCodes: number[] = [ENTER, COMMA];
    firstIntanceQuestion: any;
    progress: { percentage: number } = { percentage: 0 };
    currentFileUpload: boolean;
    srcfiles: any = 'assets/img/photo.png';
    srcFile: any;
    apiGateway: string = Constants.HOME_URL;
    //tag property ended

    testVar:boolean=false;

    constructor(private formBuilder: FormBuilder, private commonService: CommonService, public loader: LoadingBarService,
        public dom: DomSanitizer, public dialog: MatDialog) {
        this.sdrcForm = new FormGroup({});
    }

    scrollToError(): void {
        const firstElementWithError = document.querySelector('form .ng-invalid');
        this.scrollTo(firstElementWithError);
    }

    scrollTo(el: Element) {
        if (el) {
            el.scrollIntoView({ behavior: 'smooth' });
        }
    }

    async scrollIfFormHasErrors(form: FormGroup): Promise<any> {
        await form.invalid;
        this.scrollToError();
    }

    ngOnChanges() {
        if (this.questionArray) {
            this.createForm();
            this.convertQuestionarrayToMap();
        }

        if (this.questionArray.findIndex(d => d.controlType === 'geolocation') > -1) {
            setTimeout(() => {
                this.intializePicker(this.questionArray[this.questionArray.findIndex(d => d.controlType === 'geolocation')]);
            }, 2000);
        }
    }
    intializePicker(question: IQuestion) {
        if (this.questionArray.findIndex(d => d.controlType === 'geolocation') > -1) {
            this.locationPicker = new LocationPicker('map', {
                setCurrentPosition: question.value ? false : true, // You can omit this, defaults to true
            }, {
                    zoom: 15 // You can set any google map options here, zoom defaults to 15
                });
        }
        if (question.value) {
            this.locationPicker.setLocation(question.value.split(",")[0], question.value.split(",")[1]);
        }

        google.maps.event.addListener(this.locationPicker.map, 'idle', (event) => {
            // Get current location and show it in HTML
            if (question.disabled) {
                this.locationPicker.setLocation(question.value.split(",")[0], question.value.split(",")[1]);
            }
            else {
                let locationData = this.locationPicker.getMarkerPosition()
                question.value = locationData.lat + "," + locationData.lng;
            }

        });
    }
    submit(a, b) {
        this.submitClicked();
    }
    clickInfo(infoMsg) {
        // const dialogRef = this.dialog.open(InformationDialogComponent,
        //     { width: '400px', data: { msg: infoMsg } });
    }
    whiteSpaceValidator(control: FormControl) {
        if (control.value && control.value.trim() === "") {
            return {
                whiteSpace: true
            };
        }
        return null;
    }
    errors = errorMessages;

    createForm() {
        // this.intializePicker();
        let formBuilderJson: any = {};
        this.questionArray.forEach(el => {
            formBuilderJson[el.columnName] = [{ value: '', disabled: el.disabled }, []];
            if (el.required) {
                formBuilderJson[el.columnName][1].push(Validators.required)
            }
            if (el.minLength || el.minLength == 0) {
                formBuilderJson[el.columnName][1].push(Validators.minLength(el.minLength))
            }
            if (el.maxLength) {
                formBuilderJson[el.columnName][1].push(Validators.maxLength(el.maxLength))
            }
            if (el.pattern || regExps[el.type] != null) {
                formBuilderJson[el.columnName][1].push(Validators.pattern(regExps[el.type]))
            }
            if (el.type === 'text') {
                formBuilderJson[el.columnName][1].push(this.whiteSpaceValidator)
            }
            if (el.type === 'number') {
                formBuilderJson[el.columnName][1].push(Validators.min(el.min))
                if (el.max > 0)
                formBuilderJson[el.columnName][1].push(Validators.max(el.max))
            }
          
            //code for set chips predefined values start
            if (el.controlType === 'chips') {
                let values: string[] = el.value
                this.tags = []
                values.forEach(value => {
                    this.tags.push({ name: value })
                })
            }

            if (el.controlType === 'beginRepeat' || el.controlType === 'beginRepeatImageRow') {
                let k = 0;
                el.childQuestionModels.forEach(element => {

                    element.forEach(el2 => {
                        formBuilderJson[el2.indexNumberTrack] = [{ value: '', disabled: el2.disabled }, []];
                        if (el2.optionsParentColumn) {
                            el2.optionsParentColumn = el2.optionsParentColumn +'_' + k 
                        }
                        
                        if (el2.required) {
                            formBuilderJson[el2.indexNumberTrack][1].push(Validators.required)
                        }
                        if (el2.minLength || el2.minLength == 0) {
                            formBuilderJson[el2.indexNumberTrack][1].push(Validators.minLength(el2.minLength))
                        }
                        if (el2.maxLength) {
                            formBuilderJson[el2.indexNumberTrack][1].push(Validators.maxLength(el2.maxLength))
                        }
                        if (el2.pattern || regExps[el2.type] != null) {
                            formBuilderJson[el2.indexNumberTrack][1].push(Validators.pattern(regExps[el2.type]))
                        }
                       
                        if (el2.type === 'text') {
                            formBuilderJson[el2.indexNumberTrack][1].push(this.whiteSpaceValidator)
                        }

                        if (el2.type === 'number') {
                            formBuilderJson[el2.indexNumberTrack][1].push(Validators.min(el.min))
                            if(el2.max>0)
                            formBuilderJson[el2.indexNumberTrack][1].push(Validators.max(el.max))
                        }
                        //code for set chips predefined values start
                        if (el.controlType === "chips") {
                            let values: string[] = el.value
                            this.tags = []
                            values.forEach(value => {
                                this.tags.push({ name: value })
                            })
                        }
                    });
                    k++;

                });
                this.beginSeletor = k;

            }


            //code for set chips predefined values end
        });
        this.sdrcForm = this.formBuilder.group(formBuilderJson)
        // this.form.resetForm();

    }

    register(): void {
        // API call to register your user
    }
  
    ddValueReset(fieldMain,field1){
      
        if(fieldMain[1].optionsParentColumn==field1.indexNumberTrack)
         {
          fieldMain[1].value=null;
         }
          
      }

    convertExtToAcceptString(exten: string[]): string {
        let acceptString: string = "";
        exten.forEach(element => {
            acceptString += ".".concat(element + ",")
        });
        return acceptString.substring(0, acceptString.length - 1);
    }
    viewFile(tag) {
        this.srcFile = null;
        this.srcFile = this.dom.bypassSecurityTrustResourceUrl(Constants.HOME_URL + 'bypass/doc?fileId=' + tag.attachmentId);
        if (this.srcFile != null) {
            const dialogRef = this.dialog.open(ModalBoxComponent,
                { width: '100%', height: '100%', data: { msg: this.srcFile } });
            //$('#myModal').modal('show');
        }
    }
    /*file uploading -----------------------------------------------------------------------------------------------------------------*/
    fileUpload(event, extension, field: IQuestion, fieldSize): any {

        field.fileValues = [];
        this.fileExtension = [];
        var file = event.target.files;
        for (var i = 0; i < file.length; i++) {
            this.fileExtension.push(file[i].name.split('.').pop());
        }
        var allowedExtensions = extension;
        let extensionNames: string = "";
        field.fileExtensionValidationMessage = "";
        if (allowedExtensions.length > 1) {
            extensionNames = "("
            allowedExtensions.forEach(element => {
                extensionNames += element + '/';
            });
            extensionNames = extensionNames.substring(0, extensionNames.length - 1) + ")"
        } else {
            extensionNames = allowedExtensions[0];
        }
        if (this.isInArray(allowedExtensions, this.fileExtension)) {
            this.fileExtensionMessage = ""
            this.fileExtensionValidationMessage = "";
        } else {
            // this.reset();
            if (field.multiple) {
                field.fileExtensionValidationMessage = "(only " + extensionNames + " files are accepted.)";
            } else {
                field.fileExtensionValidationMessage = "(required a " + extensionNames + " file.)";
            }
            field.value = "";
        }
        if (file) {
            if (this.isInArray(allowedExtensions, this.fileExtension) && !this.checkSize(file, fieldSize)) {

                this.currentFileUpload = true;
                for (let f of file) {
                    field.fileValues.push(f)
                    this.commonService.uploadFile(f, field.groupParentId ? field.indexNumberTrack : field.columnName).subscribe(events => {
                        this.loader.stop();
                        if (events.type === HttpEventType.UploadProgress) {
                            this.progress.percentage = Math.round(
                                (100 * events.loaded) / events.total
                            );
                        } else if (events instanceof HttpResponse) {
                            this.currentFileUpload = false;
                            if (field.value && field.multiple) {
                                field.value.push(events.body)
                            }
                            else {
                                if (!field.multiple && field.value && field.value.length > 0) {
                                    if (field.deletedFileValue) {
                                        field.value.forEach(element => {
                                            field.deletedFileValue.push(element)
                                        })
                                    }
                                    else {
                                        field.deletedFileValue = [];
                                        field.value.forEach(element => {
                                            field.deletedFileValue.push(element)
                                        })

                                    }
                                }
                                field.value = [];
                                field.value.push(events.body)
                            }
                            //this.validateResponse = null;
                            //this.validated = false;
                            //this.finalUpload = events.body;
                            this.progress.percentage = 0;
                        }
                    }, error => {
                        this.currentFileUpload = false;

                        //this.validateResponse = null;
                        //this.validated = false;
                        this.progress.percentage = 0;
                        // this.errorMessage = Constants.SERVER_ERROR_MESSAGE;
                    });
                }

                field.fileExtensionValidationMessage = "";
            } else {
                // field.value = "";
                if (field.multiple)
                    field.fileExtensionValidationMessage = "(only " + extensionNames + " files are accepted and no file should exceed " + fieldSize / 1000 + " KB)";
                else
                    field.fileExtensionValidationMessage = "(required a " + extensionNames + " file within " + fieldSize / 1000 + " KB)";
            }
        } else {
            alert("Failed to load file");
        }
    }

    /**
     * this function checks whether any file exceeds the given size or not
     */
    checkSize(file: File[], fieldSize: number): boolean {
        let count = 0;
        for (var i = 0; i < file.length; i++) {
            if (file[i].size > fieldSize) count++;
        }
        if (count > 0) return true; else return false;
    }

    /**
     * returns true if the received extensions are belongs to allowed extensions else false.
     */
    isInArray(allowedExtensions: string[], receivedExtensions: string[]): boolean {
        return allowedExtensions.filter(val => receivedExtensions.includes(val)).length == 0 ? false : true;
    }
    // tslint:disable-next-line:jsdoc-format
    /**file upload complete ----------------------------------------------*/





    /* COVERT QUESTION ARRY TO MAP WITH COLOUM NAME KEY*/
    convertQuestionarrayToMap() {
        this.questionMapByColumnName = {};
        
        for (let i = 0; i < this.questionArray.length; i++) {
            this.questionMapByColumnName[this.questionArray[i].columnName] = this.questionArray[i];
            if(this.questionArray[i].childQuestionModels != null){
                for(let childIndex = 0; childIndex<this.questionArray[i].childQuestionModels.length ; childIndex++)
                {
                    for (let index = 0; index < this.questionArray[i].childQuestionModels[childIndex].length; index++) {
                        
                        this.questionMapByColumnName[this.questionArray[i].childQuestionModels[childIndex][index].columnName+ '_' +childIndex ] = this.questionArray[i].childQuestionModels[childIndex][index];
                       
                   }
            }
            }
        }
        
    }
   

    //clears all validations while an input field is hidden/removed
    removeAllValidations(field) {
        const fieldControl = this.sdrcForm.get(field.indexNumberTrack ? field.indexNumberTrack : field.columnName);
        if (field.controlType === 'file') {
            field.value = '';
            if (field.indexNumberTrack) {
                $('#fileToUpload' + field.indexNumberTrack).val(null);
            } else {
                $('#fileToUpload' + field.columnName).val(null);
            }
        }
        fieldControl.clearValidators();
        fieldControl.updateValueAndValidity();
    }
    // set all validation while input field is shown
    setAllValidations(field) {
        const fieldControl = this.sdrcForm.get(field.indexNumberTrack ? field.indexNumberTrack : field.columnName);
        let validatorArray = [];

        if (field.required) {
            validatorArray.push(Validators.required);
        }
        if (field.minLength || field.minLength === 0) {
            validatorArray.push(Validators.minLength(field.minLength));
        }
        if (field.maxLength) {
            validatorArray.push(Validators.maxLength(field.maxLength));
        }
        if (regExps[field.type] != null) {
            validatorArray.push(Validators.pattern(regExps[field.type]));
        }
        // Validators.compose([Validators.required, Validators.minLength(3)]);
        fieldControl.setValidators(Validators.compose(validatorArray));
        fieldControl.updateValueAndValidity();
    }

    


    checkIsDependencyCondition(field) {
        
        $('#' + field.key).addClass("paddingLeft")
        let flag = false;
        for (let i = 0; i < field.dependentCondition.length; i++) {
            const condition: string = field.dependentCondition[i];
            const parentColumnName = field.parentColumns[i];

            if (condition.indexOf('#') > -1 && condition.split('#')[0] == 'isDependencyValue') {
              
                if (this.questionMapByColumnName[parentColumnName+ '_' + field.indexNumberTrack.split('_')[1]].value != condition.split('#')[1]) {
                   
                    flag = true;
                }
            } else if (condition.indexOf('#') > -1 && condition.split('#')[0] === 'isDependencyValueRepeat') {
                if (this.sdrcForm.controls[parentColumnName + '_' + field.indexNumberTrack.split('_')[1]].value !==
                    condition.split('#')[1]) {
                    flag = true;
                }
            } else if (condition.indexOf('#') > -1 && condition.split('#')[0] === 'notDependencyValue') {
                if (this.questionMapByColumnName[parentColumnName].value == condition.split('#')[1]) {
                    flag = true;
                }
            } else if (condition.indexOf('@') > -1 && condition.split('@')[0] === 'isDependencyValue') {
                const exp = "this.questionMapByColumnName['" + parentColumnName + "']" + condition.split('@')[1]

                if (!this.questionMapByColumnName[parentColumnName].value) {
                    flag = true;
                } else if (!eval(exp)) {
                    flag = true;

                }
            } else if (condition.includes('isDependencyValueCalculateAge')) {
                if (this.questionMapByColumnName[parentColumnName].value) {
                    const timeDiff = Math.abs(new Date().getTime() - Date.parse(this.questionMapByColumnName[parentColumnName].value));
                    this.questionMapByColumnName[field.columnName].value = Math.floor((timeDiff / (1000 * 3600 * 24)) / 365);
                }
                flag = false;
            } else if (condition.includes('disabled')) {
                if (this.questionMapByColumnName[parentColumnName].value === condition.split('#')[1]) {
                    this.sdrcForm.controls[field.columnName].disable();
                } else {
                    this.sdrcForm.controls[field.columnName].enable();
                }
                flag = false;
            } else if (condition.includes('notDisabled')) {
                if (this.questionMapByColumnName[parentColumnName].value !== condition.split('#')[1]) {
                    this.sdrcForm.controls[field.columnName].disable();
                } else {
                    this.sdrcForm.controls[field.columnName].enable();
                }
                flag = false;
            }

        }
        if (flag) {
            field.value = "";
            this.removeAllValidations(field);
        } else {
            this.setAllValidations(field);
        }
        return flag;
    }



    convertToDate(date: string): Date | null {
        if (date == 'today')
            return new Date();
        else if (date && date.includes('@'))
            return this.questionArray.filter(d => d.columnName == date.split('@')[1])[0].value
        else if (date)
            return new Date(date);
        else
            return null
    }

    public submitClicked(): void {
        this.onSubmit.emit({
            "questionArray": this.questionArray
        });
    }

    public buttonClicked(field): void {
        this.onButtonClicked.emit({
            field: field
        });
    }
    public buttonClickedforBeginRepeatRemove(i) {
 
        if(this.backupFgArray.length>0){
[this.backupFgArray.length-1]
            for (let control in  this.backupFgArray[this.backupFgArray.length-1].controls)
            {
                
                this.sdrcForm.removeControl(control);
            
            }
            this.backupFgArray.pop();
        }
       
        this.questionArray[i].childQuestionModels.pop();
        this.convertQuestionarrayToMap();
        this.beginSeletor--;
        // this.ngOnChanges();
    }
    public buttonClickedforBeginRepeat(objlist): void {
        // let lastNum: number = parseInt(objlist[objlist.length - 1][0].indexNumberTrack.split(objlist[objlist.length - 1][0].columnName + '_')[1])
        let k: number = this.beginSeletor++;
        let firstIntanceQuestion: IQuestion[] = [];
        objlist[0].forEach((element: IQuestion) => {
            let data: IQuestion = {
                allChecked:element.allChecked,
                childQuestionModels: element.childQuestionModels,
                columnName:element.columnName,
                controlType:element.controlType,
                currentDate:element.currentDate,
                deletedFileValue:element.deletedFileValue,
                dependentCondition:element.dependentCondition,
                disabled: element.disabled,
                fileExtension: element.fileExtension,
                fileExtensionValidationMessage: element.fileExtensionValidationMessage,
                fileSize: element.fileSize,
                fileValues: element.fileValues,
                groupParentId: element.groupParentId,
                indexNumberTrack: element.columnName + '_' + k,
                infoAvailable:element.infoAvailable,
                infoMessage:element.infoMessage,
                key:element.key,
                label: element.label,
                maxDate:element.maxDate,
                maxLength:element.maxLength,
                minDate: element.minDate,
                minLength: element.minLength,
                multiple:element.multiple,
                options: element.options,
                optionsParentColumn:element.optionsParentColumn ,
                parentColumns: element.parentColumns,
                pattern: element.pattern,
                placeHolder: element.placeHolder,
                removable: true,
                required:element.required,
                selectAllOption: element.selectAllOption,
                serialNumb:element.serialNumb,
                siNo: null,
                triggable: false,
                type:element.type,
                value:element.disabled && element.controlType == 'datepicker' ? element.value : null,
                

            }
           
            firstIntanceQuestion.push(data);
           
        });
       
        // Array.prototype.push.apply(firstIntanceQuestion,objlist[0]);
        this.questionArray.filter(d =>
             d.columnName === objlist[0][0].groupParentId)[0].childQuestionModels.push(firstIntanceQuestion);
             if(objlist[0][0].groupParentId.split('_')[0]!=null)
        this.questionMapByColumnName[objlist[0][0].groupParentId]=firstIntanceQuestion;
        this.convertQuestionarrayToMap();
        let formBuilderJson: any = {};

        firstIntanceQuestion.forEach(el2 => {
            if(el2.columnName!=null){
               
                formBuilderJson[el2.columnName + '_' + k] = ['', []];
                              
                if (el2.required) {
                    formBuilderJson[el2.columnName + '_' + k][1].push(Validators.required)
                    
                }
                if (el2.minLength || el2.minLength == 0) {
                    formBuilderJson[el2.columnName + '_' + k][1].push(Validators.minLength(el2.minLength))
                    
                }
                if (el2.maxLength) {
                    formBuilderJson[el2.columnName + '_' + k][1].push(Validators.maxLength(el2.maxLength))
                    
                }
                if (el2.pattern) {
                    formBuilderJson[el2.columnName + '_' + k][1].push(Validators.pattern(regExps[el2.pattern]))
                    
                }
    
                if (el2.pattern || regExps[el2.type] != null) {
                    formBuilderJson[el2.columnName + '_' + k][1].push(Validators.pattern(regExps[el2.type]))
                   
                }
                if (el2.type === 'text') {
                    formBuilderJson[el2.columnName + '_' + k][1].push(this.whiteSpaceValidator)
                    
                }
    
                if (el2.type === 'number') {
                    formBuilderJson[el2.columnName + '_' + k][1].push(Validators.min(el2.min))
                    
                    if (el2.max > 0)
                    formBuilderJson[el2.columnName + '_' + k][1].push(Validators.max(el2.max))
                    
                }
    
                if (el2.optionsParentColumn) {
                    el2.optionsParentColumn = el2.optionsParentColumn.substring(0,2) +'_' + k
                    
                }
            }

        });
        // this.sdrcForm.invalid=false
        let formGroup: FormGroup = this.formBuilder.group(formBuilderJson);
        this.backupFg=formGroup;
        this.backupFgArray.push(formGroup);
        
        for (let control in formGroup.controls)
        {
        
            // control[status]="VALID"
            this.sdrcForm.addControl(control, formGroup.controls[control])
        }
       
            

    }
    public setNewQuestionSet(field) {
        this.firstIntanceQuestion = field;

        Array.prototype.push.apply(this.questionArray, field);
    }
   

    radioChange(val, field) {
        field.value = val
    }

    /**
 *
 * Add a tag to tag list
 * 
 */
    add(event: MatChipInputEvent, key: number): void {
        const input = event.input;
        const value = event.value;

        // Add our fruit
        if ((value || '').trim()) {
            this.tags.push({ name: value.trim() });
            this.questionArray[this.questionArray.findIndex(d => d.key == key)].value.push(value.trim())
        }

        // Reset the input value
        if (input) {
            input.value = '';
        }
    }

    /**
     * Remove a tag from the tag list
     * 
     */
    //   @ViewChild('chipList') chipList;
    remove(tag: Tag, field): void {
        const index = this.tags.indexOf(tag);

        if (index >= 0) {
            this.tags.splice(index, 1);
            this.questionArray[this.questionArray.findIndex(d => d.key == field.key)].value.splice(index, 1);
        }
        if (this.tags.length === 0 && field.required) {
            // this.chipList.errorState = true;
            this.sdrcForm.controls[field.columnName].setErrors({ 'required': true });
        }
    }

    removeFile(tag: Tag, field): void {
        const index = field.value.indexOf(tag);
        if (!field.deletedFileValue) {
            field.deletedFileValue = []
        }
        field.deletedFileValue.push(tag);
        if (index >= 0) {
            field.value.splice(index, 1);
            
        }
        if (field.value.length === 0 && field.required) {
            
            this.sdrcForm.controls[field.columnName].setErrors({ 'required': true });
        }
    }
    /**
     * checkbox change event handled
     */
    checkboxChange(item, field) {
        item.checked = !item.checked;
        if (field.required) {
            let count: number = 0;
            field.options.forEach(element => {
                if (element.checked)
                    count++;
            })
            if (count == 0)
                this.sdrcForm.controls[field.columnName].setErrors({ 'required': true });
        }
    }

    /**
     * select all option in multiselect
     */
    checkUncheckAllSelection(field) {
        field.allChecked = !field.allChecked;
        if (field.allChecked) {
            let allOptionKeys = [];
            field.options.forEach(opt => {
                if (allOptionKeys.indexOf(opt.key) == -1) {
                    allOptionKeys.push(opt.key)
                }
            });
            field.value = allOptionKeys;
        }
        else {
            field.value = [];
        }

    }

    validateAllOptionSelected(field) {
        let allOptionKeys = JSON.parse(JSON.stringify(field.value));
        if (allOptionKeys.length == field.options.length) {
            return true
        }
        if (allOptionKeys.length < field.options.length) {
            return false
        }
    }

    trackByfn(index, question) {
        return question.indexNumberTrack;
    }

}
/**
 * Tag Interface
 * 
 */
export interface Tag {
    name: string;
}

export interface IQuestion {
    key: number,
    value: any,
    controlType: string,
    label: string,
    type?: string,
    required?: boolean;
    columnName: string;
    minLength?: number;
    maxLength?: number;
    pattern: string;
    options?: any[];
    minDate?: string;
    maxDate?: string;
    fileExtension?: string[];
    fileExtensionValidationMessage?: string;
    fileValues?: any[];
    deletedFileValue?: any[];
    multiple?: boolean,
    fileSize?: number,
    optionsParentColumn?: any,
    dependentCondition?: string[],
    selectAllOption?: boolean;
    allChecked?: boolean;
    disabled?: boolean,
    childQuestionModels?: any;
    groupParentId?: string;
    indexNumberTrack?: string;
    serialNumb?: string;
    parentColumns?: string[];
    placeHolder?: string;
    currentDate?: string;
    removable?: boolean;
    infoAvailable?: boolean;
    infoMessage?: string;
    min?:number;
    max?:number;
    siNo: any;
    triggable: boolean;
}




/**
 * Custom ErrorStateMatcher which returns true (error exists) when the parent form group is invalid and the control has been touched
 */
export class ConfirmValidParentMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        return control.parent.invalid && control.touched;
    }
}

/**
* Collection of reusable RegExps
*/
export const regExps: { [key: string]: RegExp } = {
    text: /^[a-zA-Z0-9. ]+$/,
    password: /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{7,15}$/,
    email: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    name: /^[a-zA-Z ]{1,50}$/,
    phone: /^([0-9]{10,11})$/,
    domiwebsite: /^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9](?:\.[a-zA-Z]{2,})+$/,
    url: /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\(\)\*\+,;=.]+$/,
    organization: /^[a-zA-Z0-9 ]{1,50}$/,
    landlinephone: /^[0-9]\d{2,4}-\d{6,8}$/,
    pincode: /^((?!(0))[0-9]{6})$/
};

export const errorMessages: { [key: string]: string } = {
    text:'Please provide valid input',
    password: 'Please provide valid password',
    email: 'Please provide valid email',
    name: 'Please provide valid name',
    phone: 'Please provide valid phone no',
    domiwebsite: 'Please provide valid website',
    url: 'Inncorect URL',
    landlinephone: 'Please provide valid phone no',
    pincode: 'Please provide valid pincode'
};
/**
 * Collection of reusable error messages
 */

