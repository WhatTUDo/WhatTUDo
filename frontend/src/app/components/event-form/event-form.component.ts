import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {CalendarEvent} from "../../dtos/calendar-event";
import {Location} from "../../dtos/location";
import {EventService} from "../../services/event.service";
import {CalendarService} from "../../services/calendar.service";
import {Calendar} from "../../dtos/calendar";
import {ActivatedRoute} from "@angular/router";
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss']
})
export class EventFormComponent implements OnInit {
  editableCalendars: Calendar[] = [];

  isUpdate: Boolean = false;
  event: CalendarEvent = new CalendarEvent(null, null, null, null, null, null, null, null);
  reactiveEventForm = new FormGroup({
    id: new FormControl(''),
    calendarId: new FormControl(''),
    name: new FormControl(''),
    startDate: new FormControl(''),
    endDate: new FormControl(''),
    location: new FormControl(''),
    labels: new FormControl('')
  });

  constructor(private eventService: EventService, private calendarService: CalendarService, private route: ActivatedRoute) {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id) {
      this.eventService.getEvent(id).subscribe((event: CalendarEvent) => {
        if (event) {
          this.event = event;
          this.isUpdate = true;
        } else {
        }
      });
    }
    this.getAllEditableCalendars()
  }

  ngOnInit(): void {
  }

  onSubmit() {
    let formValue = this.reactiveEventForm.value;
    let validationIsPassed = this.validateFormInput(formValue);
    if (validationIsPassed) {
      this.event.name = formValue.name;
      this.event.startDateTime = new Date(formValue.startDate);
      this.event.endDateTime = new Date(formValue.endDate);
      this.event.calendarId = formValue.calendarId;
      this.event.labels = formValue.labels;


      // submit to eventService
      if (this.isUpdate) {
        this.eventService.putEvent(this.event).subscribe(response => {
            console.log("Updated event: " + response);
            console.log(response);
          },
          err => {
            console.warn(err);
            alert("Error: " + err.error.message);
          });
      } else {

        this.eventService.postEvent(this.event).subscribe(response => {
            console.log("Saved event: " + response);
            console.log(response);

            this.eventService.addLabels(1, [1]);
          },
          err => {
            console.warn(err);
            alert("Error: " + err.error.message);
          });
      }
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
    if (!formValue.calendarId) {
      errors.push(new Error("A event must belongs to a calendar."));
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

  getAllEditableCalendars() {
    this.calendarService.getAllCalendars().subscribe((calendars: Calendar[]) => {
      this.editableCalendars = calendars;
    }) //FIXME: Make me to fetch only editable calendars.
  }

  faChevronLeft = faChevronLeft;
}
