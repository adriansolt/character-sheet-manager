import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IManeuver } from '../maneuver.model';
import { ManeuverService } from '../service/maneuver.service';

@Component({
  templateUrl: './maneuver-delete-dialog.component.html',
})
export class ManeuverDeleteDialogComponent {
  maneuver?: IManeuver;

  constructor(protected maneuverService: ManeuverService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.maneuverService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
