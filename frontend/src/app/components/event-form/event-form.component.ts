import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {CalendarEvent} from "../../dtos/calendar-event";
import {Location} from "../../dtos/location";
import {EventService} from "../../services/event.service";
import {CalendarService} from "../../services/calendar.service";
import {Calendar} from "../../dtos/calendar";
import {ActivatedRoute, Router} from "@angular/router";
import {faChevronLeft, faCheckCircle} from "@fortawesome/free-solid-svg-icons";
import {faCircle} from "@fortawesome/free-regular-svg-icons";
import {FeedbackService} from "../../services/feedback.service";
import {CollisionResponse} from "../../dtos/collision-response";
import {EventCollisionService} from "../../services/event-collision.service";
import {Label} from '../../dtos/label';
import {Globals} from "../../global/globals";
import {LocationService} from "../../services/location.service";


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

  calendarEvent: CalendarEvent = new CalendarEvent(null, null, null, null, null, null, null, null, null, null, null);

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
  private selectedImage: File;
  location: Location;

  constructor(
    private eventService: EventService,
    private eventCollisionService: EventCollisionService,
    private calendarService: CalendarService,
    private feedbackService: FeedbackService,
    private locationService: LocationService,
    public globals: Globals,
    private route: ActivatedRoute,
    private router: Router) {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id) {
      this.eventService.getEvent(id).subscribe((event: CalendarEvent) => {
        if (event) {
          this.calendarEvent = event;
          this.isUpdate = true;
          this.getEventLabels(id);
          this.ev_id = id;
          this.calendarEvent.coverImageUrl = globals.backendUri + this.calendarEvent.coverImageUrl;

          this.locationService.getLocation(event.locationId).subscribe(location => {
            this.location = location;
          })
        }
      });
    } else {
      this.selectedLabels = [];
    }
    const calendarId = +this.route.snapshot.queryParamMap?.get('calendarId');
    if (calendarId) {
      this.calendarEvent.calendarId = calendarId;
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
    let validationIsPassed = this.validateFormInput(this.calendarEvent);
    console.log(JSON.stringify(this.calendarEvent));
    if (validationIsPassed) {
      // submit to eventService
      if (this.isUpdate) {
        this.eventService.putEvent(this.calendarEvent).subscribe(response => {
            console.log("Updated event: " + response);
            this.calendarEvent = response;
            this.feedbackService.displaySuccess("Updated Event", "You updated the event successfully!");
            console.log(response);
            this.eventService.addLabels(this.ev_id, this.selectedLabels);
            this.uploadImage();
            // this.router.navigate([`/event/${response.id}`])
          },
          err => {
            console.warn(err);
            this.feedbackService.displayError("Error", err.error.message);
          });
      } else {

        this.eventService.postEvent(this.calendarEvent).subscribe((response) => {
            console.log("Saved event: " + response);
            this.feedbackService.displaySuccess("Saved Event", "You saved a new Event!");
            console.log(response);

            this.calendarEvent = response;

            this.eventService.addLabels(response.id, this.selectedLabels);
            this.uploadImage();
            // this.router.navigate([`/event/${response.id}`])
          },
          err => {
            console.warn(err);
            this.feedbackService.displayError("Error", err.error.message);
          });
      }
    }
  }

  deleteEvent() {
    if (confirm(`You are deleting "${this.calendarEvent.name}". Are you sure?`)) {
      this.eventService.deleteEvent(this.calendarEvent.id).subscribe(() => {
        this.feedbackService.displaySuccess("Successfully deleted", "Event " + this.calendarEvent.name + " is deleted");
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
    if (!this.location) {
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
    if (location) {
      this.locationService.saveLocation(location).subscribe((location) => {
        this.location = location;
        this.calendarEvent.locationId = location.id;
      })
    } else {
      this.location = null;
    }
  }

  getAllEditableCalendars() {
    this.calendarService.getAllCalendars().subscribe((calendars: Calendar[]) => {
      this.editableCalendars = calendars;
      this.editableCalendars = calendars.filter(cal => cal.canEdit);
      if (!this.editableCalendars.length) {
        this.feedbackService.displayWarning('Heads up!', 'You have no permission to any calendar. Therefore, you cannot create any new events.')
      } else {
        if (!this.calendarEvent.calendarId) this.calendarEvent.calendarId = this.editableCalendars[0].id;
      }
    })
  }


  getEventConflicts() {
    if (this.calendarEvent.startDateTime && this.calendarEvent.endDateTime) {
      let helperEvent = this.calendarEvent;
      helperEvent.name = this.calendarEvent.name ? this.calendarEvent.name : "";
      helperEvent.description = this.calendarEvent.description ? this.calendarEvent.description : "";
      this.eventCollisionService.getEventCollisions(helperEvent).subscribe((collisionResponse) => {
        this.collisionResponse = collisionResponse;
        this.conflictExists = this.collisionResponse.eventCollisions.length !== 0;
        if (!this.conflictExists) {
        }
      });
    }
  }

  updateFromConflictResolver(dates: Date[]) {
    this.calendarEvent.startDateTime = dates[0];
    this.calendarEvent.endDateTime = dates[1];
    this.conflictExists = false;
  }

  selectImage(event) {
    this.selectedImage = event.target.files.item(0);
  }


  //fixme: this is still not reliable!
  uploadImage() {
    if (this.selectedImage === null) return;
    this.eventService.uploadEventCover(this.calendarEvent.id, this.selectedImage).subscribe(resp => {
      // @ts-ignore
      if (resp.url != null) {
        // @ts-ignore
        this.calendarEvent.coverImageUrl = resp.url;
      }
    });
  }
}
