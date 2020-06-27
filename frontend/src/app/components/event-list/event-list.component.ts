import {Component, Input, OnInit} from '@angular/core';
import {CalendarEvent} from "../../dtos/calendar-event";
import {EventService} from "../../services/event.service";
import {Globals} from "../../global/globals";

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})

export class EventListComponent implements OnInit {

  @Input() eventList: CalendarEvent[];

  constructor(private eventService: EventService,
              public globals: Globals) {
  }

  ngOnInit(): void {
  }

  getEventDateAndTimeString(event: CalendarEvent) {
    return this.eventService.getEventDateAndTimeString(event);
  }
}
