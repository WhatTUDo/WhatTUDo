import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faSignOutAlt} from '@fortawesome/free-solid-svg-icons'
import {Organization} from "../../dtos/organization";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  // user: User;
  user= {
    id: 1,
    username: "JaneDoe",
  } //TODO: Use DTO and not a mock class. Refactor if needed.
  // userRoleInOrganizations: OrganizationMemberOrSomething[];
  userRoleInOrganizations = [
    {organizationName: "FS Winf", role: "Admin"},
    {organizationName: "HTU", role: "Member"},
  ]
  constructor() {
  }

  ngOnInit(): void {
  }

  faChevronLeft = faChevronLeft;
  faSignOutAlt = faSignOutAlt;
}
