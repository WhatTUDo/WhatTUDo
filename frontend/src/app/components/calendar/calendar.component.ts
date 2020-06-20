import {Component, OnInit} from '@angular/core';
import {Calendar} from '../../dtos/calendar';
import {ActivatedRoute, Router} from '@angular/router';
import {CalendarService} from '../../services/calendar.service';
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

  constructor(
    public eventService: EventService,
    public globals: Globals,
    private calendarService: CalendarService,
    private route: ActivatedRoute,
  ) {
    super(eventService, globals);
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
