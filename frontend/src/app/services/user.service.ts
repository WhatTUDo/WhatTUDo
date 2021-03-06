import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {User} from "../dtos/user";
import md5 from "md5";
import {Organization} from "../dtos/organization";
import {CalendarEvent} from "../dtos/calendar-event";
import {ChangeUserPasswordDto} from '../dtos/ChangeUserPasswordDto';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private usersBaseUri: string = this.globals.backendUri + '/users';

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
      'email': user.email,
      'password': user.password
    };

    return this.httpClient.post(this.usersBaseUri, reducedElement);
  }

  changePwd(changeUserPasswordDto: ChangeUserPasswordDto): Observable<User> {
    return this.httpClient.put<User>(this.globals.backendUri + '/changePwd', changeUserPasswordDto);
  }

  /**
   * Puts user to Server --> Update user
   * @param oldUsername the username that is currently in backend DB.
   * @param user updated version of user to be saved
   */
  putUser(oldUsername: string, user: User): Observable<any> {
    console.log('Put user to Server', user);
    return this.httpClient.put(this.usersBaseUri + '/' + oldUsername, user);
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
    if (!email) email = '';
    const gravatarHash = md5(email.trim().toLowerCase());
    return `https://www.gravatar.com/avatar/${gravatarHash}?s=${Math.trunc(size)}&d=identicon`;
  }

  getUserOrganization(userId: number): Observable<Organization[]> {
    return this.httpClient.get<Organization[]>(this.globals.backendUri + "/users/organizations/" + userId);
  }

  getRecommendedEvents(userId: number): Observable<CalendarEvent[]> {
    return this.httpClient.get<CalendarEvent[]>(this.globals.backendUri + "/users/recommendedEvents/" + userId);
  }


  removeFromOrganization(userId: number, organizationId: number): Observable<User> {
    return this.httpClient.delete<User>(this.globals.backendUri + `/users/${userId}/roles?orgaId=${organizationId}`);
  }

  getUserByName(name: string): Observable<User> {
    return this.httpClient.get<User>(this.usersBaseUri + '/getByName/' + name);
  }

  getAllUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.usersBaseUri);
  }

}
