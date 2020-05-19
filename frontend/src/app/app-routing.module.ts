import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {EventComponent} from "./components/event/event.component";
import {EventFormComponent} from "./components/event-form/event-form.component";
import {CalendarListComponent} from './components/calendar-list/calendar-list.component';
import {CalendarComponent} from './components/calendar/calendar.component';
import {WeeklyCalendarComponent} from './components/weekly-calendar/weekly-calendar.component';
import {OrganizationFormComponent} from './components/organization-form/organization-form.component';
import {UserComponent} from './components/user/user.component';
//import {OrganizationComponent} from "./components/organization/organization.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'event/:id', component: EventComponent},
  {path: 'form/event/:id', component: EventFormComponent},
  {path: 'form/organization/:id', component: OrganizationFormComponent},
  {path: 'user', component: UserComponent},
 // {path: 'organization', component: OrganizationComponent},
  {path: 'calendar-list', component: CalendarListComponent},
  {path: 'calendar/:id', component: CalendarComponent},
  //{path: 'week-calendar/:id', component: WeeklyCalendarComponent}

  {path: 'form/event/:id', component: OrganizationFormComponent},
  {path: 'form/event/:id', component: EventFormComponent},


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
