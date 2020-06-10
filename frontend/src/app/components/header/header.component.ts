import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {faSignInAlt, faSignOutAlt, faChevronDown, faCog} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userEmail: string = '';

  constructor(public authService: AuthService,
              private userService: UserService
  ) {
    this.authService.getUser().subscribe((user) => {
      this.userEmail = user.email;
    })
  }

  ngOnInit() {
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }

  faSignInAlt = faSignInAlt;
  faSignOutAlt = faSignOutAlt;
  faChevronDown = faChevronDown;
  faCog = faCog;
}
