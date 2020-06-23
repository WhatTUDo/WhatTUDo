import {Component, Input, OnInit} from '@angular/core';

import {EventComment} from "../../dtos/event-comment";
import {AuthService} from '../../services/auth.service';

import {
  faThumbsDown, faThumbsUp, faUserCircle, faReply, faClock,
  faEdit, faTimesCircle
} from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";
import {Globals} from "../../global/globals";

@Component({
  selector: 'app-event-comment',
  templateUrl: './event-comment.component.html',
  styleUrls: ['./event-comment.component.scss']
})
export class EventCommentComponent implements OnInit {

  @Input() comment: EventComment;
  @Input() canEdit: boolean;

  public user: String;
  public author: string;
  public rating: number;
  public editActive: boolean;
  faThumbsUp = faThumbsUp;
  faThumbsDown = faThumbsDown;
  faUserCircle = faUserCircle;
  faReply = faReply;
  faClock = faClock;
  faEdit = faEdit;
  faTimesCircle = faTimesCircle;

  constructor(private eventService: EventService,
              private authService: AuthService,
              public globals: Globals) {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user) => {
        this.user = user.name;
      });
    }
  }

  ngOnInit(): void {
    this.author = this.comment.username;
    this.editActive = false;
  }

  public vote(isUpvote: boolean) {
    if (isUpvote) {
      console.log("You upvoted this comment!");
    } else {
      console.log("You downvoted this comment!");
    }
  }

  public changeEditCommentState() {
    this.editActive = !this.editActive;
  }

  public editComment(input: string) {
    if (input) {
      this.editActive = false;
      this.comment.text = input;
      this.eventService.createComment(this.comment).subscribe((comment) => {
        const updateDateTime = new Date(comment.updateDateTime);
        this.comment = {...comment, updateDateTime: updateDateTime} as EventComment;
      });
    }
  }

  getDisplayDateAndTimeStringFromDate(date: Date) {
    return this.eventService.getDisplayDateAndTimeStringFromDate(date);
  }

  deleteComment(): void {
    if (confirm(`You are deleting the selected comment. Are you sure?`)) {
      this.eventService.deleteComment(this.comment.id).subscribe(() => {
        this.comment = null;
      });
    }
  }
}
