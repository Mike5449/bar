import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveRequestDetailComponent } from './leave-request-detail.component';

describe('LeaveRequest Management Detail Component', () => {
  let comp: LeaveRequestDetailComponent;
  let fixture: ComponentFixture<LeaveRequestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaveRequestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaveRequest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaveRequestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaveRequestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaveRequest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaveRequest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
