import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CharacterEquippedWeaponService } from '../service/character-equipped-weapon.service';
import { ICharacterEquippedWeapon, CharacterEquippedWeapon } from '../character-equipped-weapon.model';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { WeaponService } from 'app/entities/weapon/service/weapon.service';

import { CharacterEquippedWeaponUpdateComponent } from './character-equipped-weapon-update.component';

describe('CharacterEquippedWeapon Management Update Component', () => {
  let comp: CharacterEquippedWeaponUpdateComponent;
  let fixture: ComponentFixture<CharacterEquippedWeaponUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let characterEquippedWeaponService: CharacterEquippedWeaponService;
  let weaponService: WeaponService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CharacterEquippedWeaponUpdateComponent],
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
      .overrideTemplate(CharacterEquippedWeaponUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CharacterEquippedWeaponUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    characterEquippedWeaponService = TestBed.inject(CharacterEquippedWeaponService);
    weaponService = TestBed.inject(WeaponService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Weapon query and add missing value', () => {
      const characterEquippedWeapon: ICharacterEquippedWeapon = { id: 456 };
      const weapon: IWeapon = { id: 25939 };
      characterEquippedWeapon.weapon = weapon;

      const weaponCollection: IWeapon[] = [{ id: 10175 }];
      jest.spyOn(weaponService, 'query').mockReturnValue(of(new HttpResponse({ body: weaponCollection })));
      const additionalWeapons = [weapon];
      const expectedCollection: IWeapon[] = [...additionalWeapons, ...weaponCollection];
      jest.spyOn(weaponService, 'addWeaponToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ characterEquippedWeapon });
      comp.ngOnInit();

      expect(weaponService.query).toHaveBeenCalled();
      expect(weaponService.addWeaponToCollectionIfMissing).toHaveBeenCalledWith(weaponCollection, ...additionalWeapons);
      expect(comp.weaponsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const characterEquippedWeapon: ICharacterEquippedWeapon = { id: 456 };
      const weapon: IWeapon = { id: 86040 };
      characterEquippedWeapon.weapon = weapon;

      activatedRoute.data = of({ characterEquippedWeapon });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(characterEquippedWeapon));
      expect(comp.weaponsSharedCollection).toContain(weapon);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterEquippedWeapon>>();
      const characterEquippedWeapon = { id: 123 };
      jest.spyOn(characterEquippedWeaponService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterEquippedWeapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characterEquippedWeapon }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(characterEquippedWeaponService.update).toHaveBeenCalledWith(characterEquippedWeapon);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterEquippedWeapon>>();
      const characterEquippedWeapon = new CharacterEquippedWeapon();
      jest.spyOn(characterEquippedWeaponService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterEquippedWeapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characterEquippedWeapon }));
      saveSubject.complete();

      // THEN
      expect(characterEquippedWeaponService.create).toHaveBeenCalledWith(characterEquippedWeapon);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterEquippedWeapon>>();
      const characterEquippedWeapon = { id: 123 };
      jest.spyOn(characterEquippedWeaponService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterEquippedWeapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(characterEquippedWeaponService.update).toHaveBeenCalledWith(characterEquippedWeapon);
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
