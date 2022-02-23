import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArmorPiece } from '../armor-piece.model';
import { ArmorPieceService } from '../service/armor-piece.service';

@Component({
  templateUrl: './armor-piece-delete-dialog.component.html',
})
export class ArmorPieceDeleteDialogComponent {
  armorPiece?: IArmorPiece;

  constructor(protected armorPieceService: ArmorPieceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.armorPieceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
