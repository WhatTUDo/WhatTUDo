import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CalendarEvent} from '../../dtos/calendar-event';

import {faChevronDown, faChevronLeft, faChevronRight, faChevronUp} from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";
import {SubscriptionService} from "../../services/subscription.service";
import {Globals} from "../../global/globals";
import {Calendar} from "../../dtos/calendar";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-weekly-calendar',
  templateUrl: './weekly-calendar.component.html',
  styleUrls: ['./weekly-calendar.component.scss']
})
export class WeeklyCalendarComponent implements OnInit, OnChanges {

  currentDate: number;
  currentMonth: String;
  currentYear: number;

  displayingDate: Date;
  displayingWeek: Date[]; // Starts at a monday.
  offset = 0;

  /** Change view… variables to configure: */
  /** number of rows. */
  viewBeginningAtRow = 1;
  viewEndingAtRow = 64;
  viewRowCount = this.viewEndingAtRow - this.viewBeginningAtRow;

  /** when the start and end of the grid represents. */
  viewBeginningAtTime = 8 * (60 * 60);
  viewEndingAtTime = 24 * (60 * 60) - 1;
  viewTimespan = this.viewEndingAtTime - this.viewBeginningAtTime;

  /** min row count for an event so that there's place for text. */
  viewMinRows = 8;

  /** color classes to add **/
  calendarColors = ["blue", "green", "yellow", "orange", "red", "violet"];

  @Input("subscribedOnly") filterIsActive:boolean;
  subscribeCalendarIds: number[];

  eventsOfTheWeek: Map<String, CalendarEvent[]> = new Map<String, CalendarEvent[]>();

  dateLocale: string;

  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;

  constructor(
    private eventService: EventService,
    private subscriptionService: SubscriptionService,
    private authService: AuthService,
    private globals: Globals
  ) {
    this.dateLocale = globals.dateLocale;
    this.displayingDate = this.getDate(this.offset);
    this.displayingWeek = this.getWeek(this.offset);

  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe(user => {
        this.subscriptionService.getSubscribedCalendars(user.id).subscribe(calendars => {
          this.subscribeCalendarIds = calendars.map(cal => {
            return cal.id
          });
        })
      })
    }

