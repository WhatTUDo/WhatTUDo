import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Organization} from "../../dtos/organization";
import {OrganizationService} from "../../services/organization.service";
import {UserService} from "../../services/user.service";
import {AuthService} from "../../services/auth.service";
import {Globals} from "../../global/globals";

import {faChevronLeft, faCog, faPlus, faTimesCircle} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-organization-list',
  templateUrl: './organization-list.component.html',
  styleUrls: ['./organization-list.component.scss']
})
export class OrganizationListComponent implements OnInit, OnChanges {

  @Input() organizations: Organization[]

  organizationUserAvatars: Map<number, string[]> = new Map();

  faChevronLeft = faChevronLeft;
  faCog = faCog;
  faTimesCircle = faTimesCircle;
  faPlus = faPlus;

  constructor(
    private organizationService: OrganizationService,
    private userService: UserService,
    public authService: AuthService,
    public globals: Globals
  ) {
  }

  ngOnInit(): void {
    this.loadMemberAvatars()
  }

  ngOnChanges(changes: SimpleChanges) {
    this.loadMemberAvatars()
  }

  loadMemberAvatars() {
    if (this.organizations) {
      this.organizations.forEach(org => {
        if (!this.organizationUserAvatars.get(org.id)) {
          this.organizationService.getMembers(org.id).subscribe(users => {
            this.organizationUserAvatars.set(org.id, users.map(user => {
              return this.getGravatarLink(user.email, 64)
            }))
          })
        }
      })
    }
  }

  onClickedDelete(id: number) {
    if (confirm(`You are deleting organization "${this.organizations.find(o => o.id === id).name}". Are you sure?`)) {
      this.organizationService.deleteOrganization(id).subscribe(deletedID => {
        this.organizations = this.organizations.filter(elem => elem.id != deletedID);
        alert("Deleted Organization with id: " + deletedID);
      });
    }
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }
}
