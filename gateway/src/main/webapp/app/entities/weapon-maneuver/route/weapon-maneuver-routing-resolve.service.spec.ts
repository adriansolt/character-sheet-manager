import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IWeaponManeuver, WeaponManeuver } from '../weapon-maneuver.model';
import { WeaponManeuverService } from '../service/weapon-maneuver.service';

import { WeaponManeuverRoutingResolveService } from './weapon-maneuver-routing-resolve.service';

describe('WeaponManeuver routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WeaponManeuverRoutingResolveService;
  let service: WeaponManeuverService;
  let resultWeaponManeuver: IWeaponManeuver | undefined;

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
    routingResolveService = TestBed.inject(WeaponManeuverRoutingResolveService);
    service = TestBed.inject(WeaponManeuverService);
    resultWeaponManeuver = undefined;
  });

  describe('resolve', () => {
    it('should return IWeaponManeuver returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWeaponManeuver = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWeaponManeuver).toEqual({ id: 123 });
    });

    it('should return new IWeaponManeuver if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWeaponManeuver = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWeaponManeuver).toEqual(new WeaponManeuver());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WeaponManeuver })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWeaponManeuver = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWeaponManeuver).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
