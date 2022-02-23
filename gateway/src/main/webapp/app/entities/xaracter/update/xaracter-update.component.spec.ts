import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { XaracterService } from '../service/xaracter.service';
import { IXaracter, Xaracter } from '../xaracter.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { XaracterUpdateComponent } from './xaracter-update.component';

describe('Xaracter Management Update Component', () => {
  let comp: XaracterUpdateComponent;
  let fixture: ComponentFixture<XaracterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let xaracterService: XaracterService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [XaracterUpdateComponent],
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
      .overrideTemplate(XaracterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(XaracterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    xaracterService = TestBed.inject(XaracterService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const xaracter: IXaracter = { id: 456 };
      const user: IUser = { id: '0e4dee98-3dd0-4410-923a-5684baa5129e' };
      xaracter.user = user;

      const userCollection: IUser[] = [{ id: '4cfcc42d-73a7-4444-b64d-af77e5f81b4c' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xaracter });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const xaracter: IXaracter = { id: 456 };
      const user: IUser = { id: 'f73fd6d7-780c-4e41-bfdc-c93204c31a51' };
      xaracter.user = user;

      activatedRoute.data = of({ xaracter });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(xaracter));
      expect(comp.usersSharedCollection).toContain(user);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Xaracter>>();
      const xaracter = { id: 123 };
      jest.spyOn(xaracterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracter }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(xaracterService.update).toHaveBeenCalledWith(xaracter);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Xaracter>>();
      const xaracter = new Xaracter();
      jest.spyOn(xaracterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracter }));
      saveSubject.complete();

      // THEN
      expect(xaracterService.create).toHaveBeenCalledWith(xaracter);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Xaracter>>();
      const xaracter = { id: 123 };
      jest.spyOn(xaracterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(xaracterService.update).toHaveBeenCalledWith(xaracter);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
