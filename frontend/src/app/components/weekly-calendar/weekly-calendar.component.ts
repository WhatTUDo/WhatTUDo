import {Component, OnInit} from '@angular/core';
import {CalendarEvent} from '../../dtos/calendar-event';

import {faChevronDown, faChevronLeft, faChevronRight, faChevronUp} from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-weekly-calendar',
  templateUrl: './weekly-calendar.component.html',
  styleUrls: ['./weekly-calendar.component.scss']
})
export class WeeklyCalendarComponent implements OnInit {

  currentDate: number;
  currentMonth: String;
  currentYear: number;

  displayingDate: Date;
  displayingWeek: Date[]; // Starts at a monday.
  offset = 0; // Currently in WEEKS. Probably should be in days for mobile compatibility.

  eventsOfTheWeek: Map<String, CalendarEvent[]> = new Map<String, CalendarEvent[]>()

  constructor(
    private eventService: EventService
  ) {

    // //Mock data eventsOfTheWeek
    // function getRandomInt(min, max) {
    //   min = Math.ceil(min);
    //   max = Math.floor(max);
    //   return Math.floor(Math.random() * (max - min)) + min;
    // } // Source: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
    //
    // for (let i = 1; i <= 4; i++) {
    //   const dateOffset = getRandomInt(0, +1);
    //   const startHours = 18 + getRandomInt(-3, +3);
    //   const endHours = startHours + getRandomInt(2, 4);
    //   let startDate = new Date(2020, 4, 11 + dateOffset, startHours, 0, 0, 0);
    //   let endDate = new Date(2020, 4, 11 + dateOffset, endHours, 30, 0, 0);
    //
    //   let keyISOString = this.getMidnight(startDate).toISOString()
    //   let events = this.eventsOfTheWeek.get(keyISOString)
    //   if (!events) {
    //     events = []
    //   }
    //   events.push(
    //     new CalendarEvent(i, "Test Event " + i, "", startDate, endDate, null, null, null)
    //   );
    //   this.eventsOfTheWeek.set(keyISOString, events);
    // }

    console.log(this.eventsOfTheWeek);
  }

  ngOnInit(): void {
    this.displayingDate = this.getToday();
    this.displayingWeek = this.getWeek(this.offset);

    this.loadEventsForWeek(this.getWeek(0)[0], this.getWeek(0)[6]);
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
            time.innerText = '…'
            // @ts-ignore
            name.innerText = '…';
          }
        })
    }, 500)
  }

  /**
   * Calls Service to load Events for the week.
   * @param from: Start date of week
   * @param to: End date of week
   */
  loadEventsForWeek(from: Date, to: Date) {
    this.eventService.getMultiplEvents(null, from, to).subscribe((events: Array<CalendarEvent>) => {
      events.forEach(event => {
        let startDate = new Date(event.startDateTime)
        let endDate = new Date(event.endDateTime);

        event.startDateTime = startDate;
        event.endDateTime = endDate;
      });
      this.displayingWeek.forEach((day: Date) => {
        let keyISOString = this.getMidnight(day).toISOString()
        this.eventsOfTheWeek.set(keyISOString, events.filter(event => {
          let isAfterMidnight = event.startDateTime.getTime() > this.getMidnight(day).getTime();
          let isBeforeEndOfDay = event.startDateTime.getTime() < this.getEndOfDay(day).getTime();
          return isAfterMidnight && isBeforeEndOfDay;
          }));
      })
    }, error => {
      if (error.status != 404) {
        alert("Error while loading events: " + error.message);
      }
      else {
        console.log("No events found!");
      }
    });

  }

  updateDatetime() {
    const today = this.getToday();
    this.currentMonth = today.toLocaleString('en-US', {month: 'long'});
    this.currentDate = today.getDate();
    this.currentYear = today.getFullYear();
  }

  getWeek(offset = 0) {
    const offsetWeeks = Math.round(this.offset/7);
    let currentWeekDates = [];
    let today = this.getToday();

    let beginOfWeek = new Date(today);
    // % is remainder and not mod in js. Therefore -1%7=-1. This is a workaround.
    const weekOffset = (today.getDay() - 1 + 7) % 7;
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

  private updateOffsettedDates() {
    this.displayingDate = this.getDate(this.offset);
    this.displayingWeek = this.getWeek(this.offset);
    this.loadEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
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
    const startSecond = this.getSecondOffsetFromMidnight(event.startDateTime);
    const endSecond = this.getSecondOffsetFromMidnight(event.endDateTime);

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

  getEndOfDay(date: Date) {
    let endOfDay = new Date(date);
    endOfDay.setHours(23,59, 50);
    return endOfDay;
  }

  public getDisplayTimeString(event: CalendarEvent) {
    let string = event.startDateTime.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    string += ' - '
    string += event.endDateTime.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    return string
  }

  public redirectToDetail(id: number) {
    console.log("You Clicked: ", id);
    window.location.replace("/event/" + id);
  }


  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
}
