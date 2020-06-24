import {TestBed} from '@angular/core/testing';

import {ICalService} from './iCal.service';

describe('ICalService', () => {
  let service: ICalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ICalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
