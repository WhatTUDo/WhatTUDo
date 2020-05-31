import { Component, OnInit } from '@angular/core';
import {OrganizationService} from "../../services/organization.service";
import {Organization} from "../../dtos/organization";

@Component({
  selector: 'app-organization-list',
  templateUrl: './organization-list.component.html',
  styleUrls: ['./organization-list.component.css']
})
export class OrganizationListComponent implements OnInit {

  organizations: Array<Organization>

  constructor(
    private organizationService: OrganizationService) { }

  ngOnInit(): void {

    this.getOrganizations();
  }

  private getOrganizations() {
    this.organizationService.getAll().subscribe( organizations => {
      this.organizations = organizations;
    },
      error => {
      console.warn(error);
      })
  }

  private onClickedEdit(id: number) {
    window.location.replace("/form/organization/" + id);
  }

  private onClickedDelete(id: number) {
    this.organizationService.deleteOrganization(id).subscribe( deletedID => {
      this.organizations = this.organizations.filter(elem => elem.id != deletedID);
      alert("Deleted Organization with id: " + deletedID);
    }, error => {
      console.warn(error);
      alert(error.error.message);
    });
  }

  private onClickedAddNew() {
    window.location.replace("/form/organization/0");
  }

}
