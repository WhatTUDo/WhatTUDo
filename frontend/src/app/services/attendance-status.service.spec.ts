import { TestBed } from '@angular/core/testing';

import { AttendanceStatusService } from './attendance-status.service';

describe('AttendanceStatusService', () => {
  let service: AttendanceStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttendanceStatusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
