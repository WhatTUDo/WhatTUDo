import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/auth-request';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import * as jwt_decode from 'jwt-decode';
import {Globals} from '../global/globals';
import {User} from '../dtos/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/api/v1/authentication';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  private static setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private static getTokenExpirationDate(token: string): Date {

    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => AuthService.setToken(authResponse))
      );
  }

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    // return true;
    return !!this.getToken() && (AuthService.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  getUser(): Observable<User> | null {
    if (this.isLoggedIn()) return this.httpClient.get<User>(this.globals.backendUri + "/users/user");
    else return null;
  }
}
