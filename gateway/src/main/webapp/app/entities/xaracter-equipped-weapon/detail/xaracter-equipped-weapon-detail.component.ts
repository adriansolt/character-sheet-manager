import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IXaracterEquippedWeapon } from '../xaracter-equipped-weapon.model';

@Component({
  selector: 'jhi-xaracter-equipped-weapon-detail',
  templateUrl: './xaracter-equipped-weapon-detail.component.html',
})
export class XaracterEquippedWeaponDetailComponent implements OnInit {
  xaracterEquippedWeapon: IXaracterEquippedWeapon | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracterEquippedWeapon }) => {
      this.xaracterEquippedWeapon = xaracterEquippedWeapon;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
