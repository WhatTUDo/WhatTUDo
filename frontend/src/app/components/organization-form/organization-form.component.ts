import {Component, OnInit} from '@angular/core';
import {Organization} from '../../dtos/organization';
import {OrganizationService} from '../../services/organization.service';
import {ActivatedRoute, Router} from '@angular/router';
import {faChevronLeft} from '@fortawesome/free-solid-svg-icons';
import {Globals} from "../../global/globals";

@Component({
  selector: 'app-organization-form',
  templateUrl: './organization-form.component.html',
  styleUrls: ['./organization-form.component.scss']
})
export class OrganizationFormComponent implements OnInit {

  organization: Organization;
  isUpdate: boolean;
  faChevronLeft = faChevronLeft;
  private selectedImage: File;

  constructor(private organizationService: OrganizationService,
              public globals: Globals,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id) {
      this.organizationService.getById(id).subscribe((organization: Organization) => {
        this.organization = organization;
        this.organization.coverImageUrl = this.globals.backendUri+this.organization.coverImageUrl.slice(1);
        this.isUpdate = true;
      });
    } else {
      this.organization = new Organization(null, "", []);
      this.isUpdate = false;
    }
  }

  onSubmit(name: string) {
    this.organization.name = name.trim();
    if (!name) {
      return;
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
        this.router.navigate(["/organization/" + this.organization.id]);
      });
  }

  updateOrganization(name: string) {
    this.organizationService.putOrganization(this.organization)
      .subscribe(organization => {
          this.organization = organization;
          console.log('Organization ' + organization.name + ' updated successfully.');
          this.router.navigate(["/organization/" + this.organization.id]);
        },
        error => {
          alert("Could not update organization: " + error.error.message);
        });
  }

  selectImage(event) {
    this.selectedImage = event.target.files.item(0);
  }

  uploadImage() {
    this.organizationService.uploadOrganizationAvatar(this.organization.id, this.selectedImage).subscribe(resp => {
      // @ts-ignore
      this.organization.coverImageUrl = resp.url;
    });
  }
}
