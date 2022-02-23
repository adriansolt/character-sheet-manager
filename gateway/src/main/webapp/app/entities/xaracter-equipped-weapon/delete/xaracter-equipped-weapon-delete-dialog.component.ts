import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IXaracterEquippedWeapon } from '../xaracter-equipped-weapon.model';
import { XaracterEquippedWeaponService } from '../service/xaracter-equipped-weapon.service';

@Component({
  templateUrl: './xaracter-equipped-weapon-delete-dialog.component.html',
})
export class XaracterEquippedWeaponDeleteDialogComponent {
  xaracterEquippedWeapon?: IXaracterEquippedWeapon;

  constructor(protected xaracterEquippedWeaponService: XaracterEquippedWeaponService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.xaracterEquippedWeaponService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
