import {CalendarEvent} from "../../dtos/calendar-event";
import {EventService} from "../../services/event.service";
import {faChevronDown, faChevronLeft, faChevronRight, faChevronUp, faPlus} from '@fortawesome/free-solid-svg-icons';
import {Globals} from "../../global/globals";

export class CalendarBase {
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
  viewMinRows = 10;

  /** color classes to add **/
  calendarColors = ["blue", "green", "yellow", "orange", "red", "violet"];

  offset = 0;

  displayingDate: Date;
  displayingWeek: Date[]; // Starts at a monday.

  dateLocale: string;

  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
  faPlus = faPlus;

  constructor(public eventService: EventService, public globals: Globals) {
    this.dateLocale = this.globals.dateLocale;
    this.displayingDate = this.getDate(this.offset);
    this.displayingWeek = this.getWeek(this.offset);
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

  resetOffset() {
    this.offset = 0;
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

   updateOffsettedDates() {
   //Overwrite me
  }

  loadAllEventsForWeek(from: Date, to: Date) {
    //Overwrite me!
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
