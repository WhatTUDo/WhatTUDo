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
  userParticipationStatus: AttendanceStatusPossibilities;
  labels: Array<Label>;
  comments: Array<EventComment>;
  public calendarEvent: CalendarEvent;
  public eventComment: EventComment;
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
  eventComment2: EventComment = new EventComment(null, null, null, null, null);

  constructor(private eventService: EventService,
              private labelService: LabelService,
              private calendarService: CalendarService,
              private organizationService: OrganizationService,
              private feedbackService: FeedbackService,
              private attendanceStatusService: AttendanceStatusService,
              private locationService: LocationService,
              private authService: AuthService,
              public globals: Globals,
              private route: ActivatedRoute) {
    let id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCalendarEvent(id);
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user) => {
        this.user = user;
        this.attendanceStatusService.getStatus(user.id, id).subscribe((status: AttendanceDto) => {
            this.attendanceStatus = status;
            if (status != null) {
              this.userParticipationStatus = status.status;
            }
          }
        );
      });
    }
  }

  ngOnInit(): void {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));

    this.getEventLabels(this.id);
    this.comments = this.getComments(this.id);

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
        this.getParticipants();
      }
    );
  }


  public addComment(input: string) {
    if (input.length > 0) {

      this.eventComment2.username = this.user.name;
      this.eventComment2.eventId = this.id;
      this.eventComment2.text = input;

      this.eventService.createComment(this.eventComment2).subscribe(comments => {
        this.getComments(this.id).push(this.eventComment2);
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
      this.calendarEvent = event;
      // this.location = new Location(null, 'Fachschaft Informatik', 'Treitlstraße 3', '1050', 48.199747,16.367543);
      this.calendarEvent.comments = this.getComments(id);
      this.calendarEvent.labels = this.getLabels();
      this.calendarEvent.description = event.description;
      if (event.locationId) {
        this.locationService.getLocation(event.locationId).subscribe((location) => {
          this.location = location;
        })
      }
      this.participants = this.getParticipants();
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
      if (users.find(u => u.id === this.user.id)) {
        this.userParticipationStatus = AttendanceStatusPossibilities.ATTENDING;
      }
    });
    this.attendanceStatusService.getUsersInterestedInEvent(this.id).subscribe((users: User[]) => {
      this.participants.interested = users;
      if (users.find(u => u.id === this.user.id)) {
        this.userParticipationStatus = AttendanceStatusPossibilities.INTERESTED;
      }
    });
    this.attendanceStatusService.getUsersDecliningEvent(this.id).subscribe((users: User[]) => {
      this.participants.declined = users;
      if (users.find(u => u.id === this.user.id)) {
        this.userParticipationStatus = AttendanceStatusPossibilities.DECLINED;
      }
    });
    this.userParticipationStatus = null;
    return this.participants;
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
    this.eventService.getEventComments(id).subscribe(comments => {
      this.comments = comments;
    });
    return this.comments;
  }

  deleteComment(commentid: number): void {
    if (confirm(`You are deleting the selected comment. Are you sure?`)) {
      this.eventService.deleteComment(commentid).subscribe(() => {
        this.getComments(this.id);
      });
    }
  }

  private getLabels() {

    let label1 = new Label(null, 'Party', null);
    let label2 = new Label(null, 'Festl', null);
    let array = new Array<Label>();

    array.push(label1, label2);

    return array;
  }

  getOrganizationAvatarLink(organizationId: number, size: number) {
    return this.organizationService.getOrganizationAvatarLink(organizationId, size);
  }
}
