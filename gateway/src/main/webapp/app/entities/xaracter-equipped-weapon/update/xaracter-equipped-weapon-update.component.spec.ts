import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { XaracterEquippedWeaponService } from '../service/xaracter-equipped-weapon.service';
import { IXaracterEquippedWeapon, XaracterEquippedWeapon } from '../xaracter-equipped-weapon.model';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { WeaponService } from 'app/entities/weapon/service/weapon.service';

import { XaracterEquippedWeaponUpdateComponent } from './xaracter-equipped-weapon-update.component';

describe('XaracterEquippedWeapon Management Update Component', () => {
  let comp: XaracterEquippedWeaponUpdateComponent;
  let fixture: ComponentFixture<XaracterEquippedWeaponUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let xaracterEquippedWeaponService: XaracterEquippedWeaponService;
  let weaponService: WeaponService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [XaracterEquippedWeaponUpdateComponent],
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
      .overrideTemplate(XaracterEquippedWeaponUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(XaracterEquippedWeaponUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    xaracterEquippedWeaponService = TestBed.inject(XaracterEquippedWeaponService);
    weaponService = TestBed.inject(WeaponService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Weapon query and add missing value', () => {
      const xaracterEquippedWeapon: IXaracterEquippedWeapon = { id: 456 };
      const weaponId: IWeapon = { id: 35876 };
      xaracterEquippedWeapon.weaponId = weaponId;

      const weaponCollection: IWeapon[] = [{ id: 97963 }];
      jest.spyOn(weaponService, 'query').mockReturnValue(of(new HttpResponse({ body: weaponCollection })));
      const additionalWeapons = [weaponId];
      const expectedCollection: IWeapon[] = [...additionalWeapons, ...weaponCollection];
      jest.spyOn(weaponService, 'addWeaponToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xaracterEquippedWeapon });
      comp.ngOnInit();

      expect(weaponService.query).toHaveBeenCalled();
      expect(weaponService.addWeaponToCollectionIfMissing).toHaveBeenCalledWith(weaponCollection, ...additionalWeapons);
      expect(comp.weaponsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const xaracterEquippedWeapon: IXaracterEquippedWeapon = { id: 456 };
      const weaponId: IWeapon = { id: 97442 };
      xaracterEquippedWeapon.weaponId = weaponId;

      activatedRoute.data = of({ xaracterEquippedWeapon });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(xaracterEquippedWeapon));
      expect(comp.weaponsSharedCollection).toContain(weaponId);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterEquippedWeapon>>();
      const xaracterEquippedWeapon = { id: 123 };
      jest.spyOn(xaracterEquippedWeaponService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterEquippedWeapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracterEquippedWeapon }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(xaracterEquippedWeaponService.update).toHaveBeenCalledWith(xaracterEquippedWeapon);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterEquippedWeapon>>();
      const xaracterEquippedWeapon = new XaracterEquippedWeapon();
      jest.spyOn(xaracterEquippedWeaponService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterEquippedWeapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracterEquippedWeapon }));
      saveSubject.complete();

      // THEN
      expect(xaracterEquippedWeaponService.create).toHaveBeenCalledWith(xaracterEquippedWeapon);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterEquippedWeapon>>();
      const xaracterEquippedWeapon = { id: 123 };
      jest.spyOn(xaracterEquippedWeaponService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterEquippedWeapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(xaracterEquippedWeaponService.update).toHaveBeenCalledWith(xaracterEquippedWeapon);
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
  });
});
