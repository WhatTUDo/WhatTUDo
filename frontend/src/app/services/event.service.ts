import {Injectable} from "@angular/core";
import {AuthRequest} from "../dtos/auth-request";
import {Observable} from "rxjs";
import {CalendarEvent} from "../dtos/calendar-event";
import {HttpClient, HttpParams, HttpRequest} from "@angular/common/http";
import {Globals} from "../global/globals";
import {EventComment} from "../dtos/event-comment";
import {Label} from "../dtos/label";

@Injectable({
  providedIn: 'root'
})

export class EventService {

  private eventBaseUri: string = this.globals.backendUri + '/events';
  private labelBaseUri: string = this.globals.backendUri + '/labels';
  private commentBaseUri: string = this.globals.backendUri + '/comments';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  deleteEvent(eventId: number) {
    console.log("Delete Event", CalendarEvent);
    return this.httpClient.delete(this.eventBaseUri + '/' + eventId);
  }


  deleteComment(commentId: number) {
    console.log("Delete Comment", EventComment);
    return this.httpClient.delete(this.commentBaseUri + '/' + commentId);
  }


  createComment(eventComment : EventComment) {
    console.log("Create Comment", EventComment);
   return this.httpClient.post<EventComment>(this.commentBaseUri + '/', eventComment);
   }


  editComment(eventComment : EventComment) {
    console.log("Edit Comment", eventComment);
   return this.httpClient.put<EventComment>(this.commentBaseUri + '/', eventComment);
   }

  getEventsByCalendarId(id: number): Observable<Array<CalendarEvent>> {
    console.log("Load Multiple Events by Calendar id");
    return this.httpClient.get<Array<CalendarEvent>>(this.eventBaseUri + '/calendarId/' + id);
  }


  getAllLabels(): Observable<Label[]> {
    console.log('Get all labels');
    return this.httpClient.get<Array<Label>>(this.labelBaseUri);
  }

  getEventLabels(id: number): Observable<Label[]> {
    console.log('Get event labels');
    return this.httpClient.get<Array<Label>>(this.eventBaseUri + '/' + id + '/' + 'labels');
  }

  getEventComments(id: number): Observable<EventComment[]> {
    console.log('Get event comments');
    return this.httpClient.get<EventComment[]>(this.eventBaseUri + '/' + id + '/' + 'comments');
  }

  addLabels(id: number, labelselect: Array<Label>) {

    console.log('add  labels');
    console.log(id);
    return this.httpClient.put<Event>(this.eventBaseUri + '/' + id + '/' + 'labels', labelselect)
      .subscribe(response => {
        console.log(response);
      });

  }

  getMultipleEvents(from: Date, to: Date): Observable<Array<CalendarEvent>> {
    let uriEncodedStartDate = encodeURI(from.toISOString());
    let uriEncodedEndDate = encodeURI(to.toISOString());

    let url = this.eventBaseUri + "?from=" + uriEncodedStartDate + "&to=" + uriEncodedEndDate;
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
  postEvent(event: CalendarEvent): Observable<CalendarEvent> {
    console.log("Post Event to Server", CalendarEvent);
    let reducedElement = {
      "calendarId": event.calendarId,
      "endDateTime": event.endDateTime,
      "name": event.name,
      "description": event.description,
      "startDateTime": event.startDateTime,
      "locationId": event.locationId
    }
    return this.httpClient.post<CalendarEvent>(this.eventBaseUri, reducedElement);
  }

  /**
   * Put event to Server --> Update Event
   * @param event
   */
  putEvent(event: CalendarEvent): Observable<CalendarEvent> {
    console.log("Put Event to Server", CalendarEvent);
    let reducedElement = {
      "id": event.id,
      "calendarId": event.calendarId,
      "endDateTime": event.endDateTime,
      "name": event.name,
      "startDateTime": event.startDateTime,
      "locationId": event.locationId
    }

    return this.httpClient.put<CalendarEvent>(this.eventBaseUri, reducedElement);
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

  /**
   * A helper method to generate a concise and human-readable date and time string.
   * If the event ends on the same day, the end date will be omitted, so that the date is only printed on time.
   * @param event with correct startDateTime and endDateTime.
   */
  public getEventDateAndTimeString(event: CalendarEvent) {
    const startDateTime: Date = new Date(event.startDateTime);
    const endDateTime: Date = new Date(event.endDateTime);
    const endsOnTheSameDay = (startDateTime.toDateString() == endDateTime.toDateString())
    let string = startDateTime.toLocaleTimeString(this.globals.dateLocale, {
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    string += ' - '
    string += endDateTime.toLocaleTimeString(this.globals.dateLocale, {
      month: endsOnTheSameDay ? undefined : 'short',
      day: endsOnTheSameDay ? undefined : 'numeric',
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    return string
  }

  /**
   * Similar to the method above, but only time is printed.
   * Used if the date is denoted already.
   * @param event
   */
  public getDisplayTimeString(event: CalendarEvent) {
    let startDate = new Date(event.startDateTime);
    let endDate = new Date(event.endDateTime);
    let string = startDate.toLocaleTimeString(this.globals.dateLocale, {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    string += ' - '
    string += endDate.toLocaleTimeString(this.globals.dateLocale, {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    return string
  }

  public getDisplayDateAndTimeStringFromDate(date: Date) {
    return date.toLocaleTimeString(this.globals.dateLocale, {
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
  }

  uploadEventCover(eventId: number, file: File) {
    const formData: FormData = new FormData();

    formData.append('imagefile', file);

    const req = new HttpRequest('POST', `${this.eventBaseUri}/${eventId}/cover`,
      formData, {
        responseType: 'json'
      });

    return this.httpClient.request(req);
  }

  getEventCover(eventId: number) {
    return this.httpClient.get(`${this.eventBaseUri}/${eventId}/cover`)
  }

  searchEvent(name: string) {
    let params = new HttpParams();
    params = params.set('name', name);
    return this.httpClient.get<CalendarEvent[]>(`${this.eventBaseUri}/search`, {params: params});
  }
}
