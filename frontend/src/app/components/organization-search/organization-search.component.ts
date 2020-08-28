import {Component, OnInit} from '@angular/core';
import {OrganizationService} from "../../services/organization.service";
import {Organization} from "../../dtos/organization";
import {faChevronLeft, faCog, faPlus, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {FormControl, FormGroup} from "@angular/forms";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-organization-search',
  templateUrl: './organization-search.component.html',
  styleUrls: ['./organization-search.component.scss']
})
export class OrganizationSearchComponent implements OnInit {

  organizations: Organization[];

  searchForm = new FormGroup({
    name: new FormControl('')
  });
  searchActive: boolean = false;
  organizationSearchResult: Organization[] = [];

  faChevronLeft = faChevronLeft;
  faCog = faCog;
  faTimesCircle = faTimesCircle;
  faPlus = faPlus;

  constructor(
    private organizationService: OrganizationService,
    public authService: AuthService,
  ) {
  }

  ngOnInit(): void {
    this.getOrganizations();
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
      this.organizationService.searchOrganization(formValue.name).subscribe((searchResult) => {
        this.organizationSearchResult = searchResult;
        this.searchActive = true;
      })
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
    this.searchForm = new FormGroup({
      name: new FormControl('')
    });
    this.organizationSearchResult = [];
    this.searchActive = false;
  }

  private getOrganizations() {
    this.organizationService.getAll().subscribe(organizations => {
      this.organizations = organizations;
    })
  }
}
