import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DefaultSkillOrAtributeService } from '../service/default-skill-or-atribute.service';
import { IDefaultSkillOrAtribute, DefaultSkillOrAtribute } from '../default-skill-or-atribute.model';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

import { DefaultSkillOrAtributeUpdateComponent } from './default-skill-or-atribute-update.component';

describe('DefaultSkillOrAtribute Management Update Component', () => {
  let comp: DefaultSkillOrAtributeUpdateComponent;
  let fixture: ComponentFixture<DefaultSkillOrAtributeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let defaultSkillOrAtributeService: DefaultSkillOrAtributeService;
  let skillService: SkillService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DefaultSkillOrAtributeUpdateComponent],
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
      .overrideTemplate(DefaultSkillOrAtributeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DefaultSkillOrAtributeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    defaultSkillOrAtributeService = TestBed.inject(DefaultSkillOrAtributeService);
    skillService = TestBed.inject(SkillService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Skill query and add missing value', () => {
      const defaultSkillOrAtribute: IDefaultSkillOrAtribute = { id: 456 };
      const skill: ISkill = { id: 72778 };
      defaultSkillOrAtribute.skill = skill;

      const skillCollection: ISkill[] = [{ id: 46467 }];
      jest.spyOn(skillService, 'query').mockReturnValue(of(new HttpResponse({ body: skillCollection })));
      const additionalSkills = [skill];
      const expectedCollection: ISkill[] = [...additionalSkills, ...skillCollection];
      jest.spyOn(skillService, 'addSkillToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ defaultSkillOrAtribute });
      comp.ngOnInit();

      expect(skillService.query).toHaveBeenCalled();
      expect(skillService.addSkillToCollectionIfMissing).toHaveBeenCalledWith(skillCollection, ...additionalSkills);
      expect(comp.skillsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const defaultSkillOrAtribute: IDefaultSkillOrAtribute = { id: 456 };
      const skill: ISkill = { id: 14839 };
      defaultSkillOrAtribute.skill = skill;

      activatedRoute.data = of({ defaultSkillOrAtribute });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(defaultSkillOrAtribute));
      expect(comp.skillsSharedCollection).toContain(skill);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DefaultSkillOrAtribute>>();
      const defaultSkillOrAtribute = { id: 123 };
      jest.spyOn(defaultSkillOrAtributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ defaultSkillOrAtribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: defaultSkillOrAtribute }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(defaultSkillOrAtributeService.update).toHaveBeenCalledWith(defaultSkillOrAtribute);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DefaultSkillOrAtribute>>();
      const defaultSkillOrAtribute = new DefaultSkillOrAtribute();
      jest.spyOn(defaultSkillOrAtributeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ defaultSkillOrAtribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: defaultSkillOrAtribute }));
      saveSubject.complete();

      // THEN
      expect(defaultSkillOrAtributeService.create).toHaveBeenCalledWith(defaultSkillOrAtribute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DefaultSkillOrAtribute>>();
      const defaultSkillOrAtribute = { id: 123 };
      jest.spyOn(defaultSkillOrAtributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ defaultSkillOrAtribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(defaultSkillOrAtributeService.update).toHaveBeenCalledWith(defaultSkillOrAtribute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSkillById', () => {
      it('Should return tracked Skill primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSkillById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
