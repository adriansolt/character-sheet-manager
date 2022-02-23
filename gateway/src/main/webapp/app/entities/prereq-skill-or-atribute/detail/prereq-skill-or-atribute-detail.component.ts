import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrereqSkillOrAtribute } from '../prereq-skill-or-atribute.model';

@Component({
  selector: 'jhi-prereq-skill-or-atribute-detail',
  templateUrl: './prereq-skill-or-atribute-detail.component.html',
})
export class PrereqSkillOrAtributeDetailComponent implements OnInit {
  prereqSkillOrAtribute: IPrereqSkillOrAtribute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prereqSkillOrAtribute }) => {
      this.prereqSkillOrAtribute = prereqSkillOrAtribute;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
