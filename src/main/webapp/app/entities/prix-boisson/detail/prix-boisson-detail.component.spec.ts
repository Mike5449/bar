import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrixBoissonDetailComponent } from './prix-boisson-detail.component';

describe('PrixBoisson Management Detail Component', () => {
  let comp: PrixBoissonDetailComponent;
  let fixture: ComponentFixture<PrixBoissonDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrixBoissonDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prixBoisson: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrixBoissonDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrixBoissonDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prixBoisson on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prixBoisson).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
