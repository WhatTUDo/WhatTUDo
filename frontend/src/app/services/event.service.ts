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

  private eventBaseUri: string = this.globals.backendUri + 'events';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  deleteEvent(event: CalendarEvent) {
    console.log("Delete Event", CalendarEvent);
    return this.httpClient.delete(this.eventBaseUri + '/' + event);
  }

  getEventsByCalendarId(id: number): Observable<Array<CalendarEvent>> {
    console.log("Load Multiple Events by Calendar id");
    return this.httpClient.get<Array<CalendarEvent>>(this.eventBaseUri + '/calendarId/' + id);
  }


  getMultiplEvents(name: string, from: Date, to: Date): Observable<Array<CalendarEvent>> {
    console.log("Load Multiple events: ");
    let uriEncodedName = encodeURI(name);
    let uriEncodedStartDate = encodeURI(from.toISOString());
    let uriEncodedEndDate = encodeURI(to.toISOString());

    let url = this.eventBaseUri + "?name=" + uriEncodedName + "&from=" + uriEncodedStartDate + "&to=" + uriEncodedEndDate;
    return this.httpClient.get<Array<CalendarEvent>>(url);

  }

  /**
   * Loads specific event with ID, returns Observable.
   * @param id: ID of the Event
   */
  getEvent(id: number): Observable<CalendarEvent> {
    console.log("Load Event with ID", id);
     return this.httpClient.get<CalendarEvent>(this.eventBaseUri + '/' + id);
    // return null;
  }

  /**
   * Posts event to Server --> New Event
   * @param event
   */
  postEvent(event: CalendarEvent): Observable<any> {
    console.log("Post Event to Server", CalendarEvent);
    //TODO: Implement POST call
    let reducedElement = {
      "calendarId": event.calendarId,
      "endDateTime": event.endDateTime,
      "name": event.name,
      "startDateTime": event.startDateTime,
      "location": event.location
    }

    return this.httpClient.post(this.eventBaseUri, reducedElement);
  }

  /**
   * Put event to Server --> Update Event
   * @param event
   */
  putEvent(event: CalendarEvent): Observable<any> {
    console.log("Put Event to Server", CalendarEvent);
    let reducedElement = {
      "id": event.id,
      "calendarId": event.calendarId,
      "endDateTime": event.endDateTime,
      "name": event.name,
      "startDateTime": event.startDateTime,
      "location": event.location
    }

    return this.httpClient.put(this.eventBaseUri, reducedElement);
  }

  /**
   * Posts comment with Event ID to Server (Comment contains Author ID)
   * @param comment
   * @param eventID
   */
  postComment(comment: EventComment, eventID: number) {
    console.log("Posting comment: ", comment);
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

  searchLocationInAPI(searchTerm: string): Observable<any> {
    console.log("Searching Location with string: ", searchTerm);
    let searchURI = this.globals.openStreetMapsUri + '?q=' + encodeURI(searchTerm) + '&format=json&addressdetails=1';
    return this.httpClient.get<any>(searchURI);
  }
}
