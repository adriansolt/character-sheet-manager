import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IManeuver, Maneuver } from '../maneuver.model';

import { ManeuverService } from './maneuver.service';

describe('Maneuver Service', () => {
  let service: ManeuverService;
  let httpMock: HttpTestingController;
  let elemDefault: IManeuver;
  let expectedResult: IManeuver | IManeuver[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ManeuverService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      modifier: 0,
      description: 'AAAAAAA',
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

    it('should create a Maneuver', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Maneuver()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Maneuver', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          modifier: 1,
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Maneuver', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Maneuver()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Maneuver', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          modifier: 1,
          description: 'BBBBBB',
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

    it('should delete a Maneuver', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addManeuverToCollectionIfMissing', () => {
      it('should add a Maneuver to an empty array', () => {
        const maneuver: IManeuver = { id: 123 };
        expectedResult = service.addManeuverToCollectionIfMissing([], maneuver);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maneuver);
      });

      it('should not add a Maneuver to an array that contains it', () => {
        const maneuver: IManeuver = { id: 123 };
        const maneuverCollection: IManeuver[] = [
          {
            ...maneuver,
          },
          { id: 456 },
        ];
        expectedResult = service.addManeuverToCollectionIfMissing(maneuverCollection, maneuver);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Maneuver to an array that doesn't contain it", () => {
        const maneuver: IManeuver = { id: 123 };
        const maneuverCollection: IManeuver[] = [{ id: 456 }];
        expectedResult = service.addManeuverToCollectionIfMissing(maneuverCollection, maneuver);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maneuver);
      });

      it('should add only unique Maneuver to an array', () => {
        const maneuverArray: IManeuver[] = [{ id: 123 }, { id: 456 }, { id: 44058 }];
        const maneuverCollection: IManeuver[] = [{ id: 123 }];
        expectedResult = service.addManeuverToCollectionIfMissing(maneuverCollection, ...maneuverArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maneuver: IManeuver = { id: 123 };
        const maneuver2: IManeuver = { id: 456 };
        expectedResult = service.addManeuverToCollectionIfMissing([], maneuver, maneuver2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maneuver);
        expect(expectedResult).toContain(maneuver2);
      });

      it('should accept null and undefined values', () => {
        const maneuver: IManeuver = { id: 123 };
        expectedResult = service.addManeuverToCollectionIfMissing([], null, maneuver, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maneuver);
      });

      it('should return initial array if no Maneuver is added', () => {
        const maneuverCollection: IManeuver[] = [{ id: 123 }];
        expectedResult = service.addManeuverToCollectionIfMissing(maneuverCollection, undefined, null);
        expect(expectedResult).toEqual(maneuverCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
