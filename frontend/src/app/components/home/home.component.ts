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
    setInterval(()=>{this.updateDatetime();}, 1000);
  }

  updateDatetime() {
    const date = new Date(Date.now());
    this.currentTime = date.toLocaleString('en-US', { hour: 'numeric', minute: 'numeric', hour12: true })
  }
}
