import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IXaracterEquippedArmor } from '../xaracter-equipped-armor.model';
import { XaracterEquippedArmorService } from '../service/xaracter-equipped-armor.service';

@Component({
  templateUrl: './xaracter-equipped-armor-delete-dialog.component.html',
})
export class XaracterEquippedArmorDeleteDialogComponent {
  xaracterEquippedArmor?: IXaracterEquippedArmor;

  constructor(protected xaracterEquippedArmorService: XaracterEquippedArmorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.xaracterEquippedArmorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
