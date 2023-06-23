import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompteCaisseDetailComponent } from './compte-caisse-detail.component';

describe('CompteCaisse Management Detail Component', () => {
  let comp: CompteCaisseDetailComponent;
  let fixture: ComponentFixture<CompteCaisseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompteCaisseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ compteCaisse: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompteCaisseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompteCaisseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load compteCaisse on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.compteCaisse).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
