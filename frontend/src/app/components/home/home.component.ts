import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {FeedbackService} from "../../services/feedback.service";
import {UserService} from "../../services/user.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  currentTime: string;
  userId: number;
  recommendedEvents: Event[];

  constructor(public authService: AuthService,
              public userService: UserService,
  ) {
  }

  ngOnInit() {
    this.loadRecommendedEvents();
    this.updateDatetime();
    setInterval(() => {
      this.updateDatetime();
    }, 1000);
  }

  updateDatetime() {
    const date = new Date(Date.now());
    this.currentTime = date.toLocaleString('en-US', {hour: 'numeric', minute: 'numeric', hour12: true})
  }


  loadRecommendedEvents() {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user) => {
        this.userId = user.id;
        this.userService.getRecommendedEvent(user.id).subscribe((events: Event[]) => {
          this.recommendedEvents = events;
        })
      })
    }
  }
}
