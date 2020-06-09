import {
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpEvent,
  HttpResponse,
  HttpErrorResponse
} from '@angular/common/http';
import {Injectable} from "@angular/core"
import {Observable, of, throwError} from "rxjs";
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
      tap(event => {
        if (event instanceof HttpResponse) {
          if (event.body && event.body.success) {}
        }
      }),
      catchError((error: any) => {
        if (error instanceof HttpErrorResponse) {
          try {
           switch (error.status) {
             case 400:
             case 405:
               this.feedbackService.displayError(`Oops. This should not have happened. (${error.status})`, 'We are not sending a correct request to the server. Contact us, we are glad to fix this.');
               break
             case 401:
               this.feedbackService.displayError(`Login Required. (${error.status})`, 'You can only do this after you logged in.');
               break
             case 403:
               this.feedbackService.displayError(`Permission Denied. (${error.status})`, 'You are not allow to do that.');
               break
             case 404:
               this.feedbackService.displayError(`This does not exist. (${error.status})`, 'We cannot find what you are looking for in our database.');
               break
             default:
               this.feedbackService.displayServerError(error);
           }
          } catch (e) {
            this.feedbackService.displayError('Error!', 'An unexpected error occurred.');
          }
        }
        return of(error);
      }));

  }

}
