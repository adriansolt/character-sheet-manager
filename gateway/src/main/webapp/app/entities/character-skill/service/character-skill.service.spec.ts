import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICharacterSkill, CharacterSkill } from '../character-skill.model';

import { CharacterSkillService } from './character-skill.service';

describe('CharacterSkill Service', () => {
  let service: CharacterSkillService;
  let httpMock: HttpTestingController;
  let elemDefault: ICharacterSkill;
  let expectedResult: ICharacterSkill | ICharacterSkill[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CharacterSkillService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      points: 0,
      skillModifier: 0,
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

    it('should create a CharacterSkill', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CharacterSkill()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CharacterSkill', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          points: 1,
          skillModifier: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CharacterSkill', () => {
      const patchObject = Object.assign({}, new CharacterSkill());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CharacterSkill', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          points: 1,
          skillModifier: 1,
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

    it('should delete a CharacterSkill', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCharacterSkillToCollectionIfMissing', () => {
      it('should add a CharacterSkill to an empty array', () => {
        const characterSkill: ICharacterSkill = { id: 123 };
        expectedResult = service.addCharacterSkillToCollectionIfMissing([], characterSkill);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characterSkill);
      });

      it('should not add a CharacterSkill to an array that contains it', () => {
        const characterSkill: ICharacterSkill = { id: 123 };
        const characterSkillCollection: ICharacterSkill[] = [
          {
            ...characterSkill,
          },
          { id: 456 },
        ];
        expectedResult = service.addCharacterSkillToCollectionIfMissing(characterSkillCollection, characterSkill);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CharacterSkill to an array that doesn't contain it", () => {
        const characterSkill: ICharacterSkill = { id: 123 };
        const characterSkillCollection: ICharacterSkill[] = [{ id: 456 }];
        expectedResult = service.addCharacterSkillToCollectionIfMissing(characterSkillCollection, characterSkill);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characterSkill);
      });

      it('should add only unique CharacterSkill to an array', () => {
        const characterSkillArray: ICharacterSkill[] = [{ id: 123 }, { id: 456 }, { id: 5703 }];
        const characterSkillCollection: ICharacterSkill[] = [{ id: 123 }];
        expectedResult = service.addCharacterSkillToCollectionIfMissing(characterSkillCollection, ...characterSkillArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const characterSkill: ICharacterSkill = { id: 123 };
        const characterSkill2: ICharacterSkill = { id: 456 };
        expectedResult = service.addCharacterSkillToCollectionIfMissing([], characterSkill, characterSkill2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characterSkill);
        expect(expectedResult).toContain(characterSkill2);
      });

      it('should accept null and undefined values', () => {
        const characterSkill: ICharacterSkill = { id: 123 };
        expectedResult = service.addCharacterSkillToCollectionIfMissing([], null, characterSkill, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characterSkill);
      });

      it('should return initial array if no CharacterSkill is added', () => {
        const characterSkillCollection: ICharacterSkill[] = [{ id: 123 }];
        expectedResult = service.addCharacterSkillToCollectionIfMissing(characterSkillCollection, undefined, null);
        expect(expectedResult).toEqual(characterSkillCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
