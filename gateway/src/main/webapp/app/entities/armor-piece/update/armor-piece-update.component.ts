import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IArmorPiece, ArmorPiece } from '../armor-piece.model';
import { ArmorPieceService } from '../service/armor-piece.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';
import { ArmorLocation } from 'app/entities/enumerations/armor-location.model';

@Component({
  selector: 'jhi-armor-piece-update',
  templateUrl: './armor-piece-update.component.html',
})
export class ArmorPieceUpdateComponent implements OnInit {
  isSaving = false;
  armorLocationValues = Object.keys(ArmorLocation);

  charactersSharedCollection: ICharacter[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    weight: [null, [Validators.required]],
    quality: [null, [Validators.required]],
    picture: [],
    pictureContentType: [],
    location: [],
    defenseModifier: [],
    character: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected armorPieceService: ArmorPieceService,
    protected characterService: CharacterService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ armorPiece }) => {
      this.updateForm(armorPiece);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
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

  trackCharacterById(index: number, item: ICharacter): number {
    return item.id!;
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
      name: armorPiece.name,
      description: armorPiece.description,
      weight: armorPiece.weight,
      quality: armorPiece.quality,
      picture: armorPiece.picture,
      pictureContentType: armorPiece.pictureContentType,
      location: armorPiece.location,
      defenseModifier: armorPiece.defenseModifier,
      character: armorPiece.character,
    });

    this.charactersSharedCollection = this.characterService.addCharacterToCollectionIfMissing(
      this.charactersSharedCollection,
      armorPiece.character
    );
  }

  protected loadRelationshipsOptions(): void {
    this.characterService
      .query()
      .pipe(map((res: HttpResponse<ICharacter[]>) => res.body ?? []))
      .pipe(
        map((characters: ICharacter[]) =>
          this.characterService.addCharacterToCollectionIfMissing(characters, this.editForm.get('character')!.value)
        )
      )
      .subscribe((characters: ICharacter[]) => (this.charactersSharedCollection = characters));
  }

  protected createFromForm(): IArmorPiece {
    return {
      ...new ArmorPiece(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      quality: this.editForm.get(['quality'])!.value,
      pictureContentType: this.editForm.get(['pictureContentType'])!.value,
      picture: this.editForm.get(['picture'])!.value,
      location: this.editForm.get(['location'])!.value,
      defenseModifier: this.editForm.get(['defenseModifier'])!.value,
      character: this.editForm.get(['character'])!.value,
    };
  }
}
