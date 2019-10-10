This package is developed by SDRC UI team with all the basic features those a table basically has. Its peer dependencies are bootstrap, jQuery and Fontawesome. Please follow the installation steps.

Steps to use SDRC Table
1. Install jQuery, fontawesome and bootstrap 


2. 
```bash
    npm i @progress/kendo-drawing @progress/kendo-angular-pdf-export ng2-search-filter ngx-pagination save-as
    npm i sdrc-table
```

3. import TableModule in your module ts
    ```js
    import { TableModule} from 'sdrc-table'
    ```

4. add TableModule to @ngModule imports
    ```js
    imports: [
        BrowserModule,
        HttpClientModule,
        TableModule
    ]
    ```

5. Use sdrc-table tag to get the table view
    ```js
    <sdrc-table 
    [id]="'tab1'"
    [rowData]="tableData" 
    [columnData]="tableColumns" 
    [maxTableHeight]="'600px'"
    [sorting]="true" 
    [sortExcludeColumn]="['action', 'rowId']"
    [isPaginate]="true"
    [itemsPerPage]="15"
    [headerFixed]="true" 
    [searchBox]="true" 
    [downloadPdf]="true" 
    [downloadExcel]="true"></sdrc-table>
    ```
    format of rowData and columnData should be like: 
    ```js
    tableData = [{
                    name: "xyz", 
                    age: 0, 
                    action:[{                
                                "controlType" : "button",
                                "type": "submit",
                                "value" :"Edit",
                                "class" : "btn btn-submit",
                                "icon" : "fa-edit"
                            }]
    }]

    tableColumns = ["name", "age", "action"]
    
    ```

6. If you have to handle the event on action button, use: 
    ```js
    (onActionButtonClicked)="yourFunction($event)"
    ```
