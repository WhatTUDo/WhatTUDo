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
    console.log(`Hey! I am user ${this.authService.getUserId()}`)
    const startDate = new Date(2000, 1, 1);
    const endDate = new Date(2022, 1, 1);
    this.eventService.getMultiplEvents("",
      startDate, endDate).subscribe((events: CalendarEvent[]) => {
        console.log(events);
        this.recommendedEvents = events;
      }
    );
  }

  redirectToEvent(id: number) {
    window.location.replace(`event/${id}`)
  }

  public getEventDateAndTimeString(event: CalendarEvent) {
    return this.eventService.getEventDateAndTimeString(event);
  }
}
