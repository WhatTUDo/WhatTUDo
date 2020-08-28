import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from '@angular/common/http';
import {Injectable} from "@angular/core"
import {Observable, of} from "rxjs";
import {catchError} from "rxjs/operators";
import {FeedbackService} from "../services/feedback.service";
import {Globals} from "../global/globals";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private feedbackService: FeedbackService, private globals: Globals) {
  }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const authUri = this.globals.backendUri + '/api/v1/authentication';

    // Do a different 401 for auth.
    if (req.url === authUri) {
      return next.handle(req).pipe(
        catchError((error: HttpResponse<any>) => {
          if (error instanceof HttpErrorResponse) {
            try {
              switch (error.status) {
                case 401:
                  this.feedbackService.displayError('Wrong Credentials', 'Check if your username and password are entered corrected.');
                  break
                default:
                  this.feedbackService.displayServerError(error);
              }
            } catch (e) {
              this.feedbackService.displayError('Error!', 'An unexpected error occurred.');
            }
          }
          return of(error);
        })
      );
    }

    return next.handle(req).pipe(
      catchError((error: HttpResponse<any>) => {
        if (error instanceof HttpErrorResponse) {
          try {
            error = error.error;
            switch (error.status) {
              case 400:
              case 405:
                // @ts-ignore
                this.feedbackService.displayError('Oops. This should not have happened.', 'We are not sending a correct request to the server. Contact us, we are glad to fix this.', error.status + ': ' + error.message);
                break
              case 401:
                // @ts-ignore
                this.feedbackService.displayError('Login Required.', 'You can only do this after you logged in.', error.status + ': ' + error.message);
                break
              case 403:
                // @ts-ignore
                this.feedbackService.displayError('Permission Denied.', 'You are not allow to do that.', error.status + ': ' + error.message);
                break
              case 404:
                // @ts-ignore
                this.feedbackService.displayError('This does not exist.', 'We cannot find what you are looking for in our database.', error.status + ': ' + error.message);
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
