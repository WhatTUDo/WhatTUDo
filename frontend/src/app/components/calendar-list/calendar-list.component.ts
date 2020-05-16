import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {CalendarService} from '../../services/calendar.service';
import {Calendar} from '../../dtos/calendar';
import {CalendarRepresentation} from '../../dtos/calendar-representation';
import { Router} from '@angular/router';
import {Observable} from 'rxjs';
import {OrganisationService} from '../../services/organisation.service';
import {Organisation} from '../../dtos/organisation';

@Component({
  selector: 'app-calendar-list',
  templateUrl: './calendar-list.component.html',
  styleUrls: ['./calendar-list.component.scss']
})
export class CalendarListComponent implements OnInit {
  list : Calendar[];
  listOrg : string[];
  list2 : CalendarRepresentation[];

  searchForm = new FormGroup( {
    name: new FormControl(''),
    organisation: new FormControl('')
  });


  constructor(private calendarService: CalendarService, private router: Router, private organisationService: OrganisationService) { }

  ngOnInit(): void {
    //call method for user specified calendar recommendation.
    // let calendar = new CalendarRepresentation( 2,"Calendar 0", ["Organisation", "Organisation3"] );
    // let calendar1 = new CalendarRepresentation(24,"Calendar1", ["Organisation1"]);
    // let calendar2 = new CalendarRepresentation( 46,"Calendar2", ["Organisation2"] );
    // this.list2 = [calendar, calendar1, calendar2] ;
    this.calendarService.getAllCalendars().subscribe((list) => {
        this.list   = list;
        for(let e of list){
          for(let a of e.organisationIds){
            this.organisationService.getById(a).subscribe((organisation:Organisation)=>{
              this.listOrg.push(organisation.name);
            },
              err => {
                console.warn(err);
              })
          }
          this.list2.push(new CalendarRepresentation(e.id, e.name, this.listOrg));
        }

      },
      err => {
        console.warn(err);
      });
  }



  onSelectCalendar(calendarRep: CalendarRepresentation){
    this.router.navigate(['/calendar', calendarRep.id])
  }

  onSelectOrganisation(organisation: string){
    this.router.navigate(['/'])
  }

  onSubmit() {
    let formValue = this.searchForm.value;
    let validationIsPassed = this.validateFormInput(formValue);
    if (validationIsPassed) {
        // submit to service
      console.log("search")
      this.calendarService.searchCalendars(formValue.name,formValue.organisation ).subscribe((list) => {
         this.list   = list;
         for(let e of list){

         }

         },
        err => {
          console.warn(err);
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
    if ((formValue.name == "") && (formValue.organisation == "")) {
      errors.push(new Error("Nothing was given to search."));
    }

    if (errors.length > 0) {
      console.warn(errors);
      let errorMessage = "";
      for (let error of errors) {
        errorMessage += error.message + " ";
      }
      alert(errorMessage)
      return false;
    }
    return true;
  }

}


