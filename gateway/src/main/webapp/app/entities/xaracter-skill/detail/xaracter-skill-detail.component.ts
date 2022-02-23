import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IXaracterSkill } from '../xaracter-skill.model';

@Component({
  selector: 'jhi-xaracter-skill-detail',
  templateUrl: './xaracter-skill-detail.component.html',
})
export class XaracterSkillDetailComponent implements OnInit {
  xaracterSkill: IXaracterSkill | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracterSkill }) => {
      this.xaracterSkill = xaracterSkill;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
