import {Component, Input, OnInit} from '@angular/core';
import {EventComment} from "../../dtos/event-comment";

@Component({
  selector: 'app-event-comment',
  templateUrl: './event-comment.component.html',
  styleUrls: ['./event-comment.component.css']
})
export class EventCommentComponent implements OnInit {

  @Input() comment: EventComment;

  public author: string
  public rating: number
  constructor() { }

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
