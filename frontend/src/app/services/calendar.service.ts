import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Calendar} from '../dtos/calendar';
import {HttpClient, HttpParams, HttpRequest} from '@angular/common/http';
import {Globals} from '../global/globals';
import {CreateCalendar} from "../dtos/CreateCalendar";

@Injectable({
  providedIn: 'root'
})
export class CalendarService {
  private calendarBaseUri: string = this.globals.backendUri + '/calendars';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getAllCalendars(): Observable<any[]> {
    console.log("Get All Calendars")
    return this.httpClient.get<any[]>(this.calendarBaseUri + '/all');
  }

  getCalendarById(id: number): Observable<Calendar> {
    console.log("Load Calendar with ID", id);
    return this.httpClient.get<Calendar>(this.calendarBaseUri + '/' + id);
  }


  searchCalendars(name: string): Observable<any[]> {
    let params = new HttpParams().set("name", name);
    return this.httpClient.get<any[]>(this.calendarBaseUri + '/search', {params: params});

  }

  /** POST: add a new calendar to the server */
  addCalendar(calendar: CreateCalendar): Observable<Calendar> {
    return this.httpClient.post<Calendar>(this.calendarBaseUri + '/', calendar);
  }

  /** EDIT: edit a calendar */
  editCalendar(calendar: Calendar): Observable<Calendar> {
    return this.httpClient.put<Calendar>(this.calendarBaseUri + '/', calendar);
  }

  /** DELETE: delete the calendar from the server */
  deleteCalendar(calendar: Calendar | number): Observable<Calendar> {
    const id = typeof calendar === 'number' ? calendar : calendar.id;
    const url = `${this.calendarBaseUri}/${id}`;

    return this.httpClient.delete<Calendar>(url);
  }

  updateCalendar(calendar: Calendar): Observable<Calendar> {
    console.log("Updating organizations: ", calendar.organizationIds)
    return this.httpClient.put<Calendar>(this.calendarBaseUri + "/organizations", calendar);
  }


  uploadCalendarAvatar(calendarId: number, file: File) {
    const formData: FormData = new FormData();

    formData.append('imagefile', file);

    const req = new HttpRequest('POST', `${this.calendarBaseUri}/${calendarId}/cover`,
      formData, {
        responseType: 'json'
      });

    return this.httpClient.request(req);
  }
}
