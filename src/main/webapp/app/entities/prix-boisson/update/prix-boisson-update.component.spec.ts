import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrixBoissonFormService } from './prix-boisson-form.service';
import { PrixBoissonService } from '../service/prix-boisson.service';
import { IPrixBoisson } from '../prix-boisson.model';
import { IBoisson } from 'app/entities/boisson/boisson.model';
import { BoissonService } from 'app/entities/boisson/service/boisson.service';

import { PrixBoissonUpdateComponent } from './prix-boisson-update.component';

describe('PrixBoisson Management Update Component', () => {
  let comp: PrixBoissonUpdateComponent;
  let fixture: ComponentFixture<PrixBoissonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prixBoissonFormService: PrixBoissonFormService;
  let prixBoissonService: PrixBoissonService;
  let boissonService: BoissonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrixBoissonUpdateComponent],
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
      .overrideTemplate(PrixBoissonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrixBoissonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prixBoissonFormService = TestBed.inject(PrixBoissonFormService);
    prixBoissonService = TestBed.inject(PrixBoissonService);
    boissonService = TestBed.inject(BoissonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Boisson query and add missing value', () => {
      const prixBoisson: IPrixBoisson = { id: 456 };
      const boisson: IBoisson = { id: 66513 };
      prixBoisson.boisson = boisson;

      const boissonCollection: IBoisson[] = [{ id: 99321 }];
      jest.spyOn(boissonService, 'query').mockReturnValue(of(new HttpResponse({ body: boissonCollection })));
      const additionalBoissons = [boisson];
      const expectedCollection: IBoisson[] = [...additionalBoissons, ...boissonCollection];
      jest.spyOn(boissonService, 'addBoissonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prixBoisson });
      comp.ngOnInit();

      expect(boissonService.query).toHaveBeenCalled();
      expect(boissonService.addBoissonToCollectionIfMissing).toHaveBeenCalledWith(
        boissonCollection,
        ...additionalBoissons.map(expect.objectContaining)
      );
      expect(comp.boissonsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prixBoisson: IPrixBoisson = { id: 456 };
      const boisson: IBoisson = { id: 51706 };
      prixBoisson.boisson = boisson;

      activatedRoute.data = of({ prixBoisson });
      comp.ngOnInit();

      expect(comp.boissonsSharedCollection).toContain(boisson);
      expect(comp.prixBoisson).toEqual(prixBoisson);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrixBoisson>>();
      const prixBoisson = { id: 123 };
      jest.spyOn(prixBoissonFormService, 'getPrixBoisson').mockReturnValue(prixBoisson);
      jest.spyOn(prixBoissonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prixBoisson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prixBoisson }));
      saveSubject.complete();

      // THEN
      expect(prixBoissonFormService.getPrixBoisson).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prixBoissonService.update).toHaveBeenCalledWith(expect.objectContaining(prixBoisson));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrixBoisson>>();
      const prixBoisson = { id: 123 };
      jest.spyOn(prixBoissonFormService, 'getPrixBoisson').mockReturnValue({ id: null });
      jest.spyOn(prixBoissonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prixBoisson: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prixBoisson }));
      saveSubject.complete();

      // THEN
      expect(prixBoissonFormService.getPrixBoisson).toHaveBeenCalled();
      expect(prixBoissonService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrixBoisson>>();
      const prixBoisson = { id: 123 };
      jest.spyOn(prixBoissonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prixBoisson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prixBoissonService.update).toHaveBeenCalled();
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
