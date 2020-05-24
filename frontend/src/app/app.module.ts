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
import { CalendarEditComponent } from './components/calendar-edit/calendar-edit.component';


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
    CalendarEditComponent,
    OrganizationComponent,
    UserFormComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    FontAwesomeModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
