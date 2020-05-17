import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {Location} from '../../dtos/location';
import {Organisation} from '../../dtos/organisation';
import {OrganisationService} from '../../services/organisation.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-organisation-form',
  templateUrl: './organisation-form.component.html',
  styleUrls: ['./organisation-form.component.scss']
})
export class OrganisationFormComponent implements OnInit {

  private organisation: Organisation;
  private organisationService: OrganisationService;
  private route: ActivatedRoute;


  constructor(private service: OrganisationService) {

  }

  ngOnInit(): void {
    this.getOrganisation();
  }

  /**
   * Loads the organisation for the specified id
   * @param id of the organisation
   */
  private getOrganisation() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.organisationService.getById(id).subscribe(orientation => this.organisation = orientation);
  }

  // TODO: add calendars/remove calendars (need fetch all Calendars for that) + actual alert thingy
  updateOrganisation(name: string) {
    this.organisation.name = name.trim();
    if (!name) {
      return;
    }
    this.organisationService.putOrganisation(this.organisation)
      .subscribe(organisation => {
        this.organisation = organisation;
        alert('Organisation ' + organisation.name + ' updated successfully.');
      });
  }


}
