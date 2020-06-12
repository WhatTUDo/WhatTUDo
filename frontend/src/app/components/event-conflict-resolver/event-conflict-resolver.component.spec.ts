import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EventConflictResolverComponent } from './event-conflict-resolver.component';

describe('AppointmentConflictResolverComponent', () => {
  let component: EventConflictResolverComponent;
  let fixture: ComponentFixture<EventConflictResolverComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EventConflictResolverComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EventConflictResolverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
