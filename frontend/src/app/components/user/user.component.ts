import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faSignOutAlt} from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }

  faChevronLeft = faChevronLeft;
  faSignOutAlt = faSignOutAlt;
}
