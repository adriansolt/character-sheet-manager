import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ManeuverComponent } from './list/maneuver.component';
import { ManeuverDetailComponent } from './detail/maneuver-detail.component';
import { ManeuverUpdateComponent } from './update/maneuver-update.component';
import { ManeuverDeleteDialogComponent } from './delete/maneuver-delete-dialog.component';
import { ManeuverRoutingModule } from './route/maneuver-routing.module';

@NgModule({
  imports: [SharedModule, ManeuverRoutingModule],
  declarations: [ManeuverComponent, ManeuverDetailComponent, ManeuverUpdateComponent, ManeuverDeleteDialogComponent],
  entryComponents: [ManeuverDeleteDialogComponent],
})
export class ManeuverModule {}
