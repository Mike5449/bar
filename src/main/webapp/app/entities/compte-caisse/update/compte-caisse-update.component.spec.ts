import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompteCaisseFormService } from './compte-caisse-form.service';
import { CompteCaisseService } from '../service/compte-caisse.service';
import { ICompteCaisse } from '../compte-caisse.model';

import { CompteCaisseUpdateComponent } from './compte-caisse-update.component';

describe('CompteCaisse Management Update Component', () => {
  let comp: CompteCaisseUpdateComponent;
  let fixture: ComponentFixture<CompteCaisseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compteCaisseFormService: CompteCaisseFormService;
  let compteCaisseService: CompteCaisseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompteCaisseUpdateComponent],
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
      .overrideTemplate(CompteCaisseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompteCaisseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compteCaisseFormService = TestBed.inject(CompteCaisseFormService);
    compteCaisseService = TestBed.inject(CompteCaisseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const compteCaisse: ICompteCaisse = { id: 456 };

      activatedRoute.data = of({ compteCaisse });
      comp.ngOnInit();

      expect(comp.compteCaisse).toEqual(compteCaisse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteCaisse>>();
      const compteCaisse = { id: 123 };
      jest.spyOn(compteCaisseFormService, 'getCompteCaisse').mockReturnValue(compteCaisse);
      jest.spyOn(compteCaisseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteCaisse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compteCaisse }));
      saveSubject.complete();

      // THEN
      expect(compteCaisseFormService.getCompteCaisse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compteCaisseService.update).toHaveBeenCalledWith(expect.objectContaining(compteCaisse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteCaisse>>();
      const compteCaisse = { id: 123 };
      jest.spyOn(compteCaisseFormService, 'getCompteCaisse').mockReturnValue({ id: null });
      jest.spyOn(compteCaisseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteCaisse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compteCaisse }));
      saveSubject.complete();

      // THEN
      expect(compteCaisseFormService.getCompteCaisse).toHaveBeenCalled();
      expect(compteCaisseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteCaisse>>();
      const compteCaisse = { id: 123 };
      jest.spyOn(compteCaisseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteCaisse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compteCaisseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
