import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICharacterEquippedArmor, CharacterEquippedArmor } from '../character-equipped-armor.model';
import { CharacterEquippedArmorService } from '../service/character-equipped-armor.service';

import { CharacterEquippedArmorRoutingResolveService } from './character-equipped-armor-routing-resolve.service';

describe('CharacterEquippedArmor routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CharacterEquippedArmorRoutingResolveService;
  let service: CharacterEquippedArmorService;
  let resultCharacterEquippedArmor: ICharacterEquippedArmor | undefined;

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
    routingResolveService = TestBed.inject(CharacterEquippedArmorRoutingResolveService);
    service = TestBed.inject(CharacterEquippedArmorService);
    resultCharacterEquippedArmor = undefined;
  });

  describe('resolve', () => {
    it('should return ICharacterEquippedArmor returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterEquippedArmor = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCharacterEquippedArmor).toEqual({ id: 123 });
    });

    it('should return new ICharacterEquippedArmor if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterEquippedArmor = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCharacterEquippedArmor).toEqual(new CharacterEquippedArmor());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CharacterEquippedArmor })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCharacterEquippedArmor = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCharacterEquippedArmor).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
