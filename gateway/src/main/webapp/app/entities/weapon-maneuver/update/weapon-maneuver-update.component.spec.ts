import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WeaponManeuverService } from '../service/weapon-maneuver.service';
import { IWeaponManeuver, WeaponManeuver } from '../weapon-maneuver.model';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { WeaponService } from 'app/entities/weapon/service/weapon.service';
import { IManeuver } from 'app/entities/maneuver/maneuver.model';
import { ManeuverService } from 'app/entities/maneuver/service/maneuver.service';

import { WeaponManeuverUpdateComponent } from './weapon-maneuver-update.component';

describe('WeaponManeuver Management Update Component', () => {
  let comp: WeaponManeuverUpdateComponent;
  let fixture: ComponentFixture<WeaponManeuverUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let weaponManeuverService: WeaponManeuverService;
  let weaponService: WeaponService;
  let maneuverService: ManeuverService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WeaponManeuverUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WeaponManeuverUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WeaponManeuverUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    weaponManeuverService = TestBed.inject(WeaponManeuverService);
    weaponService = TestBed.inject(WeaponService);
    maneuverService = TestBed.inject(ManeuverService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Weapon query and add missing value', () => {
      const weaponManeuver: IWeaponManeuver = { id: 456 };
      const weaponId: IWeapon = { id: 25113 };
      weaponManeuver.weaponId = weaponId;

      const weaponCollection: IWeapon[] = [{ id: 81835 }];
      jest.spyOn(weaponService, 'query').mockReturnValue(of(new HttpResponse({ body: weaponCollection })));
      const additionalWeapons = [weaponId];
      const expectedCollection: IWeapon[] = [...additionalWeapons, ...weaponCollection];
      jest.spyOn(weaponService, 'addWeaponToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weaponManeuver });
      comp.ngOnInit();

      expect(weaponService.query).toHaveBeenCalled();
      expect(weaponService.addWeaponToCollectionIfMissing).toHaveBeenCalledWith(weaponCollection, ...additionalWeapons);
      expect(comp.weaponsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Maneuver query and add missing value', () => {
      const weaponManeuver: IWeaponManeuver = { id: 456 };
      const maneuverId: IManeuver = { id: 38278 };
      weaponManeuver.maneuverId = maneuverId;

      const maneuverCollection: IManeuver[] = [{ id: 15390 }];
      jest.spyOn(maneuverService, 'query').mockReturnValue(of(new HttpResponse({ body: maneuverCollection })));
      const additionalManeuvers = [maneuverId];
      const expectedCollection: IManeuver[] = [...additionalManeuvers, ...maneuverCollection];
      jest.spyOn(maneuverService, 'addManeuverToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weaponManeuver });
      comp.ngOnInit();

      expect(maneuverService.query).toHaveBeenCalled();
      expect(maneuverService.addManeuverToCollectionIfMissing).toHaveBeenCalledWith(maneuverCollection, ...additionalManeuvers);
      expect(comp.maneuversSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const weaponManeuver: IWeaponManeuver = { id: 456 };
      const weaponId: IWeapon = { id: 79064 };
      weaponManeuver.weaponId = weaponId;
      const maneuverId: IManeuver = { id: 76592 };
      weaponManeuver.maneuverId = maneuverId;

      activatedRoute.data = of({ weaponManeuver });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(weaponManeuver));
      expect(comp.weaponsSharedCollection).toContain(weaponId);
      expect(comp.maneuversSharedCollection).toContain(maneuverId);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeaponManeuver>>();
      const weaponManeuver = { id: 123 };
      jest.spyOn(weaponManeuverService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weaponManeuver });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weaponManeuver }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(weaponManeuverService.update).toHaveBeenCalledWith(weaponManeuver);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeaponManeuver>>();
      const weaponManeuver = new WeaponManeuver();
      jest.spyOn(weaponManeuverService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weaponManeuver });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weaponManeuver }));
      saveSubject.complete();

      // THEN
      expect(weaponManeuverService.create).toHaveBeenCalledWith(weaponManeuver);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WeaponManeuver>>();
      const weaponManeuver = { id: 123 };
      jest.spyOn(weaponManeuverService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weaponManeuver });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(weaponManeuverService.update).toHaveBeenCalledWith(weaponManeuver);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackWeaponById', () => {
      it('Should return tracked Weapon primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWeaponById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackManeuverById', () => {
      it('Should return tracked Maneuver primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackManeuverById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
