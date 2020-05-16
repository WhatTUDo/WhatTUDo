import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {CalendarEvent} from '../dtos/calendar-event';
import {Calendar} from '../dtos/calendar';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class CalendarService {
  private calendarBaseUri: string = this.globals.backendUri + 'calendars';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  getAllCalendars(): Observable<Calendar[]>{
    console.log("Get All Calendars")
    return this.httpClient.get<any[]>(this.calendarBaseUri + '/');
  }

  getCalendarById(id: number): Observable<Calendar> {
    console.log("Load Calendar with ID", id);
    return this.httpClient.get<Calendar>(this.calendarBaseUri + '/' + id);
  }


  //FIXME : this is just to test some parts, has to be changed to a correct implementation.
  searchCalendars(name: string, organisation: string): Observable<Calendar[]> {
    return null;
  }


}
