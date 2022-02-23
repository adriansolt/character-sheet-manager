import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IXaracterAttribute } from '../xaracter-attribute.model';

@Component({
  selector: 'jhi-xaracter-attribute-detail',
  templateUrl: './xaracter-attribute-detail.component.html',
})
export class XaracterAttributeDetailComponent implements OnInit {
  xaracterAttribute: IXaracterAttribute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracterAttribute }) => {
      this.xaracterAttribute = xaracterAttribute;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
