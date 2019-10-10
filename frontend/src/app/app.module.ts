import { ChangePasswordComponent } from './user-management/change-password/change-password.component';
import { AuthorizationGuard } from './guard/authorization.guard';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClientXsrfModule, HttpXsrfTokenExtractor } from '@angular/common/http';
import { FormsModule} from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './fragments/header/header.component';
import { FooterComponent } from './fragments/footer/footer.component';
import { LoginComponent } from './authentication/login/login.component';
import { HomeComponent } from './static/home/home.component';
import { XhrInterceptorServiceService } from './common/api/xhr-interceptor-service.service';
import { LoadingBarHttpClientModule } from "@ngx-loading-bar/http-client";
import { LoadingBarRouterModule } from "@ngx-loading-bar/router";
import { LoadingBarModule } from "@ngx-loading-bar/core";
import { NgxSpinnerModule } from 'ngx-spinner';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { AboutUsComponent } from './static/about-us/about-us.component';
import { TermOfUseComponent } from './static/term-of-use/term-of-use.component';
import { PrivacyPolicyComponent } from './static/privacy-policy/privacy-policy.component';
import { DisclamerComponent } from './static/disclamer/disclamer.component';
import { SiteMapComponent } from './static/site-map/site-map.component';
import { ExceptionComponent } from './static/exception/exception.component';
import { UserManagementModule } from './user-management/user-management.module';
import { DdmEntryComponent } from './ddm/ddm-entry/ddm-entry.component';
import { FormModule } from 'lib/public_api';
import { TableModule } from 'lib/public_api';
import { MatCardModule, MatFormFieldModule, MatInputModule, MatSelectModule } from '@angular/material';
import { AreaFilterPipe } from './filters/area-filter.pipe';
// import { ToastrModule } from 'ngx-toastr';
import { DdmReportComponent } from './ddm/ddm-report/ddm-report.component';
import { LaQkhyaComponent } from './ddm/la-qkhya/la-qkhya.component';
import { DdmServicesService } from './ddm/ddm-services.service';
import { ToastrModule } from 'ngx-toastr';
import { ContactUsComponent } from './static/contact-us/contact-us.component';



@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    LoginComponent,
    HomeComponent,
    AboutUsComponent,
    TermOfUseComponent,
    PrivacyPolicyComponent,
    DisclamerComponent,
    SiteMapComponent,
    DdmEntryComponent,
    ExceptionComponent,
    AreaFilterPipe,
    DdmReportComponent,
    LaQkhyaComponent,
    ContactUsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    HttpClientXsrfModule,
    AppRoutingModule,
    FormsModule,
    LoadingBarHttpClientModule,
    LoadingBarRouterModule,
    LoadingBarModule,
    NgxSpinnerModule,
    BrowserAnimationsModule,
    FormModule,
    TableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    ToastrModule.forRoot({
      timeOut: 10000,
      positionClass: 'toast-top-right',
      preventDuplicates: true}),
    MatCardModule,
    
    UserManagementModule
  ],
  providers: [{
     provide: HTTP_INTERCEPTORS, useClass: XhrInterceptorServiceService, multi: true },AuthorizationGuard,
    DdmServicesService],
  bootstrap: [AppComponent]
})
export class AppModule { }



