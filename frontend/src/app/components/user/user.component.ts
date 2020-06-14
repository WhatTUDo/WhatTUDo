import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faSignOutAlt} from '@fortawesome/free-solid-svg-icons'
import {AuthService} from "../../services/auth.service";
import {User} from "../../dtos/user";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {Organization} from "../../dtos/organization";
import {OrganizationService} from "../../services/organization.service";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  user: User;
  // userRoleInOrganizations: OrganizationMemberOrSomething[];
  userInOrganizations: Organization[];
  faChevronLeft = faChevronLeft;
  faSignOutAlt = faSignOutAlt;

  constructor(private authService: AuthService,
              private userService: UserService,
              private organizationService: OrganizationService,
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

  removeSelfFromOrg(organizationId: number) {
    if (confirm(`You are leaving "${this.userInOrganizations.find(o => o.id === organizationId).name}". Are you sure?`)) {
      this.userService.removeFromOrganization(this.user.id, organizationId).subscribe((user) => {
      })
    }
  }

  getOrganizationAvatarLink(organizationId: number, size: number) {
    this.organizationService.getOrganizationAvatarLink(organizationId, size);
  }
}
