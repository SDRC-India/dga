import { TestBed } from '@angular/core/testing';

import { DhapServiceService } from './dhap-service.service';

describe('DhapServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DhapServiceService = TestBed.get(DhapServiceService);
    expect(service).toBeTruthy();
  });
});
