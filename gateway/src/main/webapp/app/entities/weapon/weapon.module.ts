import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WeaponComponent } from './list/weapon.component';
import { WeaponDetailComponent } from './detail/weapon-detail.component';
import { WeaponUpdateComponent } from './update/weapon-update.component';
import { WeaponDeleteDialogComponent } from './delete/weapon-delete-dialog.component';
import { WeaponRoutingModule } from './route/weapon-routing.module';

@NgModule({
  imports: [SharedModule, WeaponRoutingModule],
  declarations: [WeaponComponent, WeaponDetailComponent, WeaponUpdateComponent, WeaponDeleteDialogComponent],
  entryComponents: [WeaponDeleteDialogComponent],
})
export class WeaponModule {}
