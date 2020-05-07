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
    // return this.httpClient.get<CalendarEvent>(this.eventBaseUri + '/' + id);
    return null;
  }

  /**
   * Posts event to Server --> New Event
   * @param event
   */
  postEvent(event: CalendarEvent) {
    console.log("Post Event to Server", CalendarEvent);
    //TODO: Implement POST call
  }

  /**
   * Posts comment with Event ID to Server (Comment contains Author ID)
   * @param comment
   * @param eventID
   */
  postComment(comment: EventComment, eventID: number) {
    console.log("Posting comment: ", comment)
  }

  postVote(isUpvote: boolean, commentID: number, userID: number) {

  }

  /**
   * Posts Attendance to a specific event to Server
   * @param userID
   * @param attendanceStatus
   * @param eventID
   */
  postAttendance(userID: number, attendanceStatus: number, eventID: number) {
    console.log("Post Attendance for User: ", userID);
  }
}
