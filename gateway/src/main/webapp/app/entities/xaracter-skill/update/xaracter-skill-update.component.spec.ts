import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { XaracterSkillService } from '../service/xaracter-skill.service';
import { IXaracterSkill, XaracterSkill } from '../xaracter-skill.model';
import { IXaracter } from 'app/entities/xaracter/xaracter.model';
import { XaracterService } from 'app/entities/xaracter/service/xaracter.service';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

import { XaracterSkillUpdateComponent } from './xaracter-skill-update.component';

describe('XaracterSkill Management Update Component', () => {
  let comp: XaracterSkillUpdateComponent;
  let fixture: ComponentFixture<XaracterSkillUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let xaracterSkillService: XaracterSkillService;
  let xaracterService: XaracterService;
  let skillService: SkillService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [XaracterSkillUpdateComponent],
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
      .overrideTemplate(XaracterSkillUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(XaracterSkillUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    xaracterSkillService = TestBed.inject(XaracterSkillService);
    xaracterService = TestBed.inject(XaracterService);
    skillService = TestBed.inject(SkillService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Xaracter query and add missing value', () => {
      const xaracterSkill: IXaracterSkill = { id: 456 };
      const xaracterId: IXaracter = { id: 83842 };
      xaracterSkill.xaracterId = xaracterId;

      const xaracterCollection: IXaracter[] = [{ id: 52570 }];
      jest.spyOn(xaracterService, 'query').mockReturnValue(of(new HttpResponse({ body: xaracterCollection })));
      const additionalXaracters = [xaracterId];
      const expectedCollection: IXaracter[] = [...additionalXaracters, ...xaracterCollection];
      jest.spyOn(xaracterService, 'addXaracterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xaracterSkill });
      comp.ngOnInit();

      expect(xaracterService.query).toHaveBeenCalled();
      expect(xaracterService.addXaracterToCollectionIfMissing).toHaveBeenCalledWith(xaracterCollection, ...additionalXaracters);
      expect(comp.xaractersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Skill query and add missing value', () => {
      const xaracterSkill: IXaracterSkill = { id: 456 };
      const skillId: ISkill = { id: 64893 };
      xaracterSkill.skillId = skillId;

      const skillCollection: ISkill[] = [{ id: 60054 }];
      jest.spyOn(skillService, 'query').mockReturnValue(of(new HttpResponse({ body: skillCollection })));
      const additionalSkills = [skillId];
      const expectedCollection: ISkill[] = [...additionalSkills, ...skillCollection];
      jest.spyOn(skillService, 'addSkillToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xaracterSkill });
      comp.ngOnInit();

      expect(skillService.query).toHaveBeenCalled();
      expect(skillService.addSkillToCollectionIfMissing).toHaveBeenCalledWith(skillCollection, ...additionalSkills);
      expect(comp.skillsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const xaracterSkill: IXaracterSkill = { id: 456 };
      const xaracterId: IXaracter = { id: 35984 };
      xaracterSkill.xaracterId = xaracterId;
      const skillId: ISkill = { id: 15304 };
      xaracterSkill.skillId = skillId;

      activatedRoute.data = of({ xaracterSkill });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(xaracterSkill));
      expect(comp.xaractersSharedCollection).toContain(xaracterId);
      expect(comp.skillsSharedCollection).toContain(skillId);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterSkill>>();
      const xaracterSkill = { id: 123 };
      jest.spyOn(xaracterSkillService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterSkill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracterSkill }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(xaracterSkillService.update).toHaveBeenCalledWith(xaracterSkill);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterSkill>>();
      const xaracterSkill = new XaracterSkill();
      jest.spyOn(xaracterSkillService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterSkill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracterSkill }));
      saveSubject.complete();

      // THEN
      expect(xaracterSkillService.create).toHaveBeenCalledWith(xaracterSkill);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterSkill>>();
      const xaracterSkill = { id: 123 };
      jest.spyOn(xaracterSkillService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterSkill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(xaracterSkillService.update).toHaveBeenCalledWith(xaracterSkill);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackXaracterById', () => {
      it('Should return tracked Xaracter primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackXaracterById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSkillById', () => {
      it('Should return tracked Skill primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSkillById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
