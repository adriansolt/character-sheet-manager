import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IXaracterSkill, XaracterSkill } from '../xaracter-skill.model';

import { XaracterSkillService } from './xaracter-skill.service';

describe('XaracterSkill Service', () => {
  let service: XaracterSkillService;
  let httpMock: HttpTestingController;
  let elemDefault: IXaracterSkill;
  let expectedResult: IXaracterSkill | IXaracterSkill[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(XaracterSkillService);
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

    it('should create a XaracterSkill', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new XaracterSkill()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a XaracterSkill', () => {
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

    it('should partial update a XaracterSkill', () => {
      const patchObject = Object.assign(
        {
          points: 1,
        },
        new XaracterSkill()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of XaracterSkill', () => {
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

    it('should delete a XaracterSkill', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addXaracterSkillToCollectionIfMissing', () => {
      it('should add a XaracterSkill to an empty array', () => {
        const xaracterSkill: IXaracterSkill = { id: 123 };
        expectedResult = service.addXaracterSkillToCollectionIfMissing([], xaracterSkill);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracterSkill);
      });

      it('should not add a XaracterSkill to an array that contains it', () => {
        const xaracterSkill: IXaracterSkill = { id: 123 };
        const xaracterSkillCollection: IXaracterSkill[] = [
          {
            ...xaracterSkill,
          },
          { id: 456 },
        ];
        expectedResult = service.addXaracterSkillToCollectionIfMissing(xaracterSkillCollection, xaracterSkill);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a XaracterSkill to an array that doesn't contain it", () => {
        const xaracterSkill: IXaracterSkill = { id: 123 };
        const xaracterSkillCollection: IXaracterSkill[] = [{ id: 456 }];
        expectedResult = service.addXaracterSkillToCollectionIfMissing(xaracterSkillCollection, xaracterSkill);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracterSkill);
      });

      it('should add only unique XaracterSkill to an array', () => {
        const xaracterSkillArray: IXaracterSkill[] = [{ id: 123 }, { id: 456 }, { id: 59108 }];
        const xaracterSkillCollection: IXaracterSkill[] = [{ id: 123 }];
        expectedResult = service.addXaracterSkillToCollectionIfMissing(xaracterSkillCollection, ...xaracterSkillArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const xaracterSkill: IXaracterSkill = { id: 123 };
        const xaracterSkill2: IXaracterSkill = { id: 456 };
        expectedResult = service.addXaracterSkillToCollectionIfMissing([], xaracterSkill, xaracterSkill2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracterSkill);
        expect(expectedResult).toContain(xaracterSkill2);
      });

      it('should accept null and undefined values', () => {
        const xaracterSkill: IXaracterSkill = { id: 123 };
        expectedResult = service.addXaracterSkillToCollectionIfMissing([], null, xaracterSkill, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracterSkill);
      });

      it('should return initial array if no XaracterSkill is added', () => {
        const xaracterSkillCollection: IXaracterSkill[] = [{ id: 123 }];
        expectedResult = service.addXaracterSkillToCollectionIfMissing(xaracterSkillCollection, undefined, null);
        expect(expectedResult).toEqual(xaracterSkillCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
