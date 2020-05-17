import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {EventComponent} from './components/event/event.component';
import {EventFormComponent} from './components/event-form/event-form.component';
import {OrganisationFormComponent} from './components/organisation-form/organisation-form.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'event/:id', component: EventComponent},
  {path: 'form/event/:id', component: EventFormComponent},
  {path: 'form/event/:id', component: OrganisationFormComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
