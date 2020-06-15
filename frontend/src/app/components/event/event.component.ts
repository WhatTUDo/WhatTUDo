import {Component, OnInit} from '@angular/core';
import {EventComment} from '../../dtos/event-comment';
import {CalendarEvent} from '../../dtos/calendar-event';
import {Location} from '../../dtos/location';
import {Label} from '../../dtos/label';
import {EventService} from '../../services/event.service';
import {LabelService} from '../../services/label.service';
import {ActivatedRoute} from '@angular/router';
import {faCalendar, faChevronLeft, faCog, faTag} from '@fortawesome/free-solid-svg-icons';
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
    AttendanceStatusPossibilities = AttendanceStatusPossibilities;

    constructor(private eventService: EventService,
                private labelService: LabelService,
                private calendarService: CalendarService,
                private organizationService: OrganizationService,
                private feedbackService: FeedbackService,
                private attendanceStatusService: AttendanceStatusService,
                private authService: AuthService,
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
            case AttendanceStatusPossibilities.DECLINED:
                console.log('You declined!');
                break;
            case AttendanceStatusPossibilities.ATTENDING:
                console.log('You are attending!');
                break;
            case AttendanceStatusPossibilities.INTERESTED:
                console.log('You are interested!');
                break
            default:
                console.log('No idea what you want!');
                break;
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
            this.calendarEvent.comments = this.getComments();
            this.calendarEvent.labels = this.getLabels();
            this.calendarEvent.location = location;
            this.calendarEvent.description = '';
            this.participants = this.getParticipants();
            this.calendarService.getCalendarById(event.id).subscribe(cal => {
                this.calendar = cal;
                cal.organizationIds.forEach(id => {
                    this.organizationService.getById(id).subscribe(org => {
                        this.calendarOrganizations.push(org);
                    })
                })
            })
        });
    }


    private getParticipants() {
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

    private getComments() {
        let comment1 = new EventComment(null, null, 'Leverage agile frameworks to provide a robust synopsis for high level overviews. Iterative approaches to corporate strategy foster collaborative thinking to further the overall value proposition. Organically grow the holistic world view of disruptive innovation via workplace diversity and empowerment.\n' +
            '\n', 0.85);
        let comment2 = new EventComment(null, null, 'Bring to the table win-win survival strategies to ensure proactive domination. At the end of the day, going forward, a new normal that has evolved from generation X is on the runway heading towards a streamlined cloud solution. User generated content in real-time will have multiple touchpoints for offshoring.\n' +
            '\n', 0.66);
        let comment3 = new EventComment(null, null, 'Capitalize on low hanging fruit to identify a ballpark value added activity to beta test. Override the digital divide with additional clickthroughs from DevOps. Nanotechnology immersion along the information highway will close the loop on focusing solely on the bottom line.\n' +
            '\n', 0.91);
        let array = new Array<EventComment>();
        // array.push(comment1, comment2, comment3);

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

    getOrganizationAvatarLink(organizationId: number, size: number) {
        return this.organizationService.getOrganizationAvatarLink(organizationId, size);
    }

}
