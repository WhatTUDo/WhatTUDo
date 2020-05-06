import {Injectable} from "@angular/core";
import {AuthRequest} from "../dtos/auth-request";
import {Observable} from "rxjs";
import {CalendarEvent} from "../dtos/calendar-event";
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {EventComment} from "../dtos/event-comment";

@Injectable({
  providedIn: 'root'
})

export class EventService {
  private eventBaseUri: string = this.globals.backendUri + '/event'

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads specific event with ID, returns Observable.
   * @param id: ID of the Event
   */
  getEvent(id: number): Observable<CalendarEvent> {
    console.log("Load Event with ID", id);
    return this.httpClient.get<CalendarEvent>(this.eventBaseUri + '/' + id);
  }

  postEvent(event: CalendarEvent) {
    console.log("Post Event to backend", CalendarEvent);
    //TODO: Implement POST call
  }

  postComment(comment: EventComment, eventID: number) {

  }

  postAttendance(attendanceStatus: number, eventID: number) {

  }
}
