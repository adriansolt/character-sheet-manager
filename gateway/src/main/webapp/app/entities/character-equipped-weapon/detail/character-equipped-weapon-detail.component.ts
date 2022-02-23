import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICharacterEquippedWeapon } from '../character-equipped-weapon.model';

@Component({
  selector: 'jhi-character-equipped-weapon-detail',
  templateUrl: './character-equipped-weapon-detail.component.html',
})
export class CharacterEquippedWeaponDetailComponent implements OnInit {
  characterEquippedWeapon: ICharacterEquippedWeapon | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characterEquippedWeapon }) => {
      this.characterEquippedWeapon = characterEquippedWeapon;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
