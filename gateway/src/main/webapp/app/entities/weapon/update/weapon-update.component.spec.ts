import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WeaponService } from '../service/weapon.service';
import { IWeapon, Weapon } from '../weapon.model';

import { WeaponUpdateComponent } from './weapon-update.component';

describe('Weapon Management Update Component', () => {
  let comp: WeaponUpdateComponent;
  let fixture: ComponentFixture<WeaponUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let weaponService: WeaponService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WeaponUpdateComponent],
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
      .overrideTemplate(WeaponUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WeaponUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    weaponService = TestBed.inject(WeaponService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const weapon: IWeapon = { id: 456 };

      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(weapon));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Weapon>>();
      const weapon = { id: 123 };
      jest.spyOn(weaponService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weapon }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(weaponService.update).toHaveBeenCalledWith(weapon);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Weapon>>();
      const weapon = new Weapon();
      jest.spyOn(weaponService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weapon }));
      saveSubject.complete();

      // THEN
      expect(weaponService.create).toHaveBeenCalledWith(weapon);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Weapon>>();
      const weapon = { id: 123 };
      jest.spyOn(weaponService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(weaponService.update).toHaveBeenCalledWith(weapon);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
