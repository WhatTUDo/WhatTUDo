import {Component, OnInit} from '@angular/core';
import {OrganizationService} from "../../services/organization.service";
import {Organization} from "../../dtos/organization";
import {faChevronLeft, faCog, faPlus, faTimes, faTimesCircle} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-organization-list',
  templateUrl: './organization-list.component.html',
  styleUrls: ['./organization-list.component.scss']
})
export class OrganizationListComponent implements OnInit {

  organizations: Array<Organization>
  faChevronLeft = faChevronLeft;
  faCog = faCog;
  faTimesCircle = faTimesCircle;
  faTimes = faTimes;
  faPlus = faPlus;

  constructor(
    private organizationService: OrganizationService) {
  }

  ngOnInit(): void {

    this.getOrganizations();
  }

  onClickedEdit(id: number) {
    window.location.replace("/form/organization/" + id);
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
      })
  }

}
