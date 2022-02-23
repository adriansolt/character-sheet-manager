import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICharacterAttribute, CharacterAttribute } from '../character-attribute.model';
import { CharacterAttributeService } from '../service/character-attribute.service';

import { CharacterAttributeRoutingResolveService } from './character-attribute-routing-resolve.service';

describe('CharacterAttribute routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CharacterAttributeRoutingResolveService;
  let service: CharacterAttributeService;
  let resultCharacterAttribute: ICharacterAttribute | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CharacterAttributeRoutingResolveService);
    service = TestBed.inject(CharacterAttributeService);
    resultCharacterAttribute = undefined;
  });

  describe('resolve', () => {
    it('should return ICharacterAttribute returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterAttribute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCharacterAttribute).toEqual({ id: 123 });
    });

    it('should return new ICharacterAttribute if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterAttribute = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCharacterAttribute).toEqual(new CharacterAttribute());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CharacterAttribute })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterAttribute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCharacterAttribute).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
