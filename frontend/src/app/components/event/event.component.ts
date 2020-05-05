import { Component, OnInit } from '@angular/core';
import {EventComment} from "../../dtos/event-comment";
import {CalendarEvent} from "../../dtos/calendar-event";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {

  constructor() { }

  public calendarEvent: CalendarEvent

  ngOnInit(): void {
    let startDate = new Date(2020, 5, 5, 18, 0, 0, 0);
    let endDate = new Date(2020, 5, 5, 22, 30, 0, 0);
    this.calendarEvent = new CalendarEvent(null, "Test Event", startDate, endDate, null, null, null);

    this.calendarEvent.comments = this.getComments();
  }

  getComments() {
    let comment1 = new EventComment(null, null, "Lorem Ipsum Dolor Sit", 0.85);
    let comment2 = new EventComment(null, null, "Quantum est in magnis", 0.66);
    let comment3 = new EventComment(null, null, "ad flumen contendunt? Marcus et Iulia.", 0.91);
    let array = new Array<EventComment>();
    array.push(comment1, comment2, comment3);

    return array;
  }

}
