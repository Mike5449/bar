import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrixBoisson } from '../prix-boisson.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../prix-boisson.test-samples';

import { PrixBoissonService } from './prix-boisson.service';

const requireRestSample: IPrixBoisson = {
  ...sampleWithRequiredData,
};

describe('PrixBoisson Service', () => {
  let service: PrixBoissonService;
  let httpMock: HttpTestingController;
  let expectedResult: IPrixBoisson | IPrixBoisson[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrixBoissonService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PrixBoisson', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const prixBoisson = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prixBoisson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrixBoisson', () => {
      const prixBoisson = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prixBoisson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrixBoisson', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrixBoisson', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PrixBoisson', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPrixBoissonToCollectionIfMissing', () => {
      it('should add a PrixBoisson to an empty array', () => {
        const prixBoisson: IPrixBoisson = sampleWithRequiredData;
        expectedResult = service.addPrixBoissonToCollectionIfMissing([], prixBoisson);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prixBoisson);
      });

      it('should not add a PrixBoisson to an array that contains it', () => {
        const prixBoisson: IPrixBoisson = sampleWithRequiredData;
        const prixBoissonCollection: IPrixBoisson[] = [
          {
            ...prixBoisson,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPrixBoissonToCollectionIfMissing(prixBoissonCollection, prixBoisson);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrixBoisson to an array that doesn't contain it", () => {
        const prixBoisson: IPrixBoisson = sampleWithRequiredData;
        const prixBoissonCollection: IPrixBoisson[] = [sampleWithPartialData];
        expectedResult = service.addPrixBoissonToCollectionIfMissing(prixBoissonCollection, prixBoisson);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prixBoisson);
      });

      it('should add only unique PrixBoisson to an array', () => {
        const prixBoissonArray: IPrixBoisson[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prixBoissonCollection: IPrixBoisson[] = [sampleWithRequiredData];
        expectedResult = service.addPrixBoissonToCollectionIfMissing(prixBoissonCollection, ...prixBoissonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prixBoisson: IPrixBoisson = sampleWithRequiredData;
        const prixBoisson2: IPrixBoisson = sampleWithPartialData;
        expectedResult = service.addPrixBoissonToCollectionIfMissing([], prixBoisson, prixBoisson2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prixBoisson);
        expect(expectedResult).toContain(prixBoisson2);
      });

      it('should accept null and undefined values', () => {
        const prixBoisson: IPrixBoisson = sampleWithRequiredData;
        expectedResult = service.addPrixBoissonToCollectionIfMissing([], null, prixBoisson, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prixBoisson);
      });

      it('should return initial array if no PrixBoisson is added', () => {
        const prixBoissonCollection: IPrixBoisson[] = [sampleWithRequiredData];
        expectedResult = service.addPrixBoissonToCollectionIfMissing(prixBoissonCollection, undefined, null);
        expect(expectedResult).toEqual(prixBoissonCollection);
      });
    });

    describe('comparePrixBoisson', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePrixBoisson(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePrixBoisson(entity1, entity2);
        const compareResult2 = service.comparePrixBoisson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePrixBoisson(entity1, entity2);
        const compareResult2 = service.comparePrixBoisson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePrixBoisson(entity1, entity2);
        const compareResult2 = service.comparePrixBoisson(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
