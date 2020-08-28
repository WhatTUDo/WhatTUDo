import {Component, Input, OnInit} from '@angular/core';
import {CalendarEvent} from "../../../dtos/calendar-event";
import {EventService} from "../../../services/event.service";
import {Globals} from "../../../global/globals";

@Component({
  selector: 'app-calendar-day-view',
  templateUrl: './calendar-day-view.component.html',
  styleUrls: ['./calendar-day-view.component.scss']
})
export class CalendarDayViewComponent implements OnInit {

  @Input() events: CalendarEvent[]
  @Input() date: Date

  /* Change view… variables to configure: */
  /* number of rows. */
  viewBeginningAtRow = 1;
  viewEndingAtRow = 65;
  viewRowCount = this.viewEndingAtRow - this.viewBeginningAtRow;

  /* when the start and end of the grid represents. */
  viewBeginningAtTime = 8 * (60 * 60);
  viewEndingAtTime = 24 * (60 * 60) - 1;
  viewTimespan = this.viewEndingAtTime - this.viewBeginningAtTime;

  /* min row count for an event so that there's place for text. */
  viewMinRows = 6;

  /* color classes to add */
  calendarColors = ["blue", "green", "yellow", "orange", "red", "violet"];

  dateLocale: string;

  constructor(public eventService: EventService, public globals: Globals) {
    this.dateLocale = this.globals.dateLocale;
  }

  ngOnInit(): void {
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

  getDisplayTimeString(event: CalendarEvent) {
    if (this.isOnSameDay(event.startDateTime, event.endDateTime)) {
      return this.eventService.getDisplayTimeString(event);
    } else {
      return this.eventService.getEventDateAndTimeString(event);
    }
  }

  getCalendarColor(event: CalendarEvent) {
    return this.calendarColors[event.calendarId % this.calendarColors.length];
  }

  /**
   * A helper function to do the calculation of the number of row.
   * Mapping sec from interval [viewBeginningAtTime, viewEndingAtTime] to [viewBeginningAtRow, viewEndingAtRow]
   * @param sec Time in day in seconds after midnight.
   */
  private calcRow(sec) {
    return ((sec - this.viewBeginningAtTime) / this.viewTimespan * this.viewRowCount) + this.viewBeginningAtRow;
  }
}
