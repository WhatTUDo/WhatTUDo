import {Component, OnInit} from '@angular/core';
import {EventComment} from '../../dtos/event-comment';
import {CalendarEvent} from '../../dtos/calendar-event';
import {Location} from '../../dtos/location';
import {Label} from '../../dtos/label';
import {EventService} from '../../services/event.service';
import {LabelService} from '../../services/label.service';
import {ActivatedRoute} from '@angular/router';
import {faChevronLeft, faExternalLinkSquareAlt, faTag, faCog} from '@fortawesome/free-solid-svg-icons';
import {AttendanceStatusService} from '../../services/attendance-status.service';
import {AuthService} from '../../services/auth.service';
import {AttendanceDto} from '../../dtos/AttendanceDto';
import {User} from "../../dtos/user";
import {FeedbackService} from "../../services/feedback.service";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit {


  id: number;
  user: User = null;
  labels: Array<Label>;
  public calendarEvent: CalendarEvent;
  participants: any = {
    'attending': [],
    'interested': [],
    'declined': []
  };
  faChevronLeft = faChevronLeft;
  faTag = faTag;
  faExternalLinkSquareAlt = faExternalLinkSquareAlt;
  faCog = faCog;

  constructor(private eventService: EventService, private labelService: LabelService,
              private feedbackService: FeedbackService,
              private attendanceStatusService: AttendanceStatusService, private authService: AuthService,
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

  }

  public getEventDateAndTimeString() {
    return this.eventService.getEventDateAndTimeString(this.calendarEvent);
  }

  public participate(status: number) {
    if (!this.authService.isLoggedIn()) {
      this.feedbackService.displayWarning(`Login Required.`, 'You can only do this after you logged in.');
      return;
    }
    switch (status) {
      case 0:
        console.log(this.user);
        console.log(this.id);
        this.attendanceStatusService.create(new AttendanceDto(this.user.id, this.id, 0)).subscribe((attendance) => {
            console.log(attendance);
            this.getParticipants();
          }
        );
        console.log('You declined!');
        break;
      case 1:
        this.attendanceStatusService.create(new AttendanceDto(this.user.id, this.id, 1)).subscribe((attendance) => {
            console.log(attendance);
            this.getParticipants();
          }
        );

        console.log('You are attending!');
        break;
      case 2:
        this.attendanceStatusService.create(new AttendanceDto(this.user.id, this.id, 2)).subscribe((attendance) => {
            console.log(attendance);
            this.getParticipants();
          }
        );
        console.log('You are interested!');
        break;
      default:
        console.log('No idea what you want!');
        break;
    }
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

  getAllLabels() {

    this.eventService.getAllLabels().subscribe(labels => {
      this.labels = labels;
    });
  }

  getEventLabels(id: number) {

    this.eventService.getEventLabels(this.id).subscribe(labels => {
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
      this.calendarEvent.comments = this.getComments();
      this.calendarEvent.labels = this.getLabels();
      this.calendarEvent.location = location;
      this.calendarEvent.description = 'yololo';
      this.participants = this.getParticipants();
    }, err => {
      alert(err.message);
    });
  }

  private deleteEvent() {
    if (confirm(`You are deleting "${this.calendarEvent.name}". Are you sure?`)) {
      this.eventService.deleteEvent(this.calendarEvent).subscribe(() => {
        console.log('Event deleted');
      });
    }
  }

  private getParticipants() {
    this.attendanceStatusService.getUsersAttendingEvent(this.id).subscribe((users: any[]) => {
      this.participants.attending = users;
    });
    this.attendanceStatusService.getUsersInterestedInEvent(this.id).subscribe((users: any[]) => {
      this.participants.interested = users;
    });
    this.attendanceStatusService.getUsersDecliningEvent(this.id).subscribe((users: any[]) => {
      this.participants.declined = users;
    });
    return this.participants;
  }

  private getComments() {
    let comment1 = new EventComment(null, null, 'Leverage agile frameworks to provide a robust synopsis for high level overviews. Iterative approaches to corporate strategy foster collaborative thinking to further the overall value proposition. Organically grow the holistic world view of disruptive innovation via workplace diversity and empowerment.\n' +
      '\n', 0.85);
    let comment2 = new EventComment(null, null, 'Bring to the table win-win survival strategies to ensure proactive domination. At the end of the day, going forward, a new normal that has evolved from generation X is on the runway heading towards a streamlined cloud solution. User generated content in real-time will have multiple touchpoints for offshoring.\n' +
      '\n', 0.66);
    let comment3 = new EventComment(null, null, 'Capitalize on low hanging fruit to identify a ballpark value added activity to beta test. Override the digital divide with additional clickthroughs from DevOps. Nanotechnology immersion along the information highway will close the loop on focusing solely on the bottom line.\n' +
      '\n', 0.91);
    let array = new Array<EventComment>();
    array.push(comment1, comment2, comment3);

    return array;
  }

  private getLabels() {

    let label1 = new Label(null, 'Party', null);
    let label2 = new Label(null, 'Festl', null);
    let array = new Array<Label>();

    array.push(label1, label2);

    return array;
  }

  getEventPromoImageLink(eventId: number) {
    return this.eventService.getEventPromoImageLink(eventId);
  }
}
