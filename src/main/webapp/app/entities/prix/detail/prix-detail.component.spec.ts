import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrixDetailComponent } from './prix-detail.component';

describe('Prix Management Detail Component', () => {
  let comp: PrixDetailComponent;
  let fixture: ComponentFixture<PrixDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrixDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prix: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrixDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prix on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prix).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
