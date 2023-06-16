import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrixFormService } from './prix-form.service';
import { PrixService } from '../service/prix.service';
import { IPrix } from '../prix.model';
import { IBoisson } from 'app/entities/boisson/boisson.model';
import { BoissonService } from 'app/entities/boisson/service/boisson.service';

import { PrixUpdateComponent } from './prix-update.component';

describe('Prix Management Update Component', () => {
  let comp: PrixUpdateComponent;
  let fixture: ComponentFixture<PrixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prixFormService: PrixFormService;
  let prixService: PrixService;
  let boissonService: BoissonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrixUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PrixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prixFormService = TestBed.inject(PrixFormService);
    prixService = TestBed.inject(PrixService);
    boissonService = TestBed.inject(BoissonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Boisson query and add missing value', () => {
      const prix: IPrix = { id: 456 };
      const boisson: IBoisson = { id: 46063 };
      prix.boisson = boisson;

      const boissonCollection: IBoisson[] = [{ id: 62156 }];
      jest.spyOn(boissonService, 'query').mockReturnValue(of(new HttpResponse({ body: boissonCollection })));
      const additionalBoissons = [boisson];
      const expectedCollection: IBoisson[] = [...additionalBoissons, ...boissonCollection];
      jest.spyOn(boissonService, 'addBoissonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prix });
      comp.ngOnInit();

      expect(boissonService.query).toHaveBeenCalled();
      expect(boissonService.addBoissonToCollectionIfMissing).toHaveBeenCalledWith(
        boissonCollection,
        ...additionalBoissons.map(expect.objectContaining)
      );
      expect(comp.boissonsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prix: IPrix = { id: 456 };
      const boisson: IBoisson = { id: 89046 };
      prix.boisson = boisson;

      activatedRoute.data = of({ prix });
      comp.ngOnInit();

      expect(comp.boissonsSharedCollection).toContain(boisson);
      expect(comp.prix).toEqual(prix);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrix>>();
      const prix = { id: 123 };
      jest.spyOn(prixFormService, 'getPrix').mockReturnValue(prix);
      jest.spyOn(prixService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prix });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prix }));
      saveSubject.complete();

      // THEN
      expect(prixFormService.getPrix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prixService.update).toHaveBeenCalledWith(expect.objectContaining(prix));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrix>>();
      const prix = { id: 123 };
      jest.spyOn(prixFormService, 'getPrix').mockReturnValue({ id: null });
      jest.spyOn(prixService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prix: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prix }));
      saveSubject.complete();

      // THEN
      expect(prixFormService.getPrix).toHaveBeenCalled();
      expect(prixService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrix>>();
      const prix = { id: 123 };
      jest.spyOn(prixService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prix });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prixService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBoisson', () => {
      it('Should forward to boissonService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(boissonService, 'compareBoisson');
        comp.compareBoisson(entity, entity2);
        expect(boissonService.compareBoisson).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
