import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICharacterEquippedWeapon, CharacterEquippedWeapon } from '../character-equipped-weapon.model';
import { CharacterEquippedWeaponService } from '../service/character-equipped-weapon.service';

import { CharacterEquippedWeaponRoutingResolveService } from './character-equipped-weapon-routing-resolve.service';

describe('CharacterEquippedWeapon routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CharacterEquippedWeaponRoutingResolveService;
  let service: CharacterEquippedWeaponService;
  let resultCharacterEquippedWeapon: ICharacterEquippedWeapon | undefined;

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
    routingResolveService = TestBed.inject(CharacterEquippedWeaponRoutingResolveService);
    service = TestBed.inject(CharacterEquippedWeaponService);
    resultCharacterEquippedWeapon = undefined;
  });

  describe('resolve', () => {
    it('should return ICharacterEquippedWeapon returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterEquippedWeapon = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCharacterEquippedWeapon).toEqual({ id: 123 });
    });

    it('should return new ICharacterEquippedWeapon if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterEquippedWeapon = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCharacterEquippedWeapon).toEqual(new CharacterEquippedWeapon());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CharacterEquippedWeapon })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterEquippedWeapon = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCharacterEquippedWeapon).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
