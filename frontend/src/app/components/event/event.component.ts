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

  constructor(private eventService: EventService,
              private labelService: LabelService,
              private calendarService: CalendarService,
              private organizationService: OrganizationService,
              private feedbackService: FeedbackService,
              private attendanceStatusService: AttendanceStatusService,
              private authService: AuthService,
              public globals: Globals,
              private route: ActivatedRoute) {
    let id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCalendarEvent(id);
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user) => {
        this.user = user;
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
    this.attendanceStatusService.create(new AttendanceDto(this.user.name, this.id, status)).subscribe((attendance) => {
        this.getParticipants();
      }
    );
  }

  public addComment() {
    let textArea: any = document.getElementById('comment-area');
    if (textArea) {
      let comment = textArea.value;
      if (comment || comment.length > 0) {
        console.log('Comments aren\'t live yet, but here\'s what you wrote: ' + comment);
      } else {
        console.log('Could not read comment!');
      }
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
      let location = new Location(null, 'Fachschaft Informatik', 'Treitlstraße 3', '1050', 12.1234, 13.9876);
      this.calendarEvent.comments = this.getComments(id);
      this.calendarEvent.labels = this.getLabels();
      this.calendarEvent.location = location;
      this.calendarEvent.description = event.description;
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
    // TODO: Do something with backend.
    this.userParticipationStatus = null;
    this.getParticipants();
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
