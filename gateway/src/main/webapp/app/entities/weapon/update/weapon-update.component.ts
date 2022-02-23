import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWeapon, Weapon } from '../weapon.model';
import { WeaponService } from '../service/weapon.service';

@Component({
  selector: 'jhi-weapon-update',
  templateUrl: './weapon-update.component.html',
})
export class WeaponUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    reach: [null, [Validators.required]],
    baseDamage: [null, [Validators.required, Validators.min(0)]],
    requiredST: [null, [Validators.required, Validators.min(1)]],
    damageModifier: [],
  });

  constructor(protected weaponService: WeaponService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weapon }) => {
      this.updateForm(weapon);
    });
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
      reach: weapon.reach,
      baseDamage: weapon.baseDamage,
      requiredST: weapon.requiredST,
      damageModifier: weapon.damageModifier,
    });
  }

  protected createFromForm(): IWeapon {
    return {
      ...new Weapon(),
      id: this.editForm.get(['id'])!.value,
      reach: this.editForm.get(['reach'])!.value,
      baseDamage: this.editForm.get(['baseDamage'])!.value,
      requiredST: this.editForm.get(['requiredST'])!.value,
      damageModifier: this.editForm.get(['damageModifier'])!.value,
    };
  }
}
