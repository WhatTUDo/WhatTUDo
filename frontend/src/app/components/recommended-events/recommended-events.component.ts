import {Component, OnInit} from '@angular/core';
import {CalendarEvent} from "../../dtos/calendar-event";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-recommended-events',
  templateUrl: './recommended-events.component.html',
  styleUrls: ['./recommended-events.component.scss']
})
export class RecommendedEventsComponent implements OnInit {

  recommendedEvents: CalendarEvent[];

  constructor(private eventService: EventService) {
  }

  ngOnInit(): void {
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

  public getEventTimeString(event: CalendarEvent) {
    const startDateTime: Date = new Date(event.startDateTime);
    const endDateTime: Date = new Date(event.endDateTime);
    const endsOnTheSameDay = (startDateTime.toDateString() == endDateTime.toDateString())
    let string = startDateTime.toLocaleTimeString('en-US', {
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    string += ' - '
    string += endDateTime.toLocaleTimeString('en-US', {
      month: endsOnTheSameDay ? undefined : 'short',
      day: endsOnTheSameDay ? undefined : 'numeric',
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    return string
  }
}
