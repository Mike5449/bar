import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompteCaisse } from '../compte-caisse.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../compte-caisse.test-samples';

import { CompteCaisseService } from './compte-caisse.service';

const requireRestSample: ICompteCaisse = {
  ...sampleWithRequiredData,
};

describe('CompteCaisse Service', () => {
  let service: CompteCaisseService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompteCaisse | ICompteCaisse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompteCaisseService);
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

    it('should create a CompteCaisse', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const compteCaisse = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compteCaisse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompteCaisse', () => {
      const compteCaisse = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compteCaisse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompteCaisse', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompteCaisse', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompteCaisse', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompteCaisseToCollectionIfMissing', () => {
      it('should add a CompteCaisse to an empty array', () => {
        const compteCaisse: ICompteCaisse = sampleWithRequiredData;
        expectedResult = service.addCompteCaisseToCollectionIfMissing([], compteCaisse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compteCaisse);
      });

      it('should not add a CompteCaisse to an array that contains it', () => {
        const compteCaisse: ICompteCaisse = sampleWithRequiredData;
        const compteCaisseCollection: ICompteCaisse[] = [
          {
            ...compteCaisse,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompteCaisseToCollectionIfMissing(compteCaisseCollection, compteCaisse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompteCaisse to an array that doesn't contain it", () => {
        const compteCaisse: ICompteCaisse = sampleWithRequiredData;
        const compteCaisseCollection: ICompteCaisse[] = [sampleWithPartialData];
        expectedResult = service.addCompteCaisseToCollectionIfMissing(compteCaisseCollection, compteCaisse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteCaisse);
      });

      it('should add only unique CompteCaisse to an array', () => {
        const compteCaisseArray: ICompteCaisse[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compteCaisseCollection: ICompteCaisse[] = [sampleWithRequiredData];
        expectedResult = service.addCompteCaisseToCollectionIfMissing(compteCaisseCollection, ...compteCaisseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compteCaisse: ICompteCaisse = sampleWithRequiredData;
        const compteCaisse2: ICompteCaisse = sampleWithPartialData;
        expectedResult = service.addCompteCaisseToCollectionIfMissing([], compteCaisse, compteCaisse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteCaisse);
        expect(expectedResult).toContain(compteCaisse2);
      });

      it('should accept null and undefined values', () => {
        const compteCaisse: ICompteCaisse = sampleWithRequiredData;
        expectedResult = service.addCompteCaisseToCollectionIfMissing([], null, compteCaisse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compteCaisse);
      });

      it('should return initial array if no CompteCaisse is added', () => {
        const compteCaisseCollection: ICompteCaisse[] = [sampleWithRequiredData];
        expectedResult = service.addCompteCaisseToCollectionIfMissing(compteCaisseCollection, undefined, null);
        expect(expectedResult).toEqual(compteCaisseCollection);
      });
    });

    describe('compareCompteCaisse', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompteCaisse(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCompteCaisse(entity1, entity2);
        const compareResult2 = service.compareCompteCaisse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCompteCaisse(entity1, entity2);
        const compareResult2 = service.compareCompteCaisse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCompteCaisse(entity1, entity2);
        const compareResult2 = service.compareCompteCaisse(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
