import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecommendedEventsComponent } from './recommended-events.component';

describe('RecommendedEventsComponent', () => {
  let component: RecommendedEventsComponent;
  let fixture: ComponentFixture<RecommendedEventsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecommendedEventsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecommendedEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
