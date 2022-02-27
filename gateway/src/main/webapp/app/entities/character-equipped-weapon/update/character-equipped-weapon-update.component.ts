import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICharacterEquippedWeapon, CharacterEquippedWeapon } from '../character-equipped-weapon.model';
import { CharacterEquippedWeaponService } from '../service/character-equipped-weapon.service';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { WeaponService } from 'app/entities/weapon/service/weapon.service';
import { Handedness } from 'app/entities/enumerations/handedness.model';

@Component({
  selector: 'jhi-character-equipped-weapon-update',
  templateUrl: './character-equipped-weapon-update.component.html',
})
export class CharacterEquippedWeaponUpdateComponent implements OnInit {
  isSaving = false;
  handednessValues = Object.keys(Handedness);

  weaponsSharedCollection: IWeapon[] = [];

  editForm = this.fb.group({
    id: [],
    hand: [],
    weapon: [],
  });

  constructor(
    protected characterEquippedWeaponService: CharacterEquippedWeaponService,
    protected weaponService: WeaponService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characterEquippedWeapon }) => {
      this.updateForm(characterEquippedWeapon);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const characterEquippedWeapon = this.createFromForm();
    if (characterEquippedWeapon.id !== undefined) {
      this.subscribeToSaveResponse(this.characterEquippedWeaponService.update(characterEquippedWeapon));
    } else {
      this.subscribeToSaveResponse(this.characterEquippedWeaponService.create(characterEquippedWeapon));
    }
  }

  trackWeaponById(index: number, item: IWeapon): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharacterEquippedWeapon>>): void {
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

  protected updateForm(characterEquippedWeapon: ICharacterEquippedWeapon): void {
    this.editForm.patchValue({
      id: characterEquippedWeapon.id,
      hand: characterEquippedWeapon.hand,
      weapon: characterEquippedWeapon.weapon,
    });

    this.weaponsSharedCollection = this.weaponService.addWeaponToCollectionIfMissing(
      this.weaponsSharedCollection,
      characterEquippedWeapon.weapon
    );
  }

  protected loadRelationshipsOptions(): void {
    this.weaponService
      .query()
      .pipe(map((res: HttpResponse<IWeapon[]>) => res.body ?? []))
      .pipe(map((weapons: IWeapon[]) => this.weaponService.addWeaponToCollectionIfMissing(weapons, this.editForm.get('weapon')!.value)))
      .subscribe((weapons: IWeapon[]) => (this.weaponsSharedCollection = weapons));
  }

  protected createFromForm(): ICharacterEquippedWeapon {
    return {
      ...new CharacterEquippedWeapon(),
      id: this.editForm.get(['id'])!.value,
      hand: this.editForm.get(['hand'])!.value,
      weapon: this.editForm.get(['weapon'])!.value,
    };
  }
}
