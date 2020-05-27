import {Component, Input, OnInit} from '@angular/core';
import {EventComment} from "../../dtos/event-comment";

import {faThumbsDown, faThumbsUp, faUserCircle, faReply, faClock} from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-event-comment',
  templateUrl: './event-comment.component.html',
  styleUrls: ['./event-comment.component.scss']
})
export class EventCommentComponent implements OnInit {

  @Input() comment: EventComment;

  public author: string
  public rating: number

  constructor(eventService: EventService) {
  }

  faThumbsUp = faThumbsUp;
  faThumbsDown = faThumbsDown;
  faUserCircle = faUserCircle;
  faReply = faReply;
  faClock = faClock;

  ngOnInit(): void {
    this.author = "Testguy";
  }

  public vote(isUpvote: boolean) {
    if (isUpvote) {
      console.log("You upvoted this comment!");
    } else {
      console.log("You downvoted this comment!");
    }
  }

  public respondButtonClicked() {
    console.log("You clicked the respond button!");
  }

}
