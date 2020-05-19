import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {Location} from '../../dtos/location';
import {Organization} from '../../dtos/organization';
import {OrganizationService} from '../../services/organization.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-organization-form',
  templateUrl: './organization-form.component.html',
  styleUrls: ['./organization-form.component.scss']
})
export class OrganizationFormComponent implements OnInit {

  private organization: Organization;
  private organizationService: OrganizationService;
  private route: ActivatedRoute;


  constructor(private service: OrganizationService) {

  }

  ngOnInit(): void {
    this.getOrganization();
  }

  /**
   * Loads the organization for the specified id
   * @param id of the organization
   */
  private getOrganization() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.organizationService.getById(id).subscribe(orientation => this.organization = orientation);
  }

  // TODO: add calendars/remove calendars (need fetch all Calendars for that) + actual alert thingy
  updateOrganization(name: string) {
    this.organization.name = name.trim();
    if (!name) {
      return;
    }
    this.organizationService.putOrganization(this.organization)
      .subscribe(organization => {
        this.organization = organization;
        alert('Organization ' + organization.name + ' updated successfully.');
      });
  }


}
