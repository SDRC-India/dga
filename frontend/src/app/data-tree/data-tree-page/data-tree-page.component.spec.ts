import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataTreePageComponent } from './data-tree-page.component';

describe('DataTreePageComponent', () => {
  let component: DataTreePageComponent;
  let fixture: ComponentFixture<DataTreePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataTreePageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataTreePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
