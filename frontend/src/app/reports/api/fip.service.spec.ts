import { TestBed } from '@angular/core/testing';

import { FipService } from './fip.service';

describe('FipService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FipService = TestBed.get(FipService);
    expect(service).toBeTruthy();
  });
});
