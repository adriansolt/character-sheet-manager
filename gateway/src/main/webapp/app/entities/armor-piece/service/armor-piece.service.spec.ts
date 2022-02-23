import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ArmorLocation } from 'app/entities/enumerations/armor-location.model';
import { IArmorPiece, ArmorPiece } from '../armor-piece.model';

import { ArmorPieceService } from './armor-piece.service';

describe('ArmorPiece Service', () => {
  let service: ArmorPieceService;
  let httpMock: HttpTestingController;
  let elemDefault: IArmorPiece;
  let expectedResult: IArmorPiece | IArmorPiece[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArmorPieceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      location: ArmorLocation.HEAD,
      defenseModifier: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ArmorPiece', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ArmorPiece()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArmorPiece', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          location: 'BBBBBB',
          defenseModifier: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ArmorPiece', () => {
      const patchObject = Object.assign(
        {
          location: 'BBBBBB',
          defenseModifier: 1,
        },
        new ArmorPiece()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ArmorPiece', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          location: 'BBBBBB',
          defenseModifier: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ArmorPiece', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addArmorPieceToCollectionIfMissing', () => {
      it('should add a ArmorPiece to an empty array', () => {
        const armorPiece: IArmorPiece = { id: 123 };
        expectedResult = service.addArmorPieceToCollectionIfMissing([], armorPiece);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(armorPiece);
      });

      it('should not add a ArmorPiece to an array that contains it', () => {
        const armorPiece: IArmorPiece = { id: 123 };
        const armorPieceCollection: IArmorPiece[] = [
          {
            ...armorPiece,
          },
          { id: 456 },
        ];
        expectedResult = service.addArmorPieceToCollectionIfMissing(armorPieceCollection, armorPiece);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArmorPiece to an array that doesn't contain it", () => {
        const armorPiece: IArmorPiece = { id: 123 };
        const armorPieceCollection: IArmorPiece[] = [{ id: 456 }];
        expectedResult = service.addArmorPieceToCollectionIfMissing(armorPieceCollection, armorPiece);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(armorPiece);
      });

      it('should add only unique ArmorPiece to an array', () => {
        const armorPieceArray: IArmorPiece[] = [{ id: 123 }, { id: 456 }, { id: 99244 }];
        const armorPieceCollection: IArmorPiece[] = [{ id: 123 }];
        expectedResult = service.addArmorPieceToCollectionIfMissing(armorPieceCollection, ...armorPieceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const armorPiece: IArmorPiece = { id: 123 };
        const armorPiece2: IArmorPiece = { id: 456 };
        expectedResult = service.addArmorPieceToCollectionIfMissing([], armorPiece, armorPiece2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(armorPiece);
        expect(expectedResult).toContain(armorPiece2);
      });

      it('should accept null and undefined values', () => {
        const armorPiece: IArmorPiece = { id: 123 };
        expectedResult = service.addArmorPieceToCollectionIfMissing([], null, armorPiece, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(armorPiece);
      });

      it('should return initial array if no ArmorPiece is added', () => {
        const armorPieceCollection: IArmorPiece[] = [{ id: 123 }];
        expectedResult = service.addArmorPieceToCollectionIfMissing(armorPieceCollection, undefined, null);
        expect(expectedResult).toEqual(armorPieceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
