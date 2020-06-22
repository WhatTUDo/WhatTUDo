import {Component, OnInit} from '@angular/core';
import {EventComment} from '../../dtos/event-comment';
import {CalendarEvent} from '../../dtos/calendar-event';
import {Location} from '../../dtos/location';
import {Label} from '../../dtos/label';
import {EventService} from '../../services/event.service';
import {LabelService} from '../../services/label.service';
import {ActivatedRoute} from '@angular/router';
import {faCalendar, faChevronLeft, faCog, faTag, faTimesCircle} from '@fortawesome/free-solid-svg-icons';
import {AttendanceStatusService} from '../../services/attendance-status.service';
import {AuthService} from '../../services/auth.service';
import {AttendanceDto} from '../../dtos/AttendanceDto';
import {User} from "../../dtos/user";
import {FeedbackService} from "../../services/feedback.service";
import {Organization} from "../../dtos/organization";
import {Calendar} from "../../dtos/calendar";
import {CalendarService} from "../../services/calendar.service";
import {OrganizationService} from "../../services/organization.service";
import {AttendanceStatusPossibilities} from "../../dtos/AttendanceStatusPossibilities";
import {Globals} from "../../global/globals";
import {LocationService} from "../../services/location.service";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit {


  id: number;
  user: User = null;
  userParticipationStatus: AttendanceStatusPossibilities = undefined;
  labels: Array<Label>;
  comments: Array<EventComment>;
  public calendarEvent: CalendarEvent;
  location: Location;
  calendar: Calendar;
  calendarOrganizations: Organization[] = [];
  participants = {
    attending: [],
    interested: [],
    declined: []
  };
  faChevronLeft = faChevronLeft;
  faTag = faTag;
  faCog = faCog;
  faCalendar = faCalendar;
  faTimesCircle = faTimesCircle;
  AttendanceStatusPossibilities = AttendanceStatusPossibilities;
  attendanceStatus: AttendanceDto;
  newEventComment: EventComment = new EventComment(null, null, null, null, null);

  statusValues = {
    'DECLINED': 0,
    'ATTENDING': 1,
    'INTERESTED': 2
  }

  /** color classes to add **/
  calendarColors = ["blue", "green", "yellow", "orange", "red", "violet"];

  constructor(private eventService: EventService,
              private labelService: LabelService,
              private calendarService: CalendarService,
              private organizationService: OrganizationService,
              private feedbackService: FeedbackService,
              private attendanceStatusService: AttendanceStatusService,
              private locationService: LocationService,
              public authService: AuthService,
              public globals: Globals,
              private route: ActivatedRoute) {
    let id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCalendarEvent(id);
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user) => {
        this.user = user;
        this.attendanceStatusService.getStatus(user.id, id).subscribe((status: AttendanceDto) => {
            this.attendanceStatus = status;

            this.userParticipationStatus = status === null ? null : this.statusValues[status.status];
          }
        );
      });
    }
  }

  ngOnInit(): void {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));

    this.getEventLabels(this.id);
    this.getComments(this.id);
  }

  public getEventDateAndTimeString() {
    return this.eventService.getEventDateAndTimeString(this.calendarEvent);
  }

  public participate(status: AttendanceStatusPossibilities) {
    if (!this.authService.isLoggedIn()) {
      this.feedbackService.displayWarning(`Login Required.`, 'You can only do this after you logged in.');
      return;
    }
    this.attendanceStatusService.create(new AttendanceDto(null, this.user.name, this.id, status)).subscribe((attendance) => {
        this.attendanceStatus = attendance;
        this.userParticipationStatus = this.statusValues[attendance.status];
        this.getParticipants();
      }
    );
  }


  public addComment(input: string) {
    if (input.length > 0) {

      this.newEventComment.username = this.user.name;
      this.newEventComment.eventId = this.id;
      this.newEventComment.text = input;

      this.eventService.createComment(this.newEventComment).subscribe((comments) => {
        comments.updateDateTime = new Date(comments.updateDateTime);
        this.comments.push(comments);
      });
    }
  }

  getEventLabels(id: number) {
    this.eventService.getEventLabels(id).subscribe(labels => {
      this.labels = labels;
    });
  }


  /**
   * Loads Event with ID from Service.
   * @param id
   */
  private loadCalendarEvent(id: number) {
    this.eventService.getEvent(id).subscribe((event: CalendarEvent) => {
      this.calendarEvent = {
        ...event,
        startDateTime: new Date(event.startDateTime),
        endDateTime: new Date(event.endDateTime)
      };
      this.calendarEvent.description = event.description;
      if (event.locationId) {
        this.locationService.getLocation(event.locationId).subscribe((location) => {
          this.location = location;
        })
      }
      this.getParticipants();
      this.calendarService.getCalendarById(event.calendarId).subscribe(cal => {
        this.calendar = cal;
        cal.organizationIds.forEach(id => {
          this.organizationService.getById(id).subscribe(org => {
            this.calendarOrganizations.push(org);
          })
        })
      })
    });
  }


  getParticipants() {
    this.attendanceStatusService.getUsersAttendingEvent(this.id).subscribe((users: User[]) => {
      this.participants.attending = users;

    });
    this.attendanceStatusService.getUsersInterestedInEvent(this.id).subscribe((users: User[]) => {
      this.participants.interested = users;
    });
    this.attendanceStatusService.getUsersDecliningEvent(this.id).subscribe((users: User[]) => {
      this.participants.declined = users;
    });
  }


  resetParticipation() {
    this.attendanceStatusService.deleteStatus(this.attendanceStatus.id).subscribe(() => {
      this.userParticipationStatus = null;
      this.getParticipants();
    }, error => {
      console.warn(error);
      this.feedbackService.displayError("Error", error.error.message);
    });
  }

  private getComments(id: number) {
    this.eventService.getEventComments(id).subscribe((comments) => {
      this.comments = comments.map((comment) => {
        const updateDateTime = new Date(comment.updateDateTime);
        return {...comment, updateDateTime: updateDateTime} as EventComment;
      });
    });
  }

  deleteComment(commentid: number): void {
    if (confirm(`You are deleting the selected comment. Are you sure?`)) {
      this.eventService.deleteComment(commentid).subscribe(() => {
        this.getComments(this.id);
      });
    }
  }

  getCalendarColor(calendarId: number) {
    return this.calendarColors[calendarId % this.calendarColors.length];
  }

  eventEndsAfterNow() {
    return this.calendarEvent.endDateTime >= new Date(Date.now());
  }
}
