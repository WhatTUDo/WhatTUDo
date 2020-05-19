import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {CalendarEvent} from "../../dtos/calendar-event";
import {Location} from "../../dtos/location";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss']
})
export class EventFormComponent implements OnInit {

  private event: CalendarEvent
  reactiveEventForm = new FormGroup( {
    id: new FormControl(''),
    name: new FormControl(''),
    startDate: new FormControl(''),
    endDate: new FormControl(''),
    location: new FormControl(''),
    labels: new FormControl('')
  });

  constructor(private service: EventService) { }

  ngOnInit(): void {
    this.event = new CalendarEvent(null, null, null, null, null, null, null, null);
  }

  onSubmit() {
    let formValue = this.reactiveEventForm.value;
    let validationIsPassed = this.validateFormInput(formValue);
    if (validationIsPassed) {
      this.event.name = formValue.name;
      this.event.startDateTime = new Date(formValue.startDate);
      this.event.endDateTime = new Date(formValue.endDate);
      this.event.calendarId = 2;

      // submit to service
      this.service.postEvent(this.event).subscribe( response => {
        alert("Saved event: " + response);
        console.log(response);
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
    if (!formValue.name || formValue.name == "") {
      errors.push(new Error("Name cannot be null or empty!"));
    }
    if (!formValue.startDate || formValue.startDate == "") {
      errors.push(new Error("A start date and time must be specified!"));
    }
    if (!formValue.endDate || formValue.endDate == "") {
      errors.push(new Error("An end date and time must be specified!"));
    }
    if (!this.event.location) {
      errors.push(new Error("A location must be specified!"));
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

  saveLocationToEvent(location: Location) {
    this.event.location = location;
    if (this.event.location) {
      console.log("Saved location with name: ", this.event.location.name);
    }

  }



}
