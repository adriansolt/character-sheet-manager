import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IArmorPiece, ArmorPiece } from '../armor-piece.model';
import { ArmorPieceService } from '../service/armor-piece.service';
import { ArmorLocation } from 'app/entities/enumerations/armor-location.model';

@Component({
  selector: 'jhi-armor-piece-update',
  templateUrl: './armor-piece-update.component.html',
})
export class ArmorPieceUpdateComponent implements OnInit {
  isSaving = false;
  armorLocationValues = Object.keys(ArmorLocation);

  editForm = this.fb.group({
    id: [],
    location: [],
    defenseModifier: [],
  });

  constructor(protected armorPieceService: ArmorPieceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ armorPiece }) => {
      this.updateForm(armorPiece);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const armorPiece = this.createFromForm();
    if (armorPiece.id !== undefined) {
      this.subscribeToSaveResponse(this.armorPieceService.update(armorPiece));
    } else {
      this.subscribeToSaveResponse(this.armorPieceService.create(armorPiece));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArmorPiece>>): void {
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

  protected updateForm(armorPiece: IArmorPiece): void {
    this.editForm.patchValue({
      id: armorPiece.id,
      location: armorPiece.location,
      defenseModifier: armorPiece.defenseModifier,
    });
  }

  protected createFromForm(): IArmorPiece {
    return {
      ...new ArmorPiece(),
      id: this.editForm.get(['id'])!.value,
      location: this.editForm.get(['location'])!.value,
      defenseModifier: this.editForm.get(['defenseModifier'])!.value,
    };
  }
}
