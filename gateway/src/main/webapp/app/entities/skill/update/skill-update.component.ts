import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISkill, Skill } from '../skill.model';
import { SkillService } from '../service/skill.service';
import { SkillName } from 'app/entities/enumerations/skill-name.model';
import { Difficulty } from 'app/entities/enumerations/difficulty.model';

@Component({
  selector: 'jhi-skill-update',
  templateUrl: './skill-update.component.html',
})
export class SkillUpdateComponent implements OnInit {
  isSaving = false;
  skillNameValues = Object.keys(SkillName);
  difficultyValues = Object.keys(Difficulty);

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    difficulty: [null, [Validators.required]],
  });

  constructor(protected skillService: SkillService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ skill }) => {
      this.updateForm(skill);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const skill = this.createFromForm();
    if (skill.id !== undefined) {
      this.subscribeToSaveResponse(this.skillService.update(skill));
    } else {
      this.subscribeToSaveResponse(this.skillService.create(skill));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISkill>>): void {
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

  protected updateForm(skill: ISkill): void {
    this.editForm.patchValue({
      id: skill.id,
      name: skill.name,
      difficulty: skill.difficulty,
    });
  }

  protected createFromForm(): ISkill {
    return {
      ...new Skill(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      difficulty: this.editForm.get(['difficulty'])!.value,
    };
  }
}
