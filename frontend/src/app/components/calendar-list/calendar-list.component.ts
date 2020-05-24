import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {CalendarService} from '../../services/calendar.service';
import {Calendar} from '../../dtos/calendar';
import {CalendarRepresentation} from '../../dtos/calendar-representation';
import { Router} from '@angular/router';
import {Observable} from 'rxjs';
import {OrganisationService} from '../../services/organisation.service';
import {Organisation} from '../../dtos/organisation';
import {CalendarEditComponent} from '../calendar-edit/calendar-edit.component';

@Component({
  selector: 'app-calendar-list',
  templateUrl: './calendar-list.component.html',
  styleUrls: ['./calendar-list.component.scss']
})
export class CalendarListComponent implements OnInit {
  list : Calendar[] = [];
  listOrg : string[] = [];
  list2 : CalendarRepresentation[] = [];
  newcalendar : Calendar;

  searchForm = new FormGroup( {
    name: new FormControl('')
  });


  constructor(private calendarService: CalendarService, private router: Router, private organisationService: OrganisationService) {
    this.calendarService.getAllCalendars().subscribe((list) => {
        this.list   = list;
        for(let e of list){
            for(let a of e.organisationIds){
              this.organisationService.getById(a).subscribe((organisation:Organisation)=>{
                  if(organisation.name != null){
                    this.listOrg.push(organisation.name);}
                },
                err => {
                  console.warn(err);
                })
            }
            this.listOrg = ["org1", 'org2'];
            this.list2.push(new CalendarRepresentation(e.id, e.name, this.listOrg));
          }
        },
      err => {
        alert(err.message);
      });
  }


  ngOnInit(): void {
  }



  onSelectCalendar(calendarRep: CalendarRepresentation){
    this.router.navigate(['/calendar', calendarRep.id]);
  }

  onSelectEditCalendar(calendarRep: CalendarRepresentation){
    this.router.navigate(['/edit/calendar', calendarRep.id]);
  }

  onSelectOrganisation(organisation: string){
    this.router.navigate(['/']);
  }

  onSubmit() {
    let formValue = this.searchForm.value;
    let validationIsPassed = this.validateFormInput(formValue);
    if (validationIsPassed) {
        // submit to service
      console.log("search");
      this.calendarService.searchCalendars(formValue.name ).subscribe((list) => {
         this.list   = list;
         this.list2 = [];
         for(let e of list){
               this.list2.push(new CalendarRepresentation(e.id, e.name, ))
         }

         },
        err => {
          alert(err.message);
        });
    }
  }

  /**
   *
   * @param formValue
   * returns true/false depending on whether validation succeeds.
   */
  validateFormInput(formValue: any) {
    let errors: Array<Error> = new Array<Error>();
    if ((formValue.name == "")) {
      errors.push(new Error("Nothing was given to search."));
    }

    if (errors.length > 0) {
      console.warn(errors);
      let errorMessage = "";
      for (let error of errors) {
        errorMessage += error.message + " ";
      }
      alert(errorMessage);
      return false;
    }
    return true;
  }

  add(name: string, eventIds: number[], organisationIds: number[]) {
    name = name.trim();
    if (!name) { return; }
    this.calendarService.addCalendar({name, eventIds, organisationIds} as Calendar)
      .subscribe(newcalendar => {
        this.list2.push(new CalendarRepresentation(newcalendar.id, newcalendar.name, this.listOrg));
      });
  }

  delete(id: number): void {

  this.calendarService.deleteCalendar({id} as Calendar) .subscribe(()=> {
          this.calendarService.getAllCalendars()
    .subscribe(list2 => this.list2 = list2);
    });
}

}


