import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICharacterEquippedArmor, CharacterEquippedArmor } from '../character-equipped-armor.model';
import { CharacterEquippedArmorService } from '../service/character-equipped-armor.service';
import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';
import { ArmorPieceService } from 'app/entities/armor-piece/service/armor-piece.service';

@Component({
  selector: 'jhi-character-equipped-armor-update',
  templateUrl: './character-equipped-armor-update.component.html',
})
export class CharacterEquippedArmorUpdateComponent implements OnInit {
  isSaving = false;

  armorPiecesSharedCollection: IArmorPiece[] = [];

  editForm = this.fb.group({
    id: [],
    armorPiece: [],
  });

  constructor(
    protected characterEquippedArmorService: CharacterEquippedArmorService,
    protected armorPieceService: ArmorPieceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characterEquippedArmor }) => {
      this.updateForm(characterEquippedArmor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const characterEquippedArmor = this.createFromForm();
    if (characterEquippedArmor.id !== undefined) {
      this.subscribeToSaveResponse(this.characterEquippedArmorService.update(characterEquippedArmor));
    } else {
      this.subscribeToSaveResponse(this.characterEquippedArmorService.create(characterEquippedArmor));
    }
  }

  trackArmorPieceById(index: number, item: IArmorPiece): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharacterEquippedArmor>>): void {
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

  protected updateForm(characterEquippedArmor: ICharacterEquippedArmor): void {
    this.editForm.patchValue({
      id: characterEquippedArmor.id,
      armorPiece: characterEquippedArmor.armorPiece,
    });

    this.armorPiecesSharedCollection = this.armorPieceService.addArmorPieceToCollectionIfMissing(
      this.armorPiecesSharedCollection,
      characterEquippedArmor.armorPiece
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

  protected createFromForm(): ICharacterEquippedArmor {
    return {
      ...new CharacterEquippedArmor(),
      id: this.editForm.get(['id'])!.value,
      armorPiece: this.editForm.get(['armorPiece'])!.value,
    };
  }
}
