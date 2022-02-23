import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICharacterAttribute } from '../character-attribute.model';

@Component({
  selector: 'jhi-character-attribute-detail',
  templateUrl: './character-attribute-detail.component.html',
})
export class CharacterAttributeDetailComponent implements OnInit {
  characterAttribute: ICharacterAttribute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characterAttribute }) => {
      this.characterAttribute = characterAttribute;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
