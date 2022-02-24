import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArmorPiece } from '../armor-piece.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-armor-piece-detail',
  templateUrl: './armor-piece-detail.component.html',
})
export class ArmorPieceDetailComponent implements OnInit {
  armorPiece: IArmorPiece | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ armorPiece }) => {
      this.armorPiece = armorPiece;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
