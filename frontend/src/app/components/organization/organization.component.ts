import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faCog, faPlus, faTimesCircle} from '@fortawesome/free-solid-svg-icons'
import {Organization} from "../../dtos/organization";

@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit {

  // organization: Organization;
  organization = new Organization(null, "FS Winf", []);
  // organizationMembers: OrganizationMemberOrSomething[];
  organizationMembers = [
    {username: "JaneDoe", role: "Admin"},
    {username: "JohnyAppleseed", role: "Member"},
  ]

  constructor() {
  }

  ngOnInit(): void {
  }

  faChevronLeft = faChevronLeft;
  faPlus = faPlus;
  faCog = faCog;
  faTimesCircle = faTimesCircle;
}
