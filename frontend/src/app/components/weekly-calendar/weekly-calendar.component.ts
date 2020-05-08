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


  constructor() {
  }

  ngOnInit(): void {
    this.week = this.getWeek(this.offset);
    this.updateDatetime();
    setInterval(_ => {
      this.updateDatetime();
    }, 1000);
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

  faChevronUp = faChevronUp;
  faChevronDown = faChevronDown;
  faChevronCircleLeft = faChevronCircleLeft;
  faChevronCircleRight = faChevronCircleRight;
}
