import {Component, OnInit} from '@angular/core';
import {OrganizationService} from "../../services/organization.service";
import {Organization} from "../../dtos/organization";
import {faChevronLeft, faCog, faPlus, faTimes, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {UserService} from "../../services/user.service";
import {User} from "../../dtos/user";

@Component({
  selector: 'app-organization-list',
  templateUrl: './organization-list.component.html',
  styleUrls: ['./organization-list.component.scss']
})
export class OrganizationListComponent implements OnInit {

  organizations: Organization[];
  organizationUserAvatars: Map<Organization, string[]> = new Map();

  faChevronLeft = faChevronLeft;
  faCog = faCog;
  faTimesCircle = faTimesCircle;
  faTimes = faTimes;
  faPlus = faPlus;

  constructor(
    private organizationService: OrganizationService,
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.getOrganizations();
  }

  onClickedDelete(id: number) {
    if (confirm(`You are deleting organization "${this.organizations.find(o => o.id === id).name}". Are you sure?`)) {
      this.organizationService.deleteOrganization(id).subscribe(deletedID => {
        this.organizations = this.organizations.filter(elem => elem.id != deletedID);
        alert("Deleted Organization with id: " + deletedID);
      });
    }
  }

  private getOrganizations() {
    this.organizationService.getAll().subscribe(organizations => {
      this.organizations = organizations;
      organizations.forEach(org => {
        this.organizationService.getMembers(org.id).subscribe(users => {
          this.organizationUserAvatars.set(org, users.map(user => {
            return this.getGravatarLink(user.email, 64)
          }))
        })
      })
    })
  }

  getOrganizationAvatarLink(organizationId: number, size: number) {
    this.organizationService.getOrganizationAvatarLink(organizationId, size);
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }
}
