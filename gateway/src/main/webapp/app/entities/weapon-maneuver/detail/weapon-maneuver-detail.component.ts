import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWeaponManeuver } from '../weapon-maneuver.model';

@Component({
  selector: 'jhi-weapon-maneuver-detail',
  templateUrl: './weapon-maneuver-detail.component.html',
})
export class WeaponManeuverDetailComponent implements OnInit {
  weaponManeuver: IWeaponManeuver | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weaponManeuver }) => {
      this.weaponManeuver = weaponManeuver;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
