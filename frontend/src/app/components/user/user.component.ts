import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faSignOutAlt} from '@fortawesome/free-solid-svg-icons'
import {AuthService} from "../../services/auth.service";
import {User} from "../../dtos/user";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {Organization} from "../../dtos/organization";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  user: User;
  // userRoleInOrganizations: OrganizationMemberOrSomething[];
  userInOrganizations: Organization[];

  constructor(private authService: AuthService,
              private userService: UserService,
              private router: Router) {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user: User) => {
          if (!user) {
            this.router.navigate(['/login']);
          }
          this.user = user;
          this.userService.getUserOrganization(user.id).subscribe((organization: Organization[]) => {
            this.userInOrganizations = organization;
          })
        }
      );
    } else {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {
  }

  signOut(): void {
    this.authService.logoutUser();
    this.router.navigate(['/']);
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }

  faChevronLeft = faChevronLeft;
  faSignOutAlt = faSignOutAlt;
}
