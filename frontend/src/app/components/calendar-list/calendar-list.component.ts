import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {CalendarService} from '../../services/calendar.service';
import {Calendar} from '../../dtos/calendar';
import {CalendarRepresentation} from '../../dtos/calendar-representation';
import { Router} from '@angular/router';
import {Observable} from 'rxjs';


import {CalendarFormComponent} from '../calendar-form/calendar-form.component';
import {OrganizationService} from '../../services/organization.service';
import {Organization} from '../../dtos/organization';


@Component({
  selector: 'app-calendar-list',
  templateUrl: './calendar-list.component.html',
  styleUrls: ['./calendar-list.component.scss']
})
export class CalendarListComponent implements OnInit {
  list : Calendar[] = [];
  list2 : CalendarRepresentation[] = [];
  mapCalOrg  = new Map<number, Organization[]>();
  searchForm = new FormGroup( {
    name: new FormControl('')
  });


  constructor(
    private calendarService: CalendarService,
    private router: Router,
    private organizationService: OrganizationService) {
 this.getAllCalendars();
  }


  getAllCalendars() {
    this.calendarService.getAllCalendars().subscribe((list) => {
        this.list   = list;
        this.list2 = [];

        for(let e of list){
          let listOrg : Organization[] = [];
          for(let a of e.organizationIds){
            this.organizationService.getById(a).subscribe((organization:Organization)=>{
                if(organization != null){
                  listOrg.push(organization);
                }
              },
              err => {
                console.warn(err);
              })
          }
          this.mapCalOrg.set(e.id,listOrg );
          this.list2.push(new CalendarRepresentation(e.id, e.name, listOrg));
        }
      },
      err => {
        alert(err.message);
      });
  }

  ngOnInit(): void {
  }



  onSelectCalendar(calendarRep: CalendarRepresentation){
    this.router.navigate(['calendar/', calendarRep.id]);

  }

  onSelectEditCalendar(calendarRep: CalendarRepresentation){
    this.router.navigate(['/form/calendar', calendarRep.id]);
  }


  onSelectOrganization(organization: Organization){
    this.router.navigate(['organization/', organization.id]);
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
           let listOrg : Organization[] = [];
           for(let a of e.organizationIds){
             this.organizationService.getById(a).subscribe((organization:Organization)=>{ listOrg.push(organization);
             });
           }
           this.mapCalOrg.set(e.id, listOrg);
           this.list2.push(new CalendarRepresentation(e.id, e.name, listOrg));
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

   add(name: string, eventIds: number[], organizationIds: number[]) {
    name = name.trim();
    if (!name) { return; }
    this.calendarService.addCalendar({name, eventIds, organizationIds} as Calendar)
      .subscribe(newcalendar => {
        this.list2.push(new CalendarRepresentation(newcalendar.id, newcalendar.name, this.mapCalOrg.get(newcalendar.id)));
        this.getAllCalendars();
      });
  }


  delete(id: number): void {

  this.calendarService.deleteCalendar({id} as Calendar) .subscribe(()=> {
         this.getAllCalendars();
    });
}

}


