import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWeaponManeuver, WeaponManeuver } from '../weapon-maneuver.model';

import { WeaponManeuverService } from './weapon-maneuver.service';

describe('WeaponManeuver Service', () => {
  let service: WeaponManeuverService;
  let httpMock: HttpTestingController;
  let elemDefault: IWeaponManeuver;
  let expectedResult: IWeaponManeuver | IWeaponManeuver[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WeaponManeuverService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a WeaponManeuver', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new WeaponManeuver()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WeaponManeuver', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WeaponManeuver', () => {
      const patchObject = Object.assign({}, new WeaponManeuver());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WeaponManeuver', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a WeaponManeuver', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWeaponManeuverToCollectionIfMissing', () => {
      it('should add a WeaponManeuver to an empty array', () => {
        const weaponManeuver: IWeaponManeuver = { id: 123 };
        expectedResult = service.addWeaponManeuverToCollectionIfMissing([], weaponManeuver);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weaponManeuver);
      });

      it('should not add a WeaponManeuver to an array that contains it', () => {
        const weaponManeuver: IWeaponManeuver = { id: 123 };
        const weaponManeuverCollection: IWeaponManeuver[] = [
          {
            ...weaponManeuver,
          },
          { id: 456 },
        ];
        expectedResult = service.addWeaponManeuverToCollectionIfMissing(weaponManeuverCollection, weaponManeuver);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WeaponManeuver to an array that doesn't contain it", () => {
        const weaponManeuver: IWeaponManeuver = { id: 123 };
        const weaponManeuverCollection: IWeaponManeuver[] = [{ id: 456 }];
        expectedResult = service.addWeaponManeuverToCollectionIfMissing(weaponManeuverCollection, weaponManeuver);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weaponManeuver);
      });

      it('should add only unique WeaponManeuver to an array', () => {
        const weaponManeuverArray: IWeaponManeuver[] = [{ id: 123 }, { id: 456 }, { id: 66727 }];
        const weaponManeuverCollection: IWeaponManeuver[] = [{ id: 123 }];
        expectedResult = service.addWeaponManeuverToCollectionIfMissing(weaponManeuverCollection, ...weaponManeuverArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const weaponManeuver: IWeaponManeuver = { id: 123 };
        const weaponManeuver2: IWeaponManeuver = { id: 456 };
        expectedResult = service.addWeaponManeuverToCollectionIfMissing([], weaponManeuver, weaponManeuver2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weaponManeuver);
        expect(expectedResult).toContain(weaponManeuver2);
      });

      it('should accept null and undefined values', () => {
        const weaponManeuver: IWeaponManeuver = { id: 123 };
        expectedResult = service.addWeaponManeuverToCollectionIfMissing([], null, weaponManeuver, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weaponManeuver);
      });

      it('should return initial array if no WeaponManeuver is added', () => {
        const weaponManeuverCollection: IWeaponManeuver[] = [{ id: 123 }];
        expectedResult = service.addWeaponManeuverToCollectionIfMissing(weaponManeuverCollection, undefined, null);
        expect(expectedResult).toEqual(weaponManeuverCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
