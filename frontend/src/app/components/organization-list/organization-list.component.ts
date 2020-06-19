import {Component, OnInit} from '@angular/core';
import {OrganizationService} from "../../services/organization.service";
import {Organization} from "../../dtos/organization";
import {faChevronLeft, faCog, faPlus, faTimes, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {UserService} from "../../services/user.service";
import {User} from "../../dtos/user";
import {ActivatedRoute} from "@angular/router";
import {Globals} from "../../global/globals";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-organization-list',
  templateUrl: './organization-list.component.html',
  styleUrls: ['./organization-list.component.scss']
})
export class OrganizationListComponent implements OnInit {

  organizations: Organization[];
  organizationUserAvatars: Map<Organization, string[]> = new Map();

  searchForm = new FormGroup({
    name: new FormControl('')
  });
  searchActive: boolean = false;
  organizationSearchResult: Organization[] = [];

  faChevronLeft = faChevronLeft;
  faCog = faCog;
  faTimesCircle = faTimesCircle;
  faTimes = faTimes;
  faPlus = faPlus;

  constructor(
    private organizationService: OrganizationService,
    private userService: UserService,
    private globals: Globals
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
        org.coverImageUrl = this.globals.backendUri+org.coverImageUrl.slice(1);
        this.organizationService.getMembers(org.id).subscribe(users => {
          this.organizationUserAvatars.set(org, users.map(user => {
            return this.getGravatarLink(user.email, 64)
          }))
        })
      })
    })
  }

  getOrganizationAvatarLink(organizationId: number, size: number) {
    return this.organizationService.getOrganizationAvatarLink(organizationId, size);
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }


  onSubmit() {
    let formValue = this.searchForm.value;
    if (!formValue.name) {
      this.organizationSearchResult = [];
      this.searchActive = false;
      return
    }
    let validationIsPassed = this.validateFormInput(formValue);
    if (validationIsPassed) {
      // submit to service
      console.log("search");
      //TODO: Organization Search
      this.searchActive = true;
    }
  }


  /**
   *
   * @param formValue
   * returns true/false depending on whether validation succeeds.
   */
  validateFormInput(formValue: any) {
    let errors: Array<Error> = new Array<Error>();
    if ((formValue.name == "")) {
      errors.push(new Error("Nothing was given to search."));
    }

    if (errors.length > 0) {
      console.warn(errors);
      let errorMessage = "";
      for (let error of errors) {
        errorMessage += error.message + " ";
      }
      alert(errorMessage);
      this.organizationSearchResult = [];
      return false;
    }
    return true;
  }

  clearSearch() {
    this.organizationSearchResult = [];
    this.searchActive = false;
  }
}
