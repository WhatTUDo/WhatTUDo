import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {EventComment} from "../../dtos/event-comment";
import {User} from "../../dtos/user";
import {AuthService} from '../../services/auth.service';

import {faThumbsDown, faThumbsUp, faUserCircle, faReply, faClock,
faCog, faTimesCircle } from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-event-comment',
  templateUrl: './event-comment.component.html',
  styleUrls: ['./event-comment.component.scss']
})
export class EventCommentComponent implements OnInit {

  @Input() comment: EventComment;

  public user: User;
  public author: string;
  public rating: number;
  public pushedEdit: boolean;
  faThumbsUp = faThumbsUp;
  faThumbsDown = faThumbsDown;
  faUserCircle = faUserCircle;
  faReply = faReply;
  faClock = faClock;
  faCog = faCog;
  faTimesCircle = faTimesCircle;

  constructor(private eventService: EventService,
              private authService: AuthService) {


    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user) => {
        this.user = user;
      });
    }
  }

  ngOnInit(): void {

    this.author = this.comment.username;
    this.pushedEdit = false;
  }

  public vote(isUpvote: boolean) {
    if (isUpvote) {
      console.log("You upvoted this comment!");
    } else {
      console.log("You downvoted this comment!");
    }
  }

 public changeEditCommentState() {
    if (this.pushedEdit == false) {
      this.pushedEdit = true;
    } else {
      this.pushedEdit = false;
    }
  }

  public respondButtonClicked() {
    console.log("You clicked the respond button!");
  }

  public editComment(input : string) {
        if(input.length > 0){

         this.comment.text = input;

         this.eventService.createComment(this.comment).subscribe(comment => {
          this.comment = comment;
          this.pushedEdit = false;
    });
  }
  }

}
