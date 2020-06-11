import {Component, Input, OnInit} from '@angular/core';
import {CollisionResponse} from "../../dtos/collision-response";
import {EventCollision} from "../../dtos/event-collision";
import {CalendarEvent} from "../../dtos/calendar-event";
import {faExclamationTriangle} from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-event-conflict-resolver',
  templateUrl: './event-conflict-resolver.component.html',
  styleUrls: ['./event-conflict-resolver.component.scss']
})
export class EventConflictResolverComponent implements OnInit {

  // @Input() event: CalendarEvent;
  event = new CalendarEvent(null, 'Name 3', null,
    new Date(2020, 5 , 16, 16),
    new Date(2020, 5 , 16, 19), null, null, null, null);
  duration: number;

  // @Input() collisionResponse: CollisionResponse;
  collisionResponse: CollisionResponse = new CollisionResponse([
    new EventCollision(
      new CalendarEvent(null, 'Name 1', null,
        new Date(2020, 5 , 16, 16),
        new Date(2020, 5 , 16, 19), null, null, null, null),
      0.9, 'Lolo'
      )
  ], [
    new Date(2020, 5 , 17, 19),
    new Date(2020, 5 , 22, 17),
    new Date(2020, 5 , 19, 18, 30)
  ]);

  selectedResolvedEvent: CalendarEvent;

  constructor(private eventService: EventService) { }

  ngOnInit(): void {
    this.duration = this.eventService.getDuration(this.event);
  }

  getSortedCollisionEvents() {
    return this.collisionResponse.eventCollisions.sort((a, b) =>{
      return a.collisionScore - b.collisionScore;
    }).map((a) => {
      return {...a.event, message: a.message};
    })
  }

  getEventListWithSuggestedDate() {
    return this.collisionResponse.dateSuggestions.map((a)=>{
      let sugEvent = new CalendarEvent(null, null, null, null, null,
        null, null, null, null);
      sugEvent.startDateTime = a;
      sugEvent.endDateTime = new Date(a.valueOf() + this.duration);
      return sugEvent;
    })
  }

  getEventDateAndTimeString(event: CalendarEvent) {
    return this.eventService.getEventDateAndTimeString(event);
  }

  faExclamationTriangle = faExclamationTriangle;

  onSubmit() {
    console.log(this.selectedResolvedEvent)
  }
}
