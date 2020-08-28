import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EventCommentComponent} from './event-comment.component';

describe('EventCommentComponent', () => {
  let component: EventCommentComponent;
  let fixture: ComponentFixture<EventCommentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EventCommentComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EventCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
