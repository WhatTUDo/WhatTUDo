import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {faChevronDown, faSignInAlt, faSignOutAlt, faUser} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userEmail: string = null;
  faSignInAlt = faSignInAlt;
  faSignOutAlt = faSignOutAlt;
  faChevronDown = faChevronDown;
  faUser = faUser;
  mobileCollapsed: boolean = true;

  constructor(public authService: AuthService,
              private userService: UserService
  ) {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user) => {
        if (!user) this.authService.logoutUser();
        this.userEmail = user.email;
      })
    }
  }

  ngOnInit() {
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }

  toggleCollapsed() {
    this.mobileCollapsed = !this.mobileCollapsed;
  }
}
