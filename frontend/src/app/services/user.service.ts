import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {User} from "../dtos/user";
import md5 from "md5";

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private usersBaseUri: string = this.globals.backendUri + 'users';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Posts user to Server --> New user
   * @param user
   */
  postUser(user: User): Observable<any> {

    console.log('Post user to server', user);
    let reducedElement = {
      'id': null,
      'name': user.name,
      'email': user.email
    };

    return this.httpClient.post(this.usersBaseUri, reducedElement);
  }

  /**
   * Puts user to Server --> Update user
   * @param user updated version of user to be saved
   */
  putUser(user: User): Observable<any> {
    console.log('Put user to Server', user);
    return this.httpClient.put(this.usersBaseUri, user);
  }

  /**
   * Deletes user.
   * @param id
   */
  deleteUser(id: number): Observable<number> {
    console.log("Delete user with ID ", id);
    return this.httpClient.delete<number>(this.usersBaseUri + "/" + id);
  }

  getGravatarLink(email: String, size: number) {
    if (!email) return "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
    const gravatarHash = md5(email.trim().toLowerCase());
    return `https://www.gravatar.com/avatar/${gravatarHash}?s=${Math.trunc(size)}&d=identicon`;
  }

  //
  // /**
  //  * @param organizationId ID of organization where calendar should be added.
  //  * @param calendarId ID of calendar to be added.
  //  */
  // addCalendarToOrga(organizationId: number, calendarId: number): Observable<any> {
  //   console.log(`Add Calendar ${calendarId} to`, organizationId);
  //   let params = new HttpParams();
  //   params = params.set('calendarId', String(calendarId));
  //   params = params.set('organizationId', String(organizationId));
  //   console.log(params);
  //   return this.httpClient.put(this.usersBaseUri + `/calendars`, {}, {
  //     params: params
  //   });
  // }
  //
  // /**
  //  * @param organizationId ID of organization where calendar should be added.
  //  * @param calendarId ID of calendar to be added.
  //  */
  // removeCalendarToOrga(organizationId: number, calendarId: number): Observable<any> {
  //   console.log(`Remove Calendar ${calendarId} to`, Organization);
  //   let params = new HttpParams();
  //   params = params.set('id', String(calendarId));
  //   return this.httpClient.delete(this.usersBaseUri + `/${organizationId}/calendars`, {
  //     params: params
  //   })
  // }

}
