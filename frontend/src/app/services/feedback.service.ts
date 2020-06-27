import {Injectable} from '@angular/core';
import {FeedbackHandlerComponent} from "../components/feedback-handler/feedback-handler.component";

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  constructor() {
  }

  displaySuccess(header: string, message: string, technicalInformation: string = null) {
    FeedbackHandlerComponent.displaySuccess(header, message, technicalInformation)
  }

  displayWarning(header: string, message: string, technicalInformation: string = null) {
    FeedbackHandlerComponent.displayWarning(header, message, technicalInformation)
  }

  displayError(header: string, message: string, technicalInformation: string = null) {
    FeedbackHandlerComponent.displayError(header, message, technicalInformation)
  }

  displayServerError(error) {
    FeedbackHandlerComponent.displayServerError(error)
  }
}
