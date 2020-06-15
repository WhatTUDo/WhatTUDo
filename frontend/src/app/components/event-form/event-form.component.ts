import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {CalendarEvent} from "../../dtos/calendar-event";
import {Location} from "../../dtos/location";
import {EventService} from "../../services/event.service";
import {CalendarService} from "../../services/calendar.service";
import {Calendar} from "../../dtos/calendar";
import {ActivatedRoute} from "@angular/router";
import {faChevronLeft, faCheckCircle} from "@fortawesome/free-solid-svg-icons";
import {faCircle} from "@fortawesome/free-regular-svg-icons";
import {FeedbackService} from "../../services/feedback.service";
import {CollisionResponse} from "../../dtos/collision-response";
import {EventCollisionService} from "../../services/event-collision.service";
import {Label} from '../../dtos/label';

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss']
})
export class EventFormComponent implements OnInit {
  editableCalendars: Calendar[] = [];

  allLabels: Label[] = [];
  selectedLabels: Label[] = [];
  ev_id: number;
  isUpdate: Boolean = false;
  showFeedback: Boolean = false;

  pickerConfig: any = {
    showSeconds: 0,
    stepHour: 1,
    stepMinute: 5
  }

  event: CalendarEvent = new CalendarEvent(null, null, null, null, null, null, null, null, null, null, null);
  title: String = "NEW EVENT"

  conflictExists: boolean = false;

  reactiveEventForm = new FormGroup({
    id: new FormControl(''),
    calendarId: new FormControl(''),
    name: new FormControl(''),
    startDate: new FormControl(''),
    endDate: new FormControl(''),
    location: new FormControl(''),
    labelspicked: new FormControl('')
  });
  faChevronLeft = faChevronLeft;
  faCircle = faCircle;
  faCheckCircle = faCheckCircle;
  collisionResponse: CollisionResponse;

  constructor(
    private eventService: EventService,
    private eventCollisionService: EventCollisionService,
    private calendarService: CalendarService,
    private feedbackService: FeedbackService,
    private route: ActivatedRoute) {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id) {
      this.eventService.getEvent(id).subscribe((event: CalendarEvent) => {
        if (event) {
          this.event = event;
          this.isUpdate = true;
          this.title = "UPDATE EVENT";
          this.getEventLabels(id);
          this.ev_id = id;
        }
      });
    } else {
      this.selectedLabels = [];
    }
    const calendarId = +this.route.snapshot.queryParamMap?.get('calendarId');
    if (calendarId) {
      this.event.calendarId = calendarId;
    }
    this.getAllEditableCalendars()
  }

  ngOnInit(): void {

    this.getAllLabels();
  }

  getAllLabels() {

    this.eventService.getAllLabels().subscribe(labels => {
      this.allLabels = labels;
    });
  }

  getEventLabels(id: number) {

    this.eventService.getEventLabels(id).subscribe(labelspicked => {
      this.selectedLabels = labelspicked;
    });
  }

  toggleLabel(label: Label): void {
    if (this.labelIsSelected(label)) {
      this.selectedLabels = this.selectedLabels.filter(l => {
        return label.id !== l.id;
      })
    } else {
      this.selectedLabels.push(label);
    }
  }

  labelIsSelected(label: Label) {
    return this.selectedLabels.find(l => {
      return label.id === l.id
    });
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
            this.eventService.addLabels(this.ev_id, this.selectedLabels);
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

            this.eventService.addLabels(response.id, this.selectedLabels);
          },
          err => {
            console.warn(err);
            this.feedbackService.displayError("Error", err.error.message);
          });
      }
    }
  }
  private deleteEvent() {
    if (confirm(`You are deleting "${this.event.name}". Are you sure?`)) {
      this.eventService.deleteEvent(this.event.id).subscribe(() => {
        this.feedbackService.displaySuccess("Successfully deleted", "Event "+this.event.name+" is deleted");
      }, err => {
          console.warn(err);
          this.feedbackService.displayError("Error", err.error.message);
        });
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
      // this.editableCalendars = calendars.filter(cal => cal.canEdit);
      // if (this.editableCalendars) {
      //   this.feedbackService.displayWarning('Heads up!', 'You have no permission to any calendar. Therefore, you cannot create any new events.')
      // }
    })
  }


  getEventConflicts() {
    if (this.event.startDateTime && this.event.endDateTime) {
      let helperEvent = this.event;
      helperEvent.name = this.event.name ? this.event.name : "";
      helperEvent.description = this.event.description ? this.event.description : "";
      this.eventCollisionService.getEventCollisions(helperEvent).subscribe((collisionResponse) => {
        this.collisionResponse = collisionResponse;
        this.conflictExists = this.collisionResponse.eventCollisions.length !== 0;

        if (!this.conflictExists) {
          this.feedbackService.displaySuccess("No Collisions!", "No collisions were found for this Event!");
        }
      });
    } else {
      this.feedbackService.displayWarning("Event Collision", "A Start and End date must be specified")!
    }
  }

  updateFromConflictResolver(dates: Date[]) {
    this.event.startDateTime = dates[0];
    this.event.endDateTime = dates[1];
    this.conflictExists = false;
  }
}
