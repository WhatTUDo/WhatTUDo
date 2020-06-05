import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {FeedbackService} from "../../services/feedback.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  currentTime: string;

  constructor(public authService: AuthService,
              public feedbackService: FeedbackService,
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
        this.feedbackService.displaySuccess("Success!", "You look good today!")
        break;
      case 1:
        this.feedbackService.displayWarning("Warning!", "Better be careful now!");
        break;
      case 2:
        this.feedbackService.displayError("Error!", "Oh no! The squirrels have escaped!");
        break;
      default:
        break;
    }
  }

}
