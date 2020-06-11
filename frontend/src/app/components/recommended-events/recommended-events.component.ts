import {Component, OnInit} from '@angular/core';
import {CalendarEvent} from "../../dtos/calendar-event";
import {EventService} from "../../services/event.service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-recommended-events',
  templateUrl: './recommended-events.component.html',
  styleUrls: ['./recommended-events.component.scss']
})

export class RecommendedEventsComponent implements OnInit {

  recommendedEvents: CalendarEvent[];

  constructor(private eventService: EventService,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    const startDate = new Date(2000, 1, 1);
    const endDate = new Date(2022, 1, 1);
    this.eventService.getMultipleEvents(startDate, endDate).subscribe((events: CalendarEvent[]) => {
        this.recommendedEvents = events;
      }
    );
  }

  getEventDateAndTimeString(event: CalendarEvent) {
    return this.eventService.getEventDateAndTimeString(event);
  }
}
