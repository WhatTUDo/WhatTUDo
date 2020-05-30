import {Component, Injectable, Input, OnInit} from '@angular/core';
import {faTimes} from "@fortawesome/free-solid-svg-icons";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-feedback-handler',
  templateUrl: './feedback-handler.component.html',
  styleUrls: ['./feedback-handler.component.css'],
  animations: [
    trigger("makeVisible", [
      state('visible', style({
          opacity: 1
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

  @Input() isVisible: boolean = false;
  faTimes = faTimes;

  static header: String;
  static message: String
  static state: State


  constructor() { }

  ngOnInit(): void {
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

  static displaySuccess(header, message) {
    this.displayMessage(header, message, State.Success);
  }

  static displayWarning(header, message) {
    this.displayMessage(header, message, State.Warning);
  }

  static displayError(header, message) {
    this.displayMessage(header, message, State.Error)
  }

  private static displayMessage(header: String, message: String, state: State = State.Neutral) {

    this.message = message;
    this.header = header;
    this.state = state;

  console.log(header, message)
  }

  onDismiss() {
    this.isVisible = !this.isVisible;
    console.log("Dismiss");
  }
}

enum State {
  Error = "feedback-error",
  Warning = "feedback-warning",
  Success = "feedback-success",
  Neutral = "feedback-neutral"
}
