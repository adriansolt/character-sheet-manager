import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IXaracterEquippedArmor, XaracterEquippedArmor } from '../xaracter-equipped-armor.model';
import { XaracterEquippedArmorService } from '../service/xaracter-equipped-armor.service';
import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';
import { ArmorPieceService } from 'app/entities/armor-piece/service/armor-piece.service';

@Component({
  selector: 'jhi-xaracter-equipped-armor-update',
  templateUrl: './xaracter-equipped-armor-update.component.html',
})
export class XaracterEquippedArmorUpdateComponent implements OnInit {
  isSaving = false;

  armorPiecesSharedCollection: IArmorPiece[] = [];

  editForm = this.fb.group({
    id: [],
    xaracterId: [],
    armorPiece: [],
  });

  constructor(
    protected xaracterEquippedArmorService: XaracterEquippedArmorService,
    protected armorPieceService: ArmorPieceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracterEquippedArmor }) => {
      this.updateForm(xaracterEquippedArmor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const xaracterEquippedArmor = this.createFromForm();
    if (xaracterEquippedArmor.id !== undefined) {
      this.subscribeToSaveResponse(this.xaracterEquippedArmorService.update(xaracterEquippedArmor));
    } else {
      this.subscribeToSaveResponse(this.xaracterEquippedArmorService.create(xaracterEquippedArmor));
    }
  }

  trackArmorPieceById(index: number, item: IArmorPiece): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IXaracterEquippedArmor>>): void {
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

  protected updateForm(xaracterEquippedArmor: IXaracterEquippedArmor): void {
    this.editForm.patchValue({
      id: xaracterEquippedArmor.id,
      xaracterId: xaracterEquippedArmor.xaracterId,
      armorPiece: xaracterEquippedArmor.armorPiece,
    });

    this.armorPiecesSharedCollection = this.armorPieceService.addArmorPieceToCollectionIfMissing(
      this.armorPiecesSharedCollection,
      xaracterEquippedArmor.armorPiece
    );
  }

  protected loadRelationshipsOptions(): void {
    this.armorPieceService
      .query()
      .pipe(map((res: HttpResponse<IArmorPiece[]>) => res.body ?? []))
      .pipe(
        map((armorPieces: IArmorPiece[]) =>
          this.armorPieceService.addArmorPieceToCollectionIfMissing(armorPieces, this.editForm.get('armorPiece')!.value)
        )
      )
      .subscribe((armorPieces: IArmorPiece[]) => (this.armorPiecesSharedCollection = armorPieces));
  }

  protected createFromForm(): IXaracterEquippedArmor {
    return {
      ...new XaracterEquippedArmor(),
      id: this.editForm.get(['id'])!.value,
      xaracterId: this.editForm.get(['xaracterId'])!.value,
      armorPiece: this.editForm.get(['armorPiece'])!.value,
    };
  }
}
