import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SdrcDataTreeComponent } from './sdrc-data-tree.component';

describe('SdrcDataTreeComponent', () => {
  let component: SdrcDataTreeComponent;
  let fixture: ComponentFixture<SdrcDataTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SdrcDataTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SdrcDataTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
