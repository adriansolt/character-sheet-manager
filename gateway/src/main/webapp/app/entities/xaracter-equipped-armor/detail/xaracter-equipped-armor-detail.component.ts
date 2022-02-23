import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IXaracterEquippedArmor } from '../xaracter-equipped-armor.model';

@Component({
  selector: 'jhi-xaracter-equipped-armor-detail',
  templateUrl: './xaracter-equipped-armor-detail.component.html',
})
export class XaracterEquippedArmorDetailComponent implements OnInit {
  xaracterEquippedArmor: IXaracterEquippedArmor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracterEquippedArmor }) => {
      this.xaracterEquippedArmor = xaracterEquippedArmor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
