import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICharacterSkill } from '../character-skill.model';

@Component({
  selector: 'jhi-character-skill-detail',
  templateUrl: './character-skill-detail.component.html',
})
export class CharacterSkillDetailComponent implements OnInit {
  characterSkill: ICharacterSkill | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characterSkill }) => {
      this.characterSkill = characterSkill;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
