import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {AuthService} from '../services/auth.service';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private globals: Globals) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authUri = this.globals.backendUri + 'api/v1/authentication';

    // Do not intercept authentication requests
    if (req.url === authUri) {
      return next.handle(req);
    }

    if (this.authService.isLoggedIn()) {
      const authReq = req.clone({
        headers: req.headers.set('Authorization', this.authService.getToken())
      });
      return next.handle(authReq);
    }

    // Do not intercept requests if not logged in.
    return next.handle(req);
  }
}
