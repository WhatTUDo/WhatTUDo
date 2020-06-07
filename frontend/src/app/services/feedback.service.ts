import { Injectable } from '@angular/core';
import {FeedbackHandlerComponent} from "../components/feedback-handler/feedback-handler.component";

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  constructor() { }

  displaySuccess(header: string, message: string){
    FeedbackHandlerComponent.displaySuccess(header, message)
  }

  displayWarning(header: string, message: string){
    FeedbackHandlerComponent.displayWarning(header, message)
  }

  displayError(header: string, message: string){
    FeedbackHandlerComponent.displayError(header, message)
  }

  displayServerError(error){
    FeedbackHandlerComponent.displayServerError(error)
  }
}
