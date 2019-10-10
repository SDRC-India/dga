import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DdmEntryComponent } from './ddm-entry.component';

describe('DdmEntryComponent', () => {
  let component: DdmEntryComponent;
  let fixture: ComponentFixture<DdmEntryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DdmEntryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DdmEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
