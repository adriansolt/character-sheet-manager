import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWeaponManeuver } from '../weapon-maneuver.model';
import { WeaponManeuverService } from '../service/weapon-maneuver.service';

@Component({
  templateUrl: './weapon-maneuver-delete-dialog.component.html',
})
export class WeaponManeuverDeleteDialogComponent {
  weaponManeuver?: IWeaponManeuver;

  constructor(protected weaponManeuverService: WeaponManeuverService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.weaponManeuverService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
