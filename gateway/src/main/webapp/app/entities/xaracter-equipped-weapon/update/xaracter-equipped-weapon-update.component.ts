import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IXaracterEquippedWeapon, XaracterEquippedWeapon } from '../xaracter-equipped-weapon.model';
import { XaracterEquippedWeaponService } from '../service/xaracter-equipped-weapon.service';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { WeaponService } from 'app/entities/weapon/service/weapon.service';
import { Handedness } from 'app/entities/enumerations/handedness.model';

@Component({
  selector: 'jhi-xaracter-equipped-weapon-update',
  templateUrl: './xaracter-equipped-weapon-update.component.html',
})
export class XaracterEquippedWeaponUpdateComponent implements OnInit {
  isSaving = false;
  handednessValues = Object.keys(Handedness);

  weaponsSharedCollection: IWeapon[] = [];

  editForm = this.fb.group({
    id: [],
    xaracterId: [],
    hand: [],
    weaponId: [],
  });

  constructor(
    protected xaracterEquippedWeaponService: XaracterEquippedWeaponService,
    protected weaponService: WeaponService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracterEquippedWeapon }) => {
      this.updateForm(xaracterEquippedWeapon);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const xaracterEquippedWeapon = this.createFromForm();
    if (xaracterEquippedWeapon.id !== undefined) {
      this.subscribeToSaveResponse(this.xaracterEquippedWeaponService.update(xaracterEquippedWeapon));
    } else {
      this.subscribeToSaveResponse(this.xaracterEquippedWeaponService.create(xaracterEquippedWeapon));
    }
  }

  trackWeaponById(index: number, item: IWeapon): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IXaracterEquippedWeapon>>): void {
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

  protected updateForm(xaracterEquippedWeapon: IXaracterEquippedWeapon): void {
    this.editForm.patchValue({
      id: xaracterEquippedWeapon.id,
      xaracterId: xaracterEquippedWeapon.xaracterId,
      hand: xaracterEquippedWeapon.hand,
      weaponId: xaracterEquippedWeapon.weaponId,
    });

    this.weaponsSharedCollection = this.weaponService.addWeaponToCollectionIfMissing(
      this.weaponsSharedCollection,
      xaracterEquippedWeapon.weaponId
    );
  }

  protected loadRelationshipsOptions(): void {
    this.weaponService
      .query()
      .pipe(map((res: HttpResponse<IWeapon[]>) => res.body ?? []))
      .pipe(map((weapons: IWeapon[]) => this.weaponService.addWeaponToCollectionIfMissing(weapons, this.editForm.get('weaponId')!.value)))
      .subscribe((weapons: IWeapon[]) => (this.weaponsSharedCollection = weapons));
  }

  protected createFromForm(): IXaracterEquippedWeapon {
    return {
      ...new XaracterEquippedWeapon(),
      id: this.editForm.get(['id'])!.value,
      xaracterId: this.editForm.get(['xaracterId'])!.value,
      hand: this.editForm.get(['hand'])!.value,
      weaponId: this.editForm.get(['weaponId'])!.value,
    };
  }
}
