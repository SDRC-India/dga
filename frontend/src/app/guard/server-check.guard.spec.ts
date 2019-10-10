import { TestBed, async, inject } from '@angular/core/testing';

import { ServerCheckGuard } from './server-check.guard';

describe('ServerCheckGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ServerCheckGuard]
    });
  });

  it('should ...', inject([ServerCheckGuard], (guard: ServerCheckGuard) => {
    expect(guard).toBeTruthy();
  }));
});
