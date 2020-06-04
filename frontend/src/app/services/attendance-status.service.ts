import { Injectable } from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {AttendanceDto} from '../dtos/AttendanceDto';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AttendanceStatusService {

  private  attendanceUri: string = this.globals.backendUri + 'attendance';

  constructor(private httpClient: HttpClient,private globals: Globals) { }

  /**POST: add a new attendance status*/
  create(attendanceStatus: AttendanceDto): Observable<AttendanceDto>{
    return this.httpClient.post<AttendanceDto>(this.attendanceUri + '/', attendanceStatus);
  }

}
