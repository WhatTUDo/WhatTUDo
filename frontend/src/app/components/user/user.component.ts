import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faSignOutAlt} from '@fortawesome/free-solid-svg-icons'
import {Organisation} from "../../dtos/organisation";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  // user: User;
  user= {
    username: "JaneDoe",
  } //TODO: Use DTO and not a mock class. Refactor if needed.
  // userRoleInOrganizations: OrganisationMemberOrSomething[];
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
