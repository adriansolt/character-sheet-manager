import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPrereqSkillOrAtribute, PrereqSkillOrAtribute } from '../prereq-skill-or-atribute.model';
import { PrereqSkillOrAtributeService } from '../service/prereq-skill-or-atribute.service';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

@Component({
  selector: 'jhi-prereq-skill-or-atribute-update',
  templateUrl: './prereq-skill-or-atribute-update.component.html',
})
export class PrereqSkillOrAtributeUpdateComponent implements OnInit {
  isSaving = false;

  skillsSharedCollection: ISkill[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    level: [null, [Validators.required]],
    skill: [],
  });

  constructor(
    protected prereqSkillOrAtributeService: PrereqSkillOrAtributeService,
    protected skillService: SkillService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prereqSkillOrAtribute }) => {
      this.updateForm(prereqSkillOrAtribute);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prereqSkillOrAtribute = this.createFromForm();
    if (prereqSkillOrAtribute.id !== undefined) {
      this.subscribeToSaveResponse(this.prereqSkillOrAtributeService.update(prereqSkillOrAtribute));
    } else {
      this.subscribeToSaveResponse(this.prereqSkillOrAtributeService.create(prereqSkillOrAtribute));
    }
  }

  trackSkillById(index: number, item: ISkill): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrereqSkillOrAtribute>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(prereqSkillOrAtribute: IPrereqSkillOrAtribute): void {
    this.editForm.patchValue({
      id: prereqSkillOrAtribute.id,
      name: prereqSkillOrAtribute.name,
      level: prereqSkillOrAtribute.level,
      skill: prereqSkillOrAtribute.skill,
    });

    this.skillsSharedCollection = this.skillService.addSkillToCollectionIfMissing(this.skillsSharedCollection, prereqSkillOrAtribute.skill);
  }

  protected loadRelationshipsOptions(): void {
    this.skillService
      .query()
      .pipe(map((res: HttpResponse<ISkill[]>) => res.body ?? []))
      .pipe(map((skills: ISkill[]) => this.skillService.addSkillToCollectionIfMissing(skills, this.editForm.get('skill')!.value)))
      .subscribe((skills: ISkill[]) => (this.skillsSharedCollection = skills));
  }

  protected createFromForm(): IPrereqSkillOrAtribute {
    return {
      ...new PrereqSkillOrAtribute(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      level: this.editForm.get(['level'])!.value,
      skill: this.editForm.get(['skill'])!.value,
    };
  }
}