    this.loadEvents();
    this.updateDatetime();
    setInterval(_ => {
      this.updateDatetime();
    }, 1000);

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
        })
    }, 500)
  }

  ngOnChanges(changes: SimpleChanges) {
    this.loadEvents();
  }

  loadEvents() {
    if (this.filterIsActive) {
      let filteredEventsOfTheWeek = new Map<String, CalendarEvent[]>();
      this.eventsOfTheWeek.forEach((events, day) => {
        let filteredEvents = events.filter(event => {
          return this.subscribeCalendarIds.includes(event.calendarId)
        });
        filteredEventsOfTheWeek.set(day, filteredEvents);
      });
      this.eventsOfTheWeek = filteredEventsOfTheWeek;
    } else {
      this.loadAllEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
    }
  }

  /**
   * Calls Service to load Events for the week.
   * @param from: Start date of week
   * @param to: End date of week
   */
  loadAllEventsForWeek(from: Date, to: Date) {
    this.eventService.getMultipleEvents(from, to).subscribe((events: Array<CalendarEvent>) => {
      events.forEach(event => {
        let startDate = new Date(event.startDateTime)
        let endDate = new Date(event.endDateTime);

        event.startDateTime = startDate;
        event.endDateTime = endDate;
      });
      this.displayingWeek.forEach((day: Date) => {
        let keyISOString = this.getMidnight(day).toISOString()
        this.eventsOfTheWeek.set(keyISOString, events.filter(event => {
          const isAfterMidnight = event.endDateTime.getTime() > this.getMidnight(day).getTime();
          const isBeforeEndOfDay = event.startDateTime.getTime() < this.getEndOfDay(day).getTime();
          return isAfterMidnight && isBeforeEndOfDay;
        }));
      })
    });
  }

  updateDatetime() {
    const today = this.getToday();
    this.currentMonth = today.toLocaleString(this.globals.dateLocale, {month: 'long'});
    this.currentDate = today.getDate();
    this.currentYear = today.getFullYear();
  }

  getWeek(offset = 0) {
    const weekOffset = (this.getToday().getDay() - 1 + 7) % 7;
    const offsetWeeks = Math.floor((offset + weekOffset) / 7);
    let currentWeekDates = [];
    let today = this.getToday();

    let beginOfWeek = new Date(today);
    // % is remainder and not mod in js. Therefore -1%7=-1. This is a workaround.
    beginOfWeek.setDate(beginOfWeek.getDate() - weekOffset);

    let date = new Date(beginOfWeek);
    date.setDate(date.getDate() + offsetWeeks * 7);
    for (let i = 0; i <= 6; i++) {
      currentWeekDates.push(new Date(date));
      date.setDate(date.getDate() + 1);
    }
    return currentWeekDates;
  }

  getDate(offset = 0) {
    let date = this.getToday();
    date.setDate(date.getDate() + offset);
    return date
  }

  addOneWeek() {
    this.offset += 7;
    this.updateOffsettedDates();
  }

  minusOneWeek() {
    this.offset -= 7;
    this.updateOffsettedDates();
  }

  addOneDay() {
    this.offset++;
    this.updateOffsettedDates();
  }

  minusOneDay() {
    this.offset--;
    this.updateOffsettedDates();
  }

  getToday() {
    let today = new Date(Date.now());
    today.setHours(0, 0, 0, 0);
    return today;
  }

  isToday(date: Date) {
    return this.getToday().toDateString() == date.toDateString();
  }

  /**
   * Generate the CSS grid numbers for displaying the event at the right time.
   * Change this.view… variables to configure behavior.
   * @param event to display
   * @param forDate the displayed date
   */
  getDisplayRows(event: CalendarEvent, forDate: Date) {
    const startsOnToday = this.isOnSameDay(event.startDateTime, forDate);
    const endsOnToday = this.isOnSameDay(event.endDateTime, forDate);
    const startSecond = startsOnToday ? this.getSecondOffsetFromMidnight(event.startDateTime) : 0;
    const endSecond = endsOnToday ? this.getSecondOffsetFromMidnight(event.endDateTime) : 24 * 60 * 60;

    let startRow = Math.max(Math.floor(this.calcRow(startSecond)), this.viewBeginningAtRow);
    let endRow = Math.min(Math.floor(this.calcRow(endSecond)), this.viewEndingAtRow);

    if (endRow - startRow < this.viewMinRows) {
      const delta = this.viewMinRows - (endRow - startRow);
      const endRowOffset = delta + endRow > this.viewEndingAtRow ? this.viewEndingAtRow - endRow : delta;
      const startRowOffset = delta - endRowOffset;
      startRow += startRowOffset;
      endRow += endRowOffset;
    }
    return `${startRow}/${endRow}`
  }

  isOnSameDay(date1: Date, date2: Date) {
    return date1.toDateString() === date2.toDateString();
  }

  getSecondOffsetFromMidnight(date: Date) {
    return date.getSeconds() + (60 * date.getMinutes()) + (60 * 60 * date.getHours());
  }

  getMidnight(date: Date) {
    let midnight = new Date(date);
    midnight.setHours(0, 0, 0, 0);
    return midnight
  }

  getEndOfDay(date: Date) {
    let endOfDay = new Date(date);
    endOfDay.setHours(23, 59, 59);
    return endOfDay;
  }

  getDisplayTimeString(event: CalendarEvent) {
    if (this.isOnSameDay(event.startDateTime, event.endDateTime)) {
      return this.eventService.getDisplayTimeString(event);
    } else {
      return this.eventService.getEventDateAndTimeString(event);
    }
  }

  private updateOffsettedDates() {
    this.displayingDate = this.getDate(this.offset);
    this.displayingWeek = this.getWeek(this.offset);
    this.loadAllEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
  }

  /**
   * A helper function to do the calculation of the number of row.
   * Mapping sec from interval [viewBeginningAtTime, viewEndingAtTime] to [viewBeginningAtRow, viewEndingAtRow]
   * @param sec Time in day in seconds after midnight.
   */
  private calcRow(sec) {
    return ((sec - this.viewBeginningAtTime) / this.viewTimespan * this.viewRowCount) + this.viewBeginningAtRow;
  }

  getCalendarColor(event: CalendarEvent) {
    return this.calendarColors[event.calendarId % this.calendarColors.length];
  }
}
