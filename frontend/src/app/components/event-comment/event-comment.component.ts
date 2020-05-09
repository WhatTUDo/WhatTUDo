import {Component, Input, OnInit} from '@angular/core';
import {EventComment} from "../../dtos/event-comment";

import {faThumbsUp, faThumbsDown, faUserCircle} from "@fortawesome/free-solid-svg-icons";
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
  constructor(eventService: EventService) { }

  faThumbsUp = faThumbsUp;
  faThumbsDown = faThumbsDown;
  faUserCircle = faUserCircle;

  ngOnInit(): void {
    this.author = "Testguy";
  }

  public vote(isUpvote: boolean) {
    if (isUpvote) {
      alert("You upvoted this comment!");
    }
    else {
      alert("You downvoted this comment!");
    }
  }

  public respondButtonClicked() {
    alert("You clicked the respond button!");
  }

}
