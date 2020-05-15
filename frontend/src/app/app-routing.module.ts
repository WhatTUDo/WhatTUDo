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

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'event/:id', component: EventComponent},
  {path: 'form/event/:id', component: EventFormComponent},
  {path: 'calendar-list', component: CalendarListComponent},
  {path: 'calendar/:id', component: CalendarComponent}


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
