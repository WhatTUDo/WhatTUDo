import {Component, OnInit} from '@angular/core';
import {Calendar} from '../../dtos/calendar';
import {ActivatedRoute} from '@angular/router';
import {CalendarService} from '../../services/calendar.service';
import {
  faChevronDown,
  faChevronLeft,
  faChevronRight,
  faChevronUp,
  faLink,
  faPlus
} from '@fortawesome/free-solid-svg-icons';
import {CalendarEvent} from '../../dtos/calendar-event';
import {EventService} from '../../services/event.service';
import {Globals} from "../../global/globals";
import {FeedbackService} from "../../services/feedback.service";
import {CalendarBase} from "./calendar-base";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent extends CalendarBase implements OnInit {

  id: number;
  calendar: Calendar;
  events: CalendarEvent[] = [];

  color: String;

  eventsOfTheWeek: Map<String, CalendarEvent[]> = new Map<String, CalendarEvent[]>();

  dateLocale: string;

  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
  faPlus = faPlus;
  faLink = faLink;
  currentDate: number;
  currentMonth: String;
  currentYear: number;

  constructor(
    public eventService: EventService,
    public globals: Globals,
    private calendarService: CalendarService,
    private route: ActivatedRoute,
    private feedbackService: FeedbackService
  ) {
    super(eventService, globals);
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));
    this.eventService.getEventsByCalendarId(this.id).subscribe((events: CalendarEvent[]) => {
      this.events = events;
      this.loadAllEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
    });
    this.calendarService.getCalendarById(this.id).subscribe((calendar: Calendar) => {
      this.calendar = calendar;
    });
    this.dateLocale = globals.dateLocale;
  }

  ngOnInit(): void {
  }


  /**
   * Calls Service to load Events for the week.
   * @param from: Start date of week
   * @param to: End date of week
   */
  loadAllEventsForWeek(from: Date, to: Date) {
    this.events.forEach(event => {
      let startDate = new Date(event.startDateTime);
      let endDate = new Date(event.endDateTime);

      event.startDateTime = startDate;
      event.endDateTime = endDate;
    });
    this.displayingWeek.forEach((day: Date) => {
      const keyISOString = this.getMidnight(day).toISOString();
      this.eventsOfTheWeek.set(keyISOString, this.events.filter(event => {
        const isAfterMidnight = event.endDateTime.getTime() > this.getMidnight(day).getTime();
        const isBeforeEndOfDay = event.startDateTime.getTime() < this.getEndOfDay(day).getTime();
        return isAfterMidnight && isBeforeEndOfDay;
      }));
    });
  }

  redirectToAddEvent(id: number) {
    location.replace(`/form/event?calendarId=${id}`);
  }

  copyCalendarUrlToClipboard(calendarId: number) {
    const icalUrl = this.globals.backendUri + "/ical/" + calendarId + "/calendar.ics";
    this.copyMessage(icalUrl)
    this.feedbackService.displaySuccess("Copied the URL to the clipboard", icalUrl);
  }

  copyMessage(val: string) {
    const selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = val;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
  }
}
