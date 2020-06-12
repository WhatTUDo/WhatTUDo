import {Component, OnInit} from '@angular/core';
import {Calendar} from '../../dtos/calendar';
import {ActivatedRoute, Router} from '@angular/router';
import {CalendarService} from '../../services/calendar.service';
import {faChevronDown, faChevronLeft, faChevronRight, faChevronUp, faPlus} from '@fortawesome/free-solid-svg-icons';
import {CalendarEvent} from '../../dtos/calendar-event';
import {EventService} from '../../services/event.service';
import {Globals} from "../../global/globals";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit {

  id: number;
  calendar: Calendar;
  events: CalendarEvent[] = [];

  displayingDate: Date;
  displayingWeek: Date[]; // Starts at a monday.
  offset = 0;

  viewBeginningAtRow = 1;
  viewBeginningAtTime = 8 * (60 * 60);
  viewEndingAtRow = 64;
  viewEndingAtTime = 24 * (60 * 60) - 1;
  viewMinRows = 8;

  viewTimespan = this.viewEndingAtTime - this.viewBeginningAtTime;
  viewRowCount = this.viewEndingAtRow - this.viewBeginningAtRow;

  eventsOfTheWeek: Map<String, CalendarEvent[]> = new Map<String, CalendarEvent[]>();

  dateLocale: string;

  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
  faPlus = faPlus;

  constructor(
    private eventService: EventService,
    private calendarService: CalendarService,
    private route: ActivatedRoute,
    private globals: Globals
  ) {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));
    this.eventService.getEventsByCalendarId(this.id).subscribe((events: CalendarEvent[]) => {
      this.events = events;
      this.loadEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
    });
    this.calendarService.getCalendarById(this.id).subscribe((calendar: Calendar) => {
      this.calendar = calendar;
    }, err => {
      console.warn(err);
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
    // for (const eventId of this.calendar.eventIds) {
    //   this.eventService.getEvent(eventId).subscribe((event: CalendarEvent) => {
    //     const startDate = new Date(event.startDateTime);
    //     const endDate = new Date(event.endDateTime);
    //     event.startDateTime = startDate;
    //     event.endDateTime = endDate;
    //     this.events.push(event);
    //   }, error => {
    //     console.warn('Event does not exist');
    //   });
    // }
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

  getWeek(offset = 0) {
    const offsetWeeks = Math.round(offset / 7);
    const currentWeekDates = [];
    const today = this.getToday();

    const beginOfWeek = new Date(today);
    // % is remainder and not mod in js. Therefore -1%7=-1. This is a workaround.
    const weekOffset = (today.getDay() - 1 + 7) % 7;
    beginOfWeek.setDate(beginOfWeek.getDate() - weekOffset);

    const date = new Date(beginOfWeek);
    date.setDate(date.getDate() + offsetWeeks * 7);
    for (let i = 0; i <= 6; i++) {
      currentWeekDates.push(new Date(date));
      date.setDate(date.getDate() + 1);
    }
    return currentWeekDates;
  }

  getDate(offset = 0) {
    const date = this.getToday();
    date.setDate(date.getDate() + offset);
    return date;
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
    const today = new Date(Date.now());
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
    const midnight = new Date(date);
    midnight.setHours(0, 0, 0, 0);
    return midnight;
  }

  getEndOfDay(date: Date) {
    const endOfDay = new Date(date);
    endOfDay.setHours(23, 59, 50);
    return endOfDay;
  }

  getDisplayTimeString(event: CalendarEvent) {
    this.eventService.getDisplayTimeString(event);
  }

  redirectToAddEvent(id: number) {
    location.replace(`/form/event?calendarId=${id}`);
  }

  private updateOffsettedDates() {
    this.displayingDate = this.getDate(this.offset);
    this.displayingWeek = this.getWeek(this.offset);
    this.loadEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
  }

  private calcRow(sec) {
    return ((sec - this.viewBeginningAtTime) / this.viewTimespan * this.viewRowCount) + this.viewBeginningAtRow;
  }
}
