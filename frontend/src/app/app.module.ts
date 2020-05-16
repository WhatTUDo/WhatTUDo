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

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { WeeklyCalendarComponent } from './components/weekly-calendar/weekly-calendar.component';
import { EventComponent } from './components/event/event.component';
import { EventCommentComponent } from './components/event-comment/event-comment.component';
import { EventFormComponent } from './components/event-form/event-form.component';
import { EventLocationComponent } from './components/event-location/event-location.component';
import { OrganisationFormComponent } from './organisation-form/organisation-form.component';

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
    OrganisationFormComponent,
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
