import { Component, OnInit } from '@angular/core';
import {Calendar} from '../../dtos/calendar';
import {Router,ActivatedRoute, ParamMap} from '@angular/router';
import {Observable} from 'rxjs';
import {CalendarService} from '../../services/calendar.service';
import {faChevronDown, faChevronLeft, faChevronRight, faChevronUp} from "@fortawesome/free-solid-svg-icons";
import {CalendarEvent} from '../../dtos/calendar-event';
import {EventService} from '../../services/event.service';
import {Organisation} from '../../dtos/organisation';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit {

  offset = 0;
  currentDate: number;
  currentMonth: String;
  currentYear: number;

  displayingDate: Date;
  displayingWeek: Date[]; // Starts at a monday.
  eventsOfTheWeek: Map<String, CalendarEvent[]> = new Map<String, CalendarEvent[]>();
  events : CalendarEvent[] = [];
  calendar: Calendar = new Calendar(null, null, null);

  constructor(private eventService: EventService,private route: ActivatedRoute, private calendarService : CalendarService) {
  }

  ngOnInit(): void {
    let id = parseInt(this.route.snapshot.paramMap.get('id'));
    this.calendarService.getCalendarById(id).subscribe((calendar:Calendar) =>{
      this.calendar = calendar;
    },  err => {
      console.warn(err);
    });
    this.displayingWeek = this.getWeek(this.offset);
    let keyISOString = this.getMidnight(this.displayingWeek[0]).toISOString();
    let events = this.eventsOfTheWeek.get(keyISOString);
    // console.log("checkthiss::: "+this.displayingWeek[0])
    this.calendarService.getEventsOfTheWeek(id, this.displayingWeek[0], this.displayingWeek[6]).subscribe((events1)=>
    {
      this.events = events1;
    },
      err => {
        alert(err.message);
      });
    this.eventsOfTheWeek.set(keyISOString, events);  }

  getThisWeekEvents(){


  }

  updateDatetime() {
    const today = this.getToday();
    this.currentMonth = today.toLocaleString('en-US', {month: 'long'});
    this.currentDate = today.getDate();
    this.currentYear = today.getFullYear();
  }

  getWeek(offset: number) {
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


  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
}

