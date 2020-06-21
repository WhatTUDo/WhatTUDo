import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {EventComment} from "../../dtos/event-comment";
import {User} from "../../dtos/user";

import {faThumbsDown, faThumbsUp, faUserCircle, faReply, faClock} from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-event-comment',
  templateUrl: './event-comment.component.html',
  styleUrls: ['./event-comment.component.scss']
})
export class EventCommentComponent implements OnInit {

  @Input() comment: EventComment;

  public author: string;
  public rating: number;
  faThumbsUp = faThumbsUp;
  faThumbsDown = faThumbsDown;
  faUserCircle = faUserCircle;
  faReply = faReply;
  faClock = faClock;

  constructor(eventService: EventService) {
  }

  ngOnInit(): void {

    this.author = this.comment.username;
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
