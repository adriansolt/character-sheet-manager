import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWeapon, Weapon } from '../weapon.model';
import { WeaponService } from '../service/weapon.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';

@Component({
  selector: 'jhi-weapon-update',
  templateUrl: './weapon-update.component.html',
})
export class WeaponUpdateComponent implements OnInit {
  isSaving = false;

  charactersSharedCollection: ICharacter[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    weight: [null, [Validators.required]],
    quality: [null, [Validators.required]],
    picture: [],
    pictureContentType: [],
    reach: [null, [Validators.required]],
    baseDamage: [null, [Validators.required, Validators.min(0)]],
    requiredST: [null, [Validators.required, Validators.min(1)]],
    damageModifier: [],
    character: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected weaponService: WeaponService,
    protected characterService: CharacterService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weapon }) => {
      this.updateForm(weapon);

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
    const weapon = this.createFromForm();
    if (weapon.id !== undefined) {
      this.subscribeToSaveResponse(this.weaponService.update(weapon));
    } else {
      this.subscribeToSaveResponse(this.weaponService.create(weapon));
    }
  }

  trackCharacterById(index: number, item: ICharacter): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeapon>>): void {
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

  protected updateForm(weapon: IWeapon): void {
    this.editForm.patchValue({
      id: weapon.id,
      name: weapon.name,
      description: weapon.description,
      weight: weapon.weight,
      quality: weapon.quality,
      picture: weapon.picture,
      pictureContentType: weapon.pictureContentType,
      reach: weapon.reach,
      baseDamage: weapon.baseDamage,
      requiredST: weapon.requiredST,
      damageModifier: weapon.damageModifier,
      character: weapon.character,
    });

    this.charactersSharedCollection = this.characterService.addCharacterToCollectionIfMissing(
      this.charactersSharedCollection,
      weapon.character
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

  protected createFromForm(): IWeapon {
    return {
      ...new Weapon(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      quality: this.editForm.get(['quality'])!.value,
      pictureContentType: this.editForm.get(['pictureContentType'])!.value,
      picture: this.editForm.get(['picture'])!.value,
      reach: this.editForm.get(['reach'])!.value,
      baseDamage: this.editForm.get(['baseDamage'])!.value,
      requiredST: this.editForm.get(['requiredST'])!.value,
      damageModifier: this.editForm.get(['damageModifier'])!.value,
      character: this.editForm.get(['character'])!.value,
    };
  }
}
