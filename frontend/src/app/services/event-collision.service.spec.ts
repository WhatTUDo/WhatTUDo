import { TestBed } from '@angular/core/testing';

import { EventCollisionService } from './event-collision.service';

describe('EventCollisionService', () => {
  let service: EventCollisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventCollisionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
