import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IXaracterSkill, XaracterSkill } from '../xaracter-skill.model';
import { XaracterSkillService } from '../service/xaracter-skill.service';
import { IXaracter } from 'app/entities/xaracter/xaracter.model';
import { XaracterService } from 'app/entities/xaracter/service/xaracter.service';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

@Component({
  selector: 'jhi-xaracter-skill-update',
  templateUrl: './xaracter-skill-update.component.html',
})
export class XaracterSkillUpdateComponent implements OnInit {
  isSaving = false;

  xaractersSharedCollection: IXaracter[] = [];
  skillsSharedCollection: ISkill[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    skillModifier: [],
    xaracterId: [],
    skillId: [],
  });

  constructor(
    protected xaracterSkillService: XaracterSkillService,
    protected xaracterService: XaracterService,
    protected skillService: SkillService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracterSkill }) => {
      this.updateForm(xaracterSkill);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const xaracterSkill = this.createFromForm();
    if (xaracterSkill.id !== undefined) {
      this.subscribeToSaveResponse(this.xaracterSkillService.update(xaracterSkill));
    } else {
      this.subscribeToSaveResponse(this.xaracterSkillService.create(xaracterSkill));
    }
  }

  trackXaracterById(index: number, item: IXaracter): number {
    return item.id!;
  }

  trackSkillById(index: number, item: ISkill): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IXaracterSkill>>): void {
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

  protected updateForm(xaracterSkill: IXaracterSkill): void {
    this.editForm.patchValue({
      id: xaracterSkill.id,
      points: xaracterSkill.points,
      skillModifier: xaracterSkill.skillModifier,
      xaracterId: xaracterSkill.xaracterId,
      skillId: xaracterSkill.skillId,
    });

    this.xaractersSharedCollection = this.xaracterService.addXaracterToCollectionIfMissing(
      this.xaractersSharedCollection,
      xaracterSkill.xaracterId
    );
    this.skillsSharedCollection = this.skillService.addSkillToCollectionIfMissing(this.skillsSharedCollection, xaracterSkill.skillId);
  }

  protected loadRelationshipsOptions(): void {
    this.xaracterService
      .query()
      .pipe(map((res: HttpResponse<IXaracter[]>) => res.body ?? []))
      .pipe(
        map((xaracters: IXaracter[]) =>
          this.xaracterService.addXaracterToCollectionIfMissing(xaracters, this.editForm.get('xaracterId')!.value)
        )
      )
      .subscribe((xaracters: IXaracter[]) => (this.xaractersSharedCollection = xaracters));

    this.skillService
      .query()
      .pipe(map((res: HttpResponse<ISkill[]>) => res.body ?? []))
      .pipe(map((skills: ISkill[]) => this.skillService.addSkillToCollectionIfMissing(skills, this.editForm.get('skillId')!.value)))
      .subscribe((skills: ISkill[]) => (this.skillsSharedCollection = skills));
  }

  protected createFromForm(): IXaracterSkill {
    return {
      ...new XaracterSkill(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      skillModifier: this.editForm.get(['skillModifier'])!.value,
      xaracterId: this.editForm.get(['xaracterId'])!.value,
      skillId: this.editForm.get(['skillId'])!.value,
    };
  }
}
