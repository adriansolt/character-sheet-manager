import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWeaponManeuver, WeaponManeuver } from '../weapon-maneuver.model';
import { WeaponManeuverService } from '../service/weapon-maneuver.service';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { WeaponService } from 'app/entities/weapon/service/weapon.service';
import { IManeuver } from 'app/entities/maneuver/maneuver.model';
import { ManeuverService } from 'app/entities/maneuver/service/maneuver.service';

@Component({
  selector: 'jhi-weapon-maneuver-update',
  templateUrl: './weapon-maneuver-update.component.html',
})
export class WeaponManeuverUpdateComponent implements OnInit {
  isSaving = false;

  weaponsSharedCollection: IWeapon[] = [];
  maneuversSharedCollection: IManeuver[] = [];

  editForm = this.fb.group({
    id: [],
    weapon: [],
    maneuver: [],
  });

  constructor(
    protected weaponManeuverService: WeaponManeuverService,
    protected weaponService: WeaponService,
    protected maneuverService: ManeuverService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weaponManeuver }) => {
      this.updateForm(weaponManeuver);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const weaponManeuver = this.createFromForm();
    if (weaponManeuver.id !== undefined) {
      this.subscribeToSaveResponse(this.weaponManeuverService.update(weaponManeuver));
    } else {
      this.subscribeToSaveResponse(this.weaponManeuverService.create(weaponManeuver));
    }
  }

  trackWeaponById(index: number, item: IWeapon): number {
    return item.id!;
  }

  trackManeuverById(index: number, item: IManeuver): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeaponManeuver>>): void {
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

  protected updateForm(weaponManeuver: IWeaponManeuver): void {
    this.editForm.patchValue({
      id: weaponManeuver.id,
      weapon: weaponManeuver.weapon,
      maneuver: weaponManeuver.maneuver,
    });

    this.weaponsSharedCollection = this.weaponService.addWeaponToCollectionIfMissing(this.weaponsSharedCollection, weaponManeuver.weapon);
    this.maneuversSharedCollection = this.maneuverService.addManeuverToCollectionIfMissing(
      this.maneuversSharedCollection,
      weaponManeuver.maneuver
    );
  }

  protected loadRelationshipsOptions(): void {
    this.weaponService
      .query()
      .pipe(map((res: HttpResponse<IWeapon[]>) => res.body ?? []))
      .pipe(map((weapons: IWeapon[]) => this.weaponService.addWeaponToCollectionIfMissing(weapons, this.editForm.get('weapon')!.value)))
      .subscribe((weapons: IWeapon[]) => (this.weaponsSharedCollection = weapons));

    this.maneuverService
      .query()
      .pipe(map((res: HttpResponse<IManeuver[]>) => res.body ?? []))
      .pipe(
        map((maneuvers: IManeuver[]) =>
          this.maneuverService.addManeuverToCollectionIfMissing(maneuvers, this.editForm.get('maneuver')!.value)
        )
      )
      .subscribe((maneuvers: IManeuver[]) => (this.maneuversSharedCollection = maneuvers));
  }

  protected createFromForm(): IWeaponManeuver {
    return {
      ...new WeaponManeuver(),
      id: this.editForm.get(['id'])!.value,
      weapon: this.editForm.get(['weapon'])!.value,
      maneuver: this.editForm.get(['maneuver'])!.value,
    };
  }
}
