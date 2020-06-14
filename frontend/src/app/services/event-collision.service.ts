import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Globals} from '../global/globals';
import {CalendarEvent} from "../dtos/calendar-event";
import {CollisionResponse} from "../dtos/collision-response";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class EventCollisionService {

  private eventCollisionBaseUri: string = this.globals.backendUri + 'collision';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getEventCollisions(event: CalendarEvent): Observable<CollisionResponse> {
    console.log('Get collisions with of event id' + event.id);
    return this.httpClient.post<CollisionResponse>(this.eventCollisionBaseUri, event);
  }
}
