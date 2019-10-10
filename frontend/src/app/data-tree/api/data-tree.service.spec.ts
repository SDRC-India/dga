import { TestBed } from '@angular/core/testing';

import { DataTreeService } from './data-tree.service';

describe('DataTreeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DataTreeService = TestBed.get(DataTreeService);
    expect(service).toBeTruthy();
  });
});
