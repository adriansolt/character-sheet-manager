import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ManeuverService } from '../service/maneuver.service';
import { IManeuver, Maneuver } from '../maneuver.model';

import { ManeuverUpdateComponent } from './maneuver-update.component';

describe('Maneuver Management Update Component', () => {
  let comp: ManeuverUpdateComponent;
  let fixture: ComponentFixture<ManeuverUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let maneuverService: ManeuverService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ManeuverUpdateComponent],
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
      .overrideTemplate(ManeuverUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManeuverUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maneuverService = TestBed.inject(ManeuverService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const maneuver: IManeuver = { id: 456 };

      activatedRoute.data = of({ maneuver });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(maneuver));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Maneuver>>();
      const maneuver = { id: 123 };
      jest.spyOn(maneuverService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maneuver });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maneuver }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(maneuverService.update).toHaveBeenCalledWith(maneuver);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Maneuver>>();
      const maneuver = new Maneuver();
      jest.spyOn(maneuverService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maneuver });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maneuver }));
      saveSubject.complete();

      // THEN
      expect(maneuverService.create).toHaveBeenCalledWith(maneuver);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Maneuver>>();
      const maneuver = { id: 123 };
      jest.spyOn(maneuverService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maneuver });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maneuverService.update).toHaveBeenCalledWith(maneuver);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
