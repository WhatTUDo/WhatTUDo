import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {FeedbackHandlerComponent} from "../feedback-handler/feedback-handler.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  currentTime: string;
  showError: boolean = false;

  constructor(public authService: AuthService,
              ) { }

  ngOnInit() {
    this.updateDatetime();
    setInterval(_=>{this.updateDatetime();}, 1000);
  }

  updateDatetime() {
    const date = new Date(Date.now());
    this.currentTime = date.toLocaleString('en-US', { hour: 'numeric', minute: 'numeric', hour12: true })
  }

  async toggleFeedback(stateNr: number) {
    switch (stateNr) {
      case 0:
        FeedbackHandlerComponent.displaySuccess("Success!", "You look good today!")
        break;
      case 1:
        FeedbackHandlerComponent.displayWarning("Warning!", "Better be careful now!");
        break;
      case 2:
        FeedbackHandlerComponent.displayError("Error!", "Oh no! The squirrels have escaped!");
        break;
      default:
        break;
    }
    this.showError = !this.showError;

  }
}
