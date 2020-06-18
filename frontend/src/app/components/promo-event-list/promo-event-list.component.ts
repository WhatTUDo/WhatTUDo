import {Component, Input, OnInit} from '@angular/core';
import {CalendarEvent} from "../../dtos/calendar-event";
import {EventService} from "../../services/event.service";
import {Globals} from "../../global/globals";

@Component({
  selector: 'app-promo-event-list',
  templateUrl: './promo-event-list.component.html',
  styleUrls: ['./promo-event-list.component.scss']
})

export class PromoEventListComponent implements OnInit {

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
