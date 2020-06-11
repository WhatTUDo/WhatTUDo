import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PromoEventListComponent} from './promo-event-list.component';

describe('RecommendedEventsComponent', () => {
  let component: PromoEventListComponent;
  let fixture: ComponentFixture<PromoEventListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PromoEventListComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PromoEventListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
