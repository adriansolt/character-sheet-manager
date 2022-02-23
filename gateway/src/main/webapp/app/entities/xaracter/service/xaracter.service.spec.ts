import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Handedness } from 'app/entities/enumerations/handedness.model';
import { IXaracter, Xaracter } from '../xaracter.model';

import { XaracterService } from './xaracter.service';

describe('Xaracter Service', () => {
  let service: XaracterService;
  let httpMock: HttpTestingController;
  let elemDefault: IXaracter;
  let expectedResult: IXaracter | IXaracter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(XaracterService);
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

    it('should create a Xaracter', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Xaracter()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Xaracter', () => {
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

    it('should partial update a Xaracter', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          weight: 1,
          points: 1,
          handedness: 'BBBBBB',
        },
        new Xaracter()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Xaracter', () => {
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

    it('should delete a Xaracter', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addXaracterToCollectionIfMissing', () => {
      it('should add a Xaracter to an empty array', () => {
        const xaracter: IXaracter = { id: 123 };
        expectedResult = service.addXaracterToCollectionIfMissing([], xaracter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracter);
      });

      it('should not add a Xaracter to an array that contains it', () => {
        const xaracter: IXaracter = { id: 123 };
        const xaracterCollection: IXaracter[] = [
          {
            ...xaracter,
          },
          { id: 456 },
        ];
        expectedResult = service.addXaracterToCollectionIfMissing(xaracterCollection, xaracter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Xaracter to an array that doesn't contain it", () => {
        const xaracter: IXaracter = { id: 123 };
        const xaracterCollection: IXaracter[] = [{ id: 456 }];
        expectedResult = service.addXaracterToCollectionIfMissing(xaracterCollection, xaracter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracter);
      });

      it('should add only unique Xaracter to an array', () => {
        const xaracterArray: IXaracter[] = [{ id: 123 }, { id: 456 }, { id: 46474 }];
        const xaracterCollection: IXaracter[] = [{ id: 123 }];
        expectedResult = service.addXaracterToCollectionIfMissing(xaracterCollection, ...xaracterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const xaracter: IXaracter = { id: 123 };
        const xaracter2: IXaracter = { id: 456 };
        expectedResult = service.addXaracterToCollectionIfMissing([], xaracter, xaracter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracter);
        expect(expectedResult).toContain(xaracter2);
      });

      it('should accept null and undefined values', () => {
        const xaracter: IXaracter = { id: 123 };
        expectedResult = service.addXaracterToCollectionIfMissing([], null, xaracter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracter);
      });

      it('should return initial array if no Xaracter is added', () => {
        const xaracterCollection: IXaracter[] = [{ id: 123 }];
        expectedResult = service.addXaracterToCollectionIfMissing(xaracterCollection, undefined, null);
        expect(expectedResult).toEqual(xaracterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
