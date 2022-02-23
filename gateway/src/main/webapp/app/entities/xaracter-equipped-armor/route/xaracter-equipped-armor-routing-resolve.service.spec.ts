import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IXaracterEquippedArmor, XaracterEquippedArmor } from '../xaracter-equipped-armor.model';
import { XaracterEquippedArmorService } from '../service/xaracter-equipped-armor.service';

import { XaracterEquippedArmorRoutingResolveService } from './xaracter-equipped-armor-routing-resolve.service';

describe('XaracterEquippedArmor routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: XaracterEquippedArmorRoutingResolveService;
  let service: XaracterEquippedArmorService;
  let resultXaracterEquippedArmor: IXaracterEquippedArmor | undefined;

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
    routingResolveService = TestBed.inject(XaracterEquippedArmorRoutingResolveService);
    service = TestBed.inject(XaracterEquippedArmorService);
    resultXaracterEquippedArmor = undefined;
  });

  describe('resolve', () => {
    it('should return IXaracterEquippedArmor returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultXaracterEquippedArmor = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultXaracterEquippedArmor).toEqual({ id: 123 });
    });

    it('should return new IXaracterEquippedArmor if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultXaracterEquippedArmor = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultXaracterEquippedArmor).toEqual(new XaracterEquippedArmor());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as XaracterEquippedArmor })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultXaracterEquippedArmor = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultXaracterEquippedArmor).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
