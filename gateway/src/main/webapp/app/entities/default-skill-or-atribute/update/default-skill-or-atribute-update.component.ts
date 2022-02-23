import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDefaultSkillOrAtribute, DefaultSkillOrAtribute } from '../default-skill-or-atribute.model';
import { DefaultSkillOrAtributeService } from '../service/default-skill-or-atribute.service';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

@Component({
  selector: 'jhi-default-skill-or-atribute-update',
  templateUrl: './default-skill-or-atribute-update.component.html',
})
export class DefaultSkillOrAtributeUpdateComponent implements OnInit {
  isSaving = false;

  skillsSharedCollection: ISkill[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    modifier: [null, [Validators.required]],
    skill: [],
  });

  constructor(
    protected defaultSkillOrAtributeService: DefaultSkillOrAtributeService,
    protected skillService: SkillService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ defaultSkillOrAtribute }) => {
      this.updateForm(defaultSkillOrAtribute);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const defaultSkillOrAtribute = this.createFromForm();
    if (defaultSkillOrAtribute.id !== undefined) {
      this.subscribeToSaveResponse(this.defaultSkillOrAtributeService.update(defaultSkillOrAtribute));
    } else {
      this.subscribeToSaveResponse(this.defaultSkillOrAtributeService.create(defaultSkillOrAtribute));
    }
  }

  trackSkillById(index: number, item: ISkill): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDefaultSkillOrAtribute>>): void {
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

  protected updateForm(defaultSkillOrAtribute: IDefaultSkillOrAtribute): void {
    this.editForm.patchValue({
      id: defaultSkillOrAtribute.id,
      name: defaultSkillOrAtribute.name,
      modifier: defaultSkillOrAtribute.modifier,
      skill: defaultSkillOrAtribute.skill,
    });

    this.skillsSharedCollection = this.skillService.addSkillToCollectionIfMissing(
      this.skillsSharedCollection,
      defaultSkillOrAtribute.skill
    );
  }

  protected loadRelationshipsOptions(): void {
    this.skillService
      .query()
      .pipe(map((res: HttpResponse<ISkill[]>) => res.body ?? []))
      .pipe(map((skills: ISkill[]) => this.skillService.addSkillToCollectionIfMissing(skills, this.editForm.get('skill')!.value)))
      .subscribe((skills: ISkill[]) => (this.skillsSharedCollection = skills));
  }

  protected createFromForm(): IDefaultSkillOrAtribute {
    return {
      ...new DefaultSkillOrAtribute(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      modifier: this.editForm.get(['modifier'])!.value,
      skill: this.editForm.get(['skill'])!.value,
    };
  }
}
