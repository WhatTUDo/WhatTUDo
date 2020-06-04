import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MessageComponent} from './components/message/message.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {WeeklyCalendarComponent} from './components/weekly-calendar/weekly-calendar.component';
import {EventComponent} from './components/event/event.component';
import {EventCommentComponent} from './components/event-comment/event-comment.component';
import {EventFormComponent} from './components/event-form/event-form.component';
import {EventLocationComponent} from './components/event-location/event-location.component';
import {CalendarComponent} from './components/calendar/calendar.component';
import {CalendarListComponent} from './components/calendar-list/calendar-list.component';
import {OrganizationFormComponent} from './components/organization-form/organization-form.component';
import {UserComponent} from './components/user/user.component';
import {OrganizationComponent} from './components/organization/organization.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { CalendarFormComponent } from './components/calendar-form/calendar-form.component';
import { OrganizationListComponent } from './components/organization-list/organization-list.component';
import { FeedbackHandlerComponent } from './components/feedback-handler/feedback-handler.component';
import {MatSelectModule} from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import {MatDatetimepickerModule, MatNativeDatetimeModule} from "@mat-datetimepicker/core";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatInputModule} from "@angular/material/input";
import {
  NgxMatDateAdapter,
  NgxMatDatetimePickerModule,
  NgxMatNativeDateModule
} from "@angular-material-components/datetime-picker";


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    MessageComponent,
    WeeklyCalendarComponent,
    EventComponent,
    EventCommentComponent,
    EventFormComponent,
    EventLocationComponent,
    CalendarListComponent,
    CalendarComponent,
    OrganizationFormComponent,
    UserComponent,
    CalendarFormComponent,
    OrganizationComponent,
    UserFormComponent,
    OrganizationListComponent,
    FeedbackHandlerComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    FontAwesomeModule,
    MatSelectModule,
    MatSliderModule,
    MatDatepickerModule,
    MatDatetimepickerModule,
    MatNativeDatetimeModule,
    MatInputModule,
    NgxMatDatetimePickerModule,
    NgxMatNativeDateModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
