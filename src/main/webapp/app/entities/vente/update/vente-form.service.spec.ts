import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vente.test-samples';

import { VenteFormService } from './vente-form.service';

describe('Vente Form Service', () => {
  let service: VenteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VenteFormService);
  });

  describe('Service methods', () => {
    describe('createVenteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVenteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantite: expect.any(Object),
            status: expect.any(Object),
            employee: expect.any(Object),
            client: expect.any(Object),
            depot: expect.any(Object),
            boisson: expect.any(Object),
          })
        );
      });

      it('passing IVente should create a new form with FormGroup', () => {
        const formGroup = service.createVenteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantite: expect.any(Object),
            status: expect.any(Object),
            employee: expect.any(Object),
            client: expect.any(Object),
            depot: expect.any(Object),
            boisson: expect.any(Object),
          })
        );
      });
    });

    describe('getVente', () => {
      it('should return NewVente for default Vente initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVenteFormGroup(sampleWithNewData);

        const vente = service.getVente(formGroup) as any;

        expect(vente).toMatchObject(sampleWithNewData);
      });

      it('should return NewVente for empty Vente initial value', () => {
        const formGroup = service.createVenteFormGroup();

        const vente = service.getVente(formGroup) as any;

        expect(vente).toMatchObject({});
      });

      it('should return IVente', () => {
        const formGroup = service.createVenteFormGroup(sampleWithRequiredData);

        const vente = service.getVente(formGroup) as any;

        expect(vente).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVente should not enable id FormControl', () => {
        const formGroup = service.createVenteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVente should disable id FormControl', () => {
        const formGroup = service.createVenteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
