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
import {CalendarSearchComponent} from './components/calendar-search/calendar-search.component';
import {OrganizationFormComponent} from './components/organization-form/organization-form.component';
import {UserComponent} from './components/user/user.component';
import {OrganizationComponent} from './components/organization/organization.component';
import {UserFormComponent} from './components/user-form/user-form.component';
import {CalendarFormComponent} from './components/calendar-form/calendar-form.component';
import {OrganizationSearchComponent} from './components/organization-search/organization-search.component';
import {FeedbackHandlerComponent} from './components/feedback-handler/feedback-handler.component';
import {MatSelectModule} from '@angular/material/select';
import {MatSliderModule} from '@angular/material/slider';
import {MatDatetimepickerModule, MatNativeDatetimeModule} from "@mat-datetimepicker/core";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatInputModule} from "@angular/material/input";
import {NgxMatDatetimePickerModule, NgxMatNativeDateModule} from "@angular-material-components/datetime-picker";
import {MatTableModule} from "@angular/material/table";
import {EventListComponent} from './components/event-list/event-list.component';
import {UserRegistrationComponent} from './components/user-registration/user-registration.component';
import {OverlayModule} from "@angular/cdk/overlay";
import {ExplanationComponent} from './components/explanation/explanation.component';
import {EventConflictResolverComponent} from './components/event-conflict-resolver/event-conflict-resolver.component';
import {AdminDashboardComponent} from './components/admin-dashboard/admin-dashboard.component';
import {MatRadioModule} from "@angular/material/radio";
import {EventSearchComponent} from './components/event-search/event-search.component';
import {CalendarDayViewComponent} from './components/calendar/calendar-day-view/calendar-day-view.component';
import {OrganizationListComponent} from './components/organization-list/organization-list.component';
import {CalendarListComponent} from './components/calendar-list/calendar-list.component';


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
    CalendarSearchComponent,
    CalendarComponent,
    OrganizationFormComponent,
    UserComponent,
    CalendarFormComponent,
    OrganizationComponent,
    UserFormComponent,
    OrganizationSearchComponent,
    FeedbackHandlerComponent,
    EventListComponent,
    ExplanationComponent,
    UserRegistrationComponent,
    EventConflictResolverComponent,
    AdminDashboardComponent,
    EventSearchComponent,
    CalendarDayViewComponent,
    OrganizationListComponent,
    CalendarListComponent
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
    NgxMatNativeDateModule,
    MatTableModule,
    OverlayModule,
    MatRadioModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
