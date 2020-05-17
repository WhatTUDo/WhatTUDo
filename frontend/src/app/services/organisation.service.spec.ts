import {TestBed} from '@angular/core/testing';

import {OrganisationService} from "./organisation.service";

describe('MessageService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: OrganisationService = TestBed.get(OrganisationService);
    expect(service).toBeTruthy();
  });
});
