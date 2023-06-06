import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BoissonFormService } from './boisson-form.service';
import { BoissonService } from '../service/boisson.service';
import { IBoisson } from '../boisson.model';

import { BoissonUpdateComponent } from './boisson-update.component';

describe('Boisson Management Update Component', () => {
  let comp: BoissonUpdateComponent;
  let fixture: ComponentFixture<BoissonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let boissonFormService: BoissonFormService;
  let boissonService: BoissonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BoissonUpdateComponent],
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
      .overrideTemplate(BoissonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BoissonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    boissonFormService = TestBed.inject(BoissonFormService);
    boissonService = TestBed.inject(BoissonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const boisson: IBoisson = { id: 456 };

      activatedRoute.data = of({ boisson });
      comp.ngOnInit();

      expect(comp.boisson).toEqual(boisson);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBoisson>>();
      const boisson = { id: 123 };
      jest.spyOn(boissonFormService, 'getBoisson').mockReturnValue(boisson);
      jest.spyOn(boissonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boisson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: boisson }));
      saveSubject.complete();

      // THEN
      expect(boissonFormService.getBoisson).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(boissonService.update).toHaveBeenCalledWith(expect.objectContaining(boisson));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBoisson>>();
      const boisson = { id: 123 };
      jest.spyOn(boissonFormService, 'getBoisson').mockReturnValue({ id: null });
      jest.spyOn(boissonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boisson: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: boisson }));
      saveSubject.complete();

      // THEN
      expect(boissonFormService.getBoisson).toHaveBeenCalled();
      expect(boissonService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBoisson>>();
      const boisson = { id: 123 };
      jest.spyOn(boissonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boisson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(boissonService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
