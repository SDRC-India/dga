import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LaQkhyaComponent } from './la-qkhya.component';

describe('LaQkhyaComponent', () => {
  let component: LaQkhyaComponent;
  let fixture: ComponentFixture<LaQkhyaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LaQkhyaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LaQkhyaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
