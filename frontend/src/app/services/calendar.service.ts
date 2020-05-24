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

  getAllCalendars(): Observable<any[]>{
    console.log("Get All Calendars")
    return this.httpClient.get<any[]>(this.calendarBaseUri + '/all');
  }

  getCalendarById(id: number): Observable<Calendar> {
    console.log("Load Calendar with ID", id);
    return this.httpClient.get<Calendar>(this.calendarBaseUri + '/' + id);
  }

  getEventsOfTheWeek(id:number, from: Date, to: Date): Observable<any[]>{
    let params = new HttpParams().set("id", String(id))
      .set("from", String(from))
      .set("to", String(to));
    return this.httpClient.get<any[]>(this.calendarBaseUri+"/thisWeek", {params: params});
  }


  searchCalendars(name: string): Observable<any[]> {
    let params = new HttpParams().set("name", name) ;
    return this.httpClient.get<any[]>(this.calendarBaseUri+'/search', {params: params});

  }
  /** POST: add a new calendar to the server */
  addCalendar (calendar: Calendar): Observable<Calendar> {
    return this.httpClient.post<Calendar>(this.calendarBaseUri + '/' , calendar);
  }



}
