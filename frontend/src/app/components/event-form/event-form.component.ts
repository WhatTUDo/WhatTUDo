import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {CalendarEvent} from "../../dtos/calendar-event";
import {EventLocationComponent} from "../event-location/event-location.component";

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.css']
})
export class EventFormComponent implements OnInit {

  private event: CalendarEvent
  reactiveEventForm = new FormGroup( {
    id: new FormControl(''),
    name: new FormControl(''),
    startTime: new FormControl(''),
    endTime: new FormControl(''),
    location: new FormControl(''),
    labels: new FormControl('')
  });

  constructor() { }

  ngOnInit(): void {
    this.event = new CalendarEvent(null, null, null, null, null, null, null, null);
  }

  onSubmit() {
    let formValue = this.reactiveEventForm.value;
    if (this.validateFormInput(formValue)) {
      this.event.name = formValue.name;
      this.event.startDate = new Date(formValue.startDate);
      this.event.endDate = new Date(formValue.endDate);

      // submit to service
    }
  }

  /**
   *
   * @param formValue
   * returns true/false depending on whether validation succeeds.
   */
  validateFormInput(formValue: any) {

    let elements = document.getElementsByClassName('form-control')
    for (let elementsKey in elements) {

    }

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

    if (errors.length > 0) {
      console.error(errors);
      let errorMessage = "";
      for (let error of errors) {
        errorMessage += error.message + " ";
      }
      alert(errorMessage)
      return false;
    }
    return true;
  }

  displayValidationMessage(id, message) {
    let element: any = document.getElementById(id);

    if (element) {
      element.addClass('form-incorrect');
      element.append("<p [class='validation-feedback']> " + message + " </p>")

    }
  }

}
