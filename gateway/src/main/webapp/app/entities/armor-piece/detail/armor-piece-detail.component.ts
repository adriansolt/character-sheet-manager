import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArmorPiece } from '../armor-piece.model';

@Component({
  selector: 'jhi-armor-piece-detail',
  templateUrl: './armor-piece-detail.component.html',
})
export class ArmorPieceDetailComponent implements OnInit {
  armorPiece: IArmorPiece | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ armorPiece }) => {
      this.armorPiece = armorPiece;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
