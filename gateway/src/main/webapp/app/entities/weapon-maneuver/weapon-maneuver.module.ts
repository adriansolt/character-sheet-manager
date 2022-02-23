import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WeaponManeuverComponent } from './list/weapon-maneuver.component';
import { WeaponManeuverDetailComponent } from './detail/weapon-maneuver-detail.component';
import { WeaponManeuverUpdateComponent } from './update/weapon-maneuver-update.component';
import { WeaponManeuverDeleteDialogComponent } from './delete/weapon-maneuver-delete-dialog.component';
import { WeaponManeuverRoutingModule } from './route/weapon-maneuver-routing.module';

@NgModule({
  imports: [SharedModule, WeaponManeuverRoutingModule],
  declarations: [
    WeaponManeuverComponent,
    WeaponManeuverDetailComponent,
    WeaponManeuverUpdateComponent,
    WeaponManeuverDeleteDialogComponent,
  ],
  entryComponents: [WeaponManeuverDeleteDialogComponent],
})
export class WeaponManeuverModule {}
