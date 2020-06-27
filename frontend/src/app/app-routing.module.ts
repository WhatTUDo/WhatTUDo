import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {EventComponent} from "./components/event/event.component";
import {EventFormComponent} from "./components/event-form/event-form.component";
import {CalendarSearchComponent} from './components/calendar-search/calendar-search.component';
import {CalendarComponent} from './components/calendar/calendar.component';


import {OrganizationFormComponent} from './components/organization-form/organization-form.component';
import {UserComponent} from './components/user/user.component';
import {OrganizationComponent} from "./components/organization/organization.component";
import {UserFormComponent} from './components/user-form/user-form.component';

import {CalendarFormComponent} from './components/calendar-form/calendar-form.component';
import {OrganizationSearchComponent} from "./components/organization-search/organization-search.component";
import {ExplanationComponent} from "./components/explanation/explanation.component";
import {UserRegistrationComponent} from "./components/user-registration/user-registration.component";
import {AdminDashboardComponent} from "./components/admin-dashboard/admin-dashboard.component";
import {EventSearchComponent} from "./components/event-search/event-search.component";


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'calendar-list', component: CalendarSearchComponent},
  {path: 'calendar/:id', component: CalendarComponent},
  {path: 'event/:id', component: EventComponent},
  {path: 'event-search', component: EventSearchComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'organization/:id', component: OrganizationComponent},
  {path: 'organization-list', component: OrganizationSearchComponent},
  {path: 'form/event', component: EventFormComponent},
  {path: 'form/event/:id', component: EventFormComponent},
  {path: 'form/organization', component: OrganizationFormComponent},
  {path: 'form/organization/:id', component: OrganizationFormComponent},
  {path: 'form/user', component: UserFormComponent},
  {path: 'form/calendar', component: CalendarFormComponent},
  {path: 'form/calendar/:id', component: CalendarFormComponent},
  {path: 'explanation/:pageName', component: ExplanationComponent},

  {path: 'user', component: UserComponent},
  {path: 'register', component: UserRegistrationComponent},
  {path: 'admin', component: AdminDashboardComponent}


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
