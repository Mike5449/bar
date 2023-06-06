import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SalaryDetailComponent } from './salary-detail.component';

describe('Salary Management Detail Component', () => {
  let comp: SalaryDetailComponent;
  let fixture: ComponentFixture<SalaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SalaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ salary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SalaryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SalaryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load salary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.salary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
