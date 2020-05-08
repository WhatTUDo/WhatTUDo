import {Component, OnInit} from '@angular/core';
import {CalendarEvent} from '../../dtos/calendar-event';

import {faChevronCircleLeft, faChevronCircleRight, faChevronDown, faChevronUp} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-weekly-calendar',
  templateUrl: './weekly-calendar.component.html',
  styleUrls: ['./weekly-calendar.component.scss']
})
export class WeeklyCalendarComponent implements OnInit {

  currentDate: number;
  currentMonth: String;
  currentYear: number;

  week: Date[]; // Starts at a monday.
  offset = 0; // Currently in WEEKS. Probably should be in days for mobile compatibility.

  eventsOfTheWeek: Map<String, CalendarEvent[]> = new Map<String, CalendarEvent[]>()

  constructor() {

    //Mock data eventsOfTheWeek
    function getRandomInt(min, max) {
      min = Math.ceil(min);
      max = Math.floor(max);
      return Math.floor(Math.random() * (max - min)) + min;
    } // Source: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random

    for (let i = 1; i <= 4; i++) {
      const dateOffset = getRandomInt(0, +3);
      const startHours = 18 + getRandomInt(-3, +3);
      const endHours = startHours + getRandomInt(2, 4);
      let startDate = new Date(2020, 4, 8 + dateOffset, startHours, 0, 0, 0);
      let endDate = new Date(2020, 4, 8 + dateOffset, endHours, 30, 0, 0);

      let keyISOString = this.getMidnight(startDate).toISOString()
      let events = this.eventsOfTheWeek.get(keyISOString)
      if (!events) {
        events = []
      }
      events.push(
        new CalendarEvent(i, "Test Event " + i, "", startDate, endDate, null, null, null)
      );
      this.eventsOfTheWeek.set(keyISOString, events);
    }

    console.log(this.eventsOfTheWeek);
  }

  ngOnInit(): void {
    this.week = this.getWeek(this.offset);
    this.updateDatetime();
    setInterval(_ => {
      this.updateDatetime();
    }, 1000);

    setInterval(_ => {    Array.from(document.getElementsByClassName('calendar-event'))
      .forEach(event => {
        const time = event.getElementsByClassName('calendar-event-time')[0];
        const name = event.getElementsByClassName('calendar-event-name')[0];
        // @ts-ignore
        if (event.offsetHeight < time.scrollHeight + name.scrollHeight) {
          // @ts-ignore
          time.innerText = '...'
          // @ts-ignore
          name.innerText = '...';
        }
      })}, 500)
  }

  updateDatetime() {
    const today = this.getToday();
    this.currentMonth = today.toLocaleString('en-US', {month: 'long'});
    this.currentDate = today.getDate();
    this.currentYear = today.getFullYear();
  }

  getWeek(offset = 0) {
    let currentWeekDates = [];
    let today = this.getToday();

    let beginOfWeek = new Date(today);
    beginOfWeek.setDate(beginOfWeek.getDate() - (today.getDay() - 1));

    let date = new Date(beginOfWeek);
    date.setDate(date.getDate() + offset * 7);
    for (let i = 0; i <= 6; i++) {
      currentWeekDates.push(new Date(date));
      date.setDate(date.getDate() + 1);
    }
    return currentWeekDates;
  }

  addOneWeek() {
    this.offset++;
    this.week = this.getWeek(this.offset);
  }

  minusOneWeek() {
    this.offset--;
    this.week = this.getWeek(this.offset);
  }

  getToday() {
    let today = new Date(Date.now());
    today.setHours(0, 0, 0, 0);
    return today;
  }

  isToday(date: Date) {
    return this.getToday().toDateString() == date.toDateString();
  }

  getDisplayRows(event: CalendarEvent) {
    const startSecond = this.getSecondOffsetFromMidnight(event.startDate);
    const endSecond = this.getSecondOffsetFromMidnight(event.endDate);

    // Calendar View should starts at a time like 8 AM and ends at 23:59PM or even later.
    const startRow = Math.max(Math.floor((startSecond - 28800) / 57600 * 64), 0) // Proof of concept with magic numbers. TODO: Fix this.
    const endRow = Math.min(Math.floor((endSecond - 28800) / 57600 * 64), 64) // Proof of concept with magic numbers. TODO: Fix this.

    return `${startRow}/${endRow}`
  }

  getSecondOffsetFromMidnight(date: Date) {
    return date.getSeconds() + (60 * date.getMinutes()) + (60 * 60 * date.getHours());
  }

  getMidnight(date: Date) {
    let midnight = new Date(date);
    midnight.setHours(0, 0, 0, 0);
    return midnight
  }

  getDisplayTimeString(event: CalendarEvent) {
    let string = event.startDate.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    string += ' - '
    string += event.endDate.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    return string
  }

  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronCircleLeft = faChevronCircleLeft;
  faChevronCircleRight = faChevronCircleRight;
}
