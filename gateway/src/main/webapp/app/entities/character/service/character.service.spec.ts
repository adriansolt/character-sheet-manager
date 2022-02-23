import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Handedness } from 'app/entities/enumerations/handedness.model';
import { ICharacter, Character } from '../character.model';

import { CharacterService } from './character.service';

describe('Character Service', () => {
  let service: CharacterService;
  let httpMock: HttpTestingController;
  let elemDefault: ICharacter;
  let expectedResult: ICharacter | ICharacter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CharacterService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      weight: 0,
      height: 0,
      points: 0,
      pictureContentType: 'image/png',
      picture: 'AAAAAAA',
      handedness: Handedness.RIGHT,
      campaignId: 0,
      active: false,
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

    it('should create a Character', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Character()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Character', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          weight: 1,
          height: 1,
          points: 1,
          picture: 'BBBBBB',
          handedness: 'BBBBBB',
          campaignId: 1,
          active: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Character', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          points: 1,
          campaignId: 1,
        },
        new Character()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Character', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          weight: 1,
          height: 1,
          points: 1,
          picture: 'BBBBBB',
          handedness: 'BBBBBB',
          campaignId: 1,
          active: true,
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

    it('should delete a Character', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCharacterToCollectionIfMissing', () => {
      it('should add a Character to an empty array', () => {
        const character: ICharacter = { id: 123 };
        expectedResult = service.addCharacterToCollectionIfMissing([], character);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(character);
      });

      it('should not add a Character to an array that contains it', () => {
        const character: ICharacter = { id: 123 };
        const characterCollection: ICharacter[] = [
          {
            ...character,
          },
          { id: 456 },
        ];
        expectedResult = service.addCharacterToCollectionIfMissing(characterCollection, character);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Character to an array that doesn't contain it", () => {
        const character: ICharacter = { id: 123 };
        const characterCollection: ICharacter[] = [{ id: 456 }];
        expectedResult = service.addCharacterToCollectionIfMissing(characterCollection, character);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(character);
      });

      it('should add only unique Character to an array', () => {
        const characterArray: ICharacter[] = [{ id: 123 }, { id: 456 }, { id: 9941 }];
        const characterCollection: ICharacter[] = [{ id: 123 }];
        expectedResult = service.addCharacterToCollectionIfMissing(characterCollection, ...characterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const character: ICharacter = { id: 123 };
        const character2: ICharacter = { id: 456 };
        expectedResult = service.addCharacterToCollectionIfMissing([], character, character2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(character);
        expect(expectedResult).toContain(character2);
      });

      it('should accept null and undefined values', () => {
        const character: ICharacter = { id: 123 };
        expectedResult = service.addCharacterToCollectionIfMissing([], null, character, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(character);
      });

      it('should return initial array if no Character is added', () => {
        const characterCollection: ICharacter[] = [{ id: 123 }];
        expectedResult = service.addCharacterToCollectionIfMissing(characterCollection, undefined, null);
        expect(expectedResult).toEqual(characterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
