jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PrixBoissonService } from '../service/prix-boisson.service';

import { PrixBoissonDeleteDialogComponent } from './prix-boisson-delete-dialog.component';

describe('PrixBoisson Management Delete Component', () => {
  let comp: PrixBoissonDeleteDialogComponent;
  let fixture: ComponentFixture<PrixBoissonDeleteDialogComponent>;
  let service: PrixBoissonService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PrixBoissonDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(PrixBoissonDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrixBoissonDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PrixBoissonService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
