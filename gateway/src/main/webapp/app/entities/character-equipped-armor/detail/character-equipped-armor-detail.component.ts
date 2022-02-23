import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICharacterEquippedArmor } from '../character-equipped-armor.model';

@Component({
  selector: 'jhi-character-equipped-armor-detail',
  templateUrl: './character-equipped-armor-detail.component.html',
})
export class CharacterEquippedArmorDetailComponent implements OnInit {
  characterEquippedArmor: ICharacterEquippedArmor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characterEquippedArmor }) => {
      this.characterEquippedArmor = characterEquippedArmor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
