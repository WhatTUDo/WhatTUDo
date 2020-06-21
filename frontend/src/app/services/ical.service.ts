import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ICalService {
  private iCalBaseUri: string = this.globals.backendUri + 'ical';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getUserIcalToken(): Observable<string> {
    return this.httpClient.post(this.iCalBaseUri + '/user', null, {
      headers: new HttpHeaders({
        'Accept': 'text/plain'
      }),
      responseType: 'text'
    });
  }
}
