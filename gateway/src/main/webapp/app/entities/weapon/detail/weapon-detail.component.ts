import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWeapon } from '../weapon.model';

@Component({
  selector: 'jhi-weapon-detail',
  templateUrl: './weapon-detail.component.html',
})
export class WeaponDetailComponent implements OnInit {
  weapon: IWeapon | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weapon }) => {
      this.weapon = weapon;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
