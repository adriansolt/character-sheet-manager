import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDefaultSkillOrAtribute } from '../default-skill-or-atribute.model';

@Component({
  selector: 'jhi-default-skill-or-atribute-detail',
  templateUrl: './default-skill-or-atribute-detail.component.html',
})
export class DefaultSkillOrAtributeDetailComponent implements OnInit {
  defaultSkillOrAtribute: IDefaultSkillOrAtribute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ defaultSkillOrAtribute }) => {
      this.defaultSkillOrAtribute = defaultSkillOrAtribute;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
