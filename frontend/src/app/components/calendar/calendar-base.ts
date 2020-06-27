import {EventService} from "../../services/event.service";
import {faChevronDown, faChevronLeft, faChevronRight, faChevronUp, faPlus} from '@fortawesome/free-solid-svg-icons';
import {Globals} from "../../global/globals";

export class CalendarBase {
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

  updateOffsettedDates() {
    this.displayingDate = this.getDate(this.offset);
    this.displayingWeek = this.getWeek(this.offset);
    this.loadAllEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
  }

  loadAllEventsForWeek(from: Date, to: Date) {
    //Overwrite me!
  }

}
