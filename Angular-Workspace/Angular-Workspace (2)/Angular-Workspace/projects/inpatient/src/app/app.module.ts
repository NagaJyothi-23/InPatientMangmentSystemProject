import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RoomTypeComponent } from './room-type/room-type.component';
import { LoginComponent } from './login/login.component';
import { ForgotComponent } from './forgot/forgot.component';
import { SignupComponent } from './signup/signup.component';
import { PasswordChangeComponent } from './password-change/password-change.component';
import { FacilitiesComponent } from './facilities/facilities.component';
import { BedAllocationComponent } from './bed-allocation/bed-allocation.component';
import { BillingComponent } from './billing/billing.component';
import { AdminComponent } from './admin/admin.component';
import { ReceptionComponent } from './reception/reception.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { DepartmentListComponent } from './departmentlist/departmentlist.component';
import { WardComponent } from './ward/ward.component';

import { EditComponent } from './edit/edit.component';
import { RoomComponent } from './room/room.component';
import { AddroomComponent } from './addroom/addroom.component';
import { RoomKindComponent } from './room-kind/room-kind.component';
import { EditroomComponent } from './editroom/editroom.component';
import { InpatientHomeComponent } from './inpatient-home/inpatient-home.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { PatientregistrationComponent } from './patientregistration/patientregistration.component';
import { BedComponent } from './bed/bed.component';
import { DoctorComponent } from './doctor/doctor.component';
import { DoctorlistComponent } from './doctorlist/doctorlist.component';
import { EnquiryComponent } from './enquiry/enquiry.component';
import { DashboardComponent } from './componens/dashboard/dashboard.component';
import { MainComponent } from './componens/main/main.component';
import { AddpatientComponent } from './addpatient/addpatient.component';
import { MatDialogModule } from '@angular/material/dialog';
import { PopupsComponent } from './popups/popups.component';
import { EditbedallocationComponent } from './editbedallocation/editbedallocation.component';
import{MatSnackBarModule} from '@angular/material/snack-bar'
import { AuthInterceptor } from './Auth/AuthInterceptor';
import { AuthGuard } from './Auth/AuthGuard';
import { Test1Component } from './test1/test1.component';
import {MatInputModule} from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import { MatTableModule} from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule} from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AdddepartmentComponent } from './adddepartment/adddepartment.component';
import { AddbedComponent } from './addbed/addbed.component';
@NgModule({
  declarations:[
    AppComponent,
    RoomTypeComponent,
    LoginComponent,
    ForgotComponent,
    SignupComponent,
    PasswordChangeComponent,
    FacilitiesComponent,
    BedAllocationComponent,
    BillingComponent,
    AdminComponent,
    ReceptionComponent,
    DepartmentListComponent,
    WardComponent,
    EditComponent,
    RoomComponent,
    AddroomComponent,
    RoomKindComponent,
    EditroomComponent,
    InpatientHomeComponent,
    PatientregistrationComponent,
    BedComponent,
    DoctorComponent,
    DoctorlistComponent,
    EnquiryComponent,
    DashboardComponent,
    MainComponent,
    AddpatientComponent,
    PopupsComponent,
    EditbedallocationComponent,
    Test1Component,
    AdddepartmentComponent,
    AddbedComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatDialogModule,
    MatTableModule,
    MatIconModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatSnackBarModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule

  ],
  providers: [
    provideAnimationsAsync(),
    AuthGuard,
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true, }
  ],
  bootstrap: [RoomTypeComponent]
})
 export class AppModule { }

