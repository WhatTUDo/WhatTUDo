import {Component, OnInit} from '@angular/core';
import {Organization} from '../../dtos/organization';
import {OrganizationService} from '../../services/organization.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-organization-form',
  templateUrl: './organization-form.component.html',
  styleUrls: ['./organization-form.component.scss']
})
export class OrganizationFormComponent implements OnInit {

  organization: Organization;
  isUpdate: boolean;


  constructor(private organizationService: OrganizationService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id) {
      this.organizationService.getById(id).subscribe((organization: Organization) => {
        if (organization) {
          this.organization = organization;
          this.isUpdate = true;
        } else {
          this.organization = new Organization(null, "", []);
          this.isUpdate = false;
        }
      });
    } else {
      this.organization = new Organization(null, "", []);
      this.isUpdate = false;
    }
  }

  // TODO: add calendars/remove calendars (need fetch all Calendars for that) + actual alert thingy
  onSubmit(name: string) {
    this.organization.name = name.trim();
    if (!name) {
      return
    }
    if (this.organization.id) {
      this.updateOrganization(name);
    } else {
      this.createOrganization(name);
    }
  }

  createOrganization(name: string) {
    this.organizationService.postOrganization(this.organization)
      .subscribe(organization => {
        this.organization = organization;
        console.log('Organization ' + organization.name + ' created successfully.');
        window.location.replace("/organization/" + this.organization.id);
      });
  }


  updateOrganization(name: string) {
    this.organizationService.putOrganization(this.organization)
      .subscribe(organization => {
        this.organization = organization;
        console.log('Organization ' + organization.name + ' updated successfully.');
        window.location.replace("/organization/" + this.organization.id);
      },
        error => {
          alert("Could not update organization: " + error.error.message);
        });
  }


}
