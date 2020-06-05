import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {CalendarEvent} from "../../dtos/calendar-event";
import {Location} from "../../dtos/location";
import {EventService} from "../../services/event.service";
import {CalendarService} from "../../services/calendar.service";
import {Calendar} from "../../dtos/calendar";
import {ActivatedRoute} from "@angular/router";
import {faChevronLeft, faCircle, faCheckCircle, faTag} from "@fortawesome/free-solid-svg-icons";
import {FeedbackService} from "../../services/feedback.service";
import {MatDatetimepickerModule} from "@mat-datetimepicker/core";
import {NgxMatDatetimePickerModule, NgxMatDateAdapter} from "@angular-material-components/datetime-picker";

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss']
})
export class EventFormComponent implements OnInit {
  editableCalendars: Calendar[] = [];

  isUpdate: Boolean = false;
  showFeedback: Boolean = false;

  pickerConfig: any = {
    showSeconds: 0,
    stepHour: 1,
    stepMinute: 5

  }

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

  constructor(
    private eventService: EventService,
    private calendarService: CalendarService,
    private feedbackService: FeedbackService,
    private route: ActivatedRoute) {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id) {
      this.eventService.getEvent(id).subscribe((event: CalendarEvent) => {
        if (event) {
          this.event = event;
          this.isUpdate = true;
        }
      });
    }
    this.getAllEditableCalendars()
  }

  ngOnInit(): void {
  }

  onSubmit() {
    let validationIsPassed = this.validateFormInput(this.event);
    if (validationIsPassed) {


      // submit to eventService
      if (this.isUpdate) {
        this.eventService.putEvent(this.event).subscribe(response => {
            this.feedbackService.displaySuccess("Updated Event!", response.toString());
            console.log("Updated event: " + response);
            this.feedbackService.displaySuccess("Updated Event", "You updated the event successfully!");
            console.log(response);
          },
          err => {
            console.warn(err);
            this.feedbackService.displayError("Error", err.error.message);
          });
      } else {

        this.eventService.postEvent(this.event).subscribe(response => {
            console.log("Saved event: " + response);
            this.feedbackService.displaySuccess("Saved Event", "You saved a new Event!");
            console.log(response);

            this.eventService.addLabels(1, [1]);
          },
          err => {
            console.warn(err);
            this.feedbackService.displayError("Error", err.error.message);
          });
      }
    }
  }

  /**
   *
   * @param event
   * returns true/false depending on whether validation succeeds.
   */
  validateFormInput(event: CalendarEvent) {
    let errors: Array<Error> = new Array<Error>();
    if (!event.name || event.name == "") {
      errors.push(new Error("Name cannot be null or empty!"));
    }
    if (!event.startDateTime) {
      errors.push(new Error("A start date and time must be specified!"));
    }
    if (!event.endDateTime) {
      errors.push(new Error("An end date and time must be specified!"));
    }
    if (!this.event.location) {
      errors.push(new Error("A location must be specified!"));
    }
    if (!event.calendarId) {
      errors.push(new Error("A event must belongs to a calendar."));
    }

    if (errors.length > 0) {
      console.warn(errors);
      let errorMessage = "";
      for (let error of errors) {
        errorMessage += error.message + " ";
      }
      this.showFeedback = true;
      this.feedbackService.displayError("Validation Error(s)", errorMessage);
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
  faTag = faTag;
  faCircle = faCircle;
  faCheckCircle = faCheckCircle;

}
