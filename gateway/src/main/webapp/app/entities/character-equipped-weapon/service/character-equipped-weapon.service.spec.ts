import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Handedness } from 'app/entities/enumerations/handedness.model';
import { ICharacterEquippedWeapon, CharacterEquippedWeapon } from '../character-equipped-weapon.model';

import { CharacterEquippedWeaponService } from './character-equipped-weapon.service';

describe('CharacterEquippedWeapon Service', () => {
  let service: CharacterEquippedWeaponService;
  let httpMock: HttpTestingController;
  let elemDefault: ICharacterEquippedWeapon;
  let expectedResult: ICharacterEquippedWeapon | ICharacterEquippedWeapon[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CharacterEquippedWeaponService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      hand: Handedness.RIGHT,
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

    it('should create a CharacterEquippedWeapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CharacterEquippedWeapon()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CharacterEquippedWeapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          hand: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CharacterEquippedWeapon', () => {
      const patchObject = Object.assign(
        {
          hand: 'BBBBBB',
        },
        new CharacterEquippedWeapon()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CharacterEquippedWeapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          hand: 'BBBBBB',
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

    it('should delete a CharacterEquippedWeapon', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCharacterEquippedWeaponToCollectionIfMissing', () => {
      it('should add a CharacterEquippedWeapon to an empty array', () => {
        const characterEquippedWeapon: ICharacterEquippedWeapon = { id: 123 };
        expectedResult = service.addCharacterEquippedWeaponToCollectionIfMissing([], characterEquippedWeapon);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characterEquippedWeapon);
      });

      it('should not add a CharacterEquippedWeapon to an array that contains it', () => {
        const characterEquippedWeapon: ICharacterEquippedWeapon = { id: 123 };
        const characterEquippedWeaponCollection: ICharacterEquippedWeapon[] = [
          {
            ...characterEquippedWeapon,
          },
          { id: 456 },
        ];
        expectedResult = service.addCharacterEquippedWeaponToCollectionIfMissing(
          characterEquippedWeaponCollection,
          characterEquippedWeapon
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CharacterEquippedWeapon to an array that doesn't contain it", () => {
        const characterEquippedWeapon: ICharacterEquippedWeapon = { id: 123 };
        const characterEquippedWeaponCollection: ICharacterEquippedWeapon[] = [{ id: 456 }];
        expectedResult = service.addCharacterEquippedWeaponToCollectionIfMissing(
          characterEquippedWeaponCollection,
          characterEquippedWeapon
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characterEquippedWeapon);
      });

      it('should add only unique CharacterEquippedWeapon to an array', () => {
        const characterEquippedWeaponArray: ICharacterEquippedWeapon[] = [{ id: 123 }, { id: 456 }, { id: 86856 }];
        const characterEquippedWeaponCollection: ICharacterEquippedWeapon[] = [{ id: 123 }];
        expectedResult = service.addCharacterEquippedWeaponToCollectionIfMissing(
          characterEquippedWeaponCollection,
          ...characterEquippedWeaponArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const characterEquippedWeapon: ICharacterEquippedWeapon = { id: 123 };
        const characterEquippedWeapon2: ICharacterEquippedWeapon = { id: 456 };
        expectedResult = service.addCharacterEquippedWeaponToCollectionIfMissing([], characterEquippedWeapon, characterEquippedWeapon2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characterEquippedWeapon);
        expect(expectedResult).toContain(characterEquippedWeapon2);
      });

      it('should accept null and undefined values', () => {
        const characterEquippedWeapon: ICharacterEquippedWeapon = { id: 123 };
        expectedResult = service.addCharacterEquippedWeaponToCollectionIfMissing([], null, characterEquippedWeapon, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characterEquippedWeapon);
      });

      it('should return initial array if no CharacterEquippedWeapon is added', () => {
        const characterEquippedWeaponCollection: ICharacterEquippedWeapon[] = [{ id: 123 }];
        expectedResult = service.addCharacterEquippedWeaponToCollectionIfMissing(characterEquippedWeaponCollection, undefined, null);
        expect(expectedResult).toEqual(characterEquippedWeaponCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
