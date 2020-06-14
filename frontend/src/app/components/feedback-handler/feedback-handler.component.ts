import {Component, Injectable, Input, OnInit} from '@angular/core';
import {faTimes} from "@fortawesome/free-solid-svg-icons";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-feedback-handler',
  templateUrl: './feedback-handler.component.html',
  styleUrls: ['./feedback-handler.component.scss'],
  animations: [
    trigger("makeVisible", [
      state('visible', style({
          opacity: 1,
          display: 'block'
        })
      ),
      state('invisible', style({
          opacity: 0,
          display: 'none'
        })
      ),
      transition('invisible => visible', [
        animate('0.25s')
      ]),
      transition('visible => invisible', [
        animate('0.25s')
      ])
    ])
  ]
})

@Injectable()
export class FeedbackHandlerComponent implements OnInit {

  static header: String;
  static message: String;
  static state: State;
  static isVisible: Boolean;
  static dismissAfter = 5_000;
  faTimes = faTimes;

  constructor() {
  }

  static displaySuccess(header, message) {
    this.displayMessage(header, message, State.Success);
  }

  static displayWarning(header, message) {
    this.displayMessage(header, message, State.Warning);
  }

  static displayError(header, message) {
    this.displayMessage(header, message, State.Error)
  }

  static displayServerError(error) {
    this.displayError(`Server Error (${error.status})`, error.message);
  }

  private static displayMessage(header: String, message: String, state: State = State.Neutral) {
    this.isVisible = true;
    this.message = message;
    this.header = header;
    this.state = state;

    setTimeout(_ => {
      this.onDismiss();
    }, this.dismissAfter)
  }

  ngOnInit(): void {
  }

  getVisibility() {
    return FeedbackHandlerComponent.isVisible;
  }

  getState() {
    return FeedbackHandlerComponent.state;
  }

  getHeader() {
    return FeedbackHandlerComponent.header;
  }

  getMessage() {
    return FeedbackHandlerComponent.message;
  }

  static onDismiss() {
    FeedbackHandlerComponent.isVisible = !FeedbackHandlerComponent.isVisible;
  }
}

enum State {
  Error = "feedback-error",
  Warning = "feedback-warning",
  Success = "feedback-success",
  Neutral = "feedback-neutral"
}
