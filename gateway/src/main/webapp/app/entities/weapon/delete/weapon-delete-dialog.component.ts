import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWeapon } from '../weapon.model';
import { WeaponService } from '../service/weapon.service';

@Component({
  templateUrl: './weapon-delete-dialog.component.html',
})
export class WeaponDeleteDialogComponent {
  weapon?: IWeapon;

  constructor(protected weaponService: WeaponService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.weaponService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
