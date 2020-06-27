import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CollisionResponse} from "../../dtos/collision-response";
import {CalendarEvent} from "../../dtos/calendar-event";
import {faExclamationTriangle} from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-event-conflict-resolver',
  templateUrl: './event-conflict-resolver.component.html',
  styleUrls: ['./event-conflict-resolver.component.scss']
})
export class EventConflictResolverComponent implements OnInit {

  @Input() event: CalendarEvent;
  @Input() collisionResponse: CollisionResponse;
  @Output() change: EventEmitter<Date[]> = new EventEmitter();

  selectedDateIndex: number = null;
  faExclamationTriangle = faExclamationTriangle;

  constructor(private eventService: EventService) {
  }

  ngOnInit(): void {
  }

  getSortedCollisionEvents() {
    return this.collisionResponse.eventCollisions.sort((a, b) => {
      return a.collisionScore - b.collisionScore;
    }).map((a) => {
      return {...a.event, message: a.message};
    })
  }

  getEventListWithSuggestedDate() {
    return this.collisionResponse.dateSuggestions.map((a) => {
      return new CalendarEvent(null, null, null, a[0], a[1],
        null, null, null, null);
    })
  }

  getEventDateAndTimeString(event: CalendarEvent) {
    return this.eventService.getEventDateAndTimeString(event);
  }

  onSubmit() {
    this.change.emit(this.collisionResponse.dateSuggestions[this.selectedDateIndex]);
  }

  onSelect(index: number) {
    this.selectedDateIndex = index;
  }
}
