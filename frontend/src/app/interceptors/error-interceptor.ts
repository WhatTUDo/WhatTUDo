import {
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpEvent,
  HttpResponse,
  HttpErrorResponse
} from '@angular/common/http';
import {Injectable} from "@angular/core"
import {Observable, of} from "rxjs";
import {tap, catchError} from "rxjs/operators";
import {FeedbackService} from "../services/feedback.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private feedbackService: FeedbackService) {
  }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    return next.handle(req).pipe(
      tap(evt => {
        if (evt instanceof HttpResponse) {
          if (evt.body && evt.body.success) {}
        }
      }),
      catchError((err: any) => {
        if (err instanceof HttpErrorResponse) {
          try {
           switch (err.status) {
             case 400:
             case 404:
             case 405:
               this.feedbackService.displayError(`Oops. This should not have happened. (${err.status})`, 'We are not sending a correct request to the server. Contact us. We are glad to fix this.');
               break
             case 401:
             case 403:
               this.feedbackService.displayError(`Permission Denied. (${err.status})`, 'You are not allow to do that.');
               break
             default:
               this.feedbackService.displayServerError(err);
           }
          } catch (e) {
            this.feedbackService.displayError('Error!', 'An unexpected error occurred.');
          }
        }
        return of(err);
      }));

  }

}
