import {Component, OnInit} from '@angular/core';
import {Calendar} from '../../dtos/calendar';
import {ActivatedRoute, Router} from '@angular/router';
import {CalendarService} from '../../services/calendar.service';
import {faChevronDown, faChevronLeft, faChevronRight, faChevronUp, faPlus} from '@fortawesome/free-solid-svg-icons';
import {CalendarEvent} from '../../dtos/calendar-event';
import {EventService} from '../../services/event.service';
import {Globals} from "../../global/globals";
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

  /** color classes to add **/
  calendarColors = ["blue", "green", "yellow", "orange", "red", "violet"];

  color: String;

  eventsOfTheWeek: Map<String, CalendarEvent[]> = new Map<String, CalendarEvent[]>();

  dateLocale: string;

  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
  faPlus = faPlus;
  // currentDate: number;
  // currentMonth: String;
  // currentYear: number;

  constructor(
    public eventService: EventService,
    private calendarService: CalendarService,
    private route: ActivatedRoute,
    private globals: Globals
  ) {
    super(eventService);
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));
    this.eventService.getEventsByCalendarId(this.id).subscribe((events: CalendarEvent[]) => {
      this.events = events;
      this.loadEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
    });
    this.calendarService.getCalendarById(this.id).subscribe((calendar: Calendar) => {
      this.calendar = calendar;
      this.color = this.calendarColors[this.calendar.id % this.calendarColors.length];
    });
    this.dateLocale = globals.dateLocale;
  }

  ngOnInit(): void {
    this.displayingDate = this.getDate(this.offset);
    this.displayingWeek = this.getWeek(this.offset);

    setInterval(_ => {
      Array.from(document.getElementsByClassName('calendar-event'))
        .forEach(event => {
          const time = event.getElementsByClassName('calendar-event-time')[0];
          const name = event.getElementsByClassName('calendar-event-name')[0];
          // @ts-ignore
          if (event.offsetHeight < time.scrollHeight + name.scrollHeight) {
            // @ts-ignore
            time.innerText = '…';
            // @ts-ignore
            name.innerText = '…';
          }

        });
    }, 500);
  }

  /**
   * Calls Service to load Events for the week.
   * @param from: Start date of week
   * @param to: End date of week
   */
  loadEventsForWeek(from: Date, to: Date) {
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
}
