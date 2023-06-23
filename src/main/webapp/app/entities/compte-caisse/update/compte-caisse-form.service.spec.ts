import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../compte-caisse.test-samples';

import { CompteCaisseFormService } from './compte-caisse-form.service';

describe('CompteCaisse Form Service', () => {
  let service: CompteCaisseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompteCaisseFormService);
  });

  describe('Service methods', () => {
    describe('createCompteCaisseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompteCaisseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            injection: expect.any(Object),
            sale: expect.any(Object),
            cancel: expect.any(Object),
            cash: expect.any(Object),
            pret: expect.any(Object),
            balance: expect.any(Object),
            aVerser: expect.any(Object),
            status: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing ICompteCaisse should create a new form with FormGroup', () => {
        const formGroup = service.createCompteCaisseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            injection: expect.any(Object),
            sale: expect.any(Object),
            cancel: expect.any(Object),
            cash: expect.any(Object),
            pret: expect.any(Object),
            balance: expect.any(Object),
            aVerser: expect.any(Object),
            status: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getCompteCaisse', () => {
      it('should return NewCompteCaisse for default CompteCaisse initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCompteCaisseFormGroup(sampleWithNewData);

        const compteCaisse = service.getCompteCaisse(formGroup) as any;

        expect(compteCaisse).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompteCaisse for empty CompteCaisse initial value', () => {
        const formGroup = service.createCompteCaisseFormGroup();

        const compteCaisse = service.getCompteCaisse(formGroup) as any;

        expect(compteCaisse).toMatchObject({});
      });

      it('should return ICompteCaisse', () => {
        const formGroup = service.createCompteCaisseFormGroup(sampleWithRequiredData);

        const compteCaisse = service.getCompteCaisse(formGroup) as any;

        expect(compteCaisse).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompteCaisse should not enable id FormControl', () => {
        const formGroup = service.createCompteCaisseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompteCaisse should disable id FormControl', () => {
        const formGroup = service.createCompteCaisseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
