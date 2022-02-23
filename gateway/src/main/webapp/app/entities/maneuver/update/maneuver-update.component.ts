import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IManeuver, Maneuver } from '../maneuver.model';
import { ManeuverService } from '../service/maneuver.service';

@Component({
  selector: 'jhi-maneuver-update',
  templateUrl: './maneuver-update.component.html',
})
export class ManeuverUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    modifier: [],
    description: [null, [Validators.required]],
  });

  constructor(protected maneuverService: ManeuverService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maneuver }) => {
      this.updateForm(maneuver);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maneuver = this.createFromForm();
    if (maneuver.id !== undefined) {
      this.subscribeToSaveResponse(this.maneuverService.update(maneuver));
    } else {
      this.subscribeToSaveResponse(this.maneuverService.create(maneuver));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManeuver>>): void {
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

  protected updateForm(maneuver: IManeuver): void {
    this.editForm.patchValue({
      id: maneuver.id,
      name: maneuver.name,
      modifier: maneuver.modifier,
      description: maneuver.description,
    });
  }

  protected createFromForm(): IManeuver {
    return {
      ...new Maneuver(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      modifier: this.editForm.get(['modifier'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
