import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedbackHandlerComponent } from './feedback-handler.component';

describe('FeedbackHandlerComponent', () => {
  let component: FeedbackHandlerComponent;
  let fixture: ComponentFixture<FeedbackHandlerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FeedbackHandlerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FeedbackHandlerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
