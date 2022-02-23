import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { XaracterEquippedWeaponComponent } from './list/xaracter-equipped-weapon.component';
import { XaracterEquippedWeaponDetailComponent } from './detail/xaracter-equipped-weapon-detail.component';
import { XaracterEquippedWeaponUpdateComponent } from './update/xaracter-equipped-weapon-update.component';
import { XaracterEquippedWeaponDeleteDialogComponent } from './delete/xaracter-equipped-weapon-delete-dialog.component';
import { XaracterEquippedWeaponRoutingModule } from './route/xaracter-equipped-weapon-routing.module';

@NgModule({
  imports: [SharedModule, XaracterEquippedWeaponRoutingModule],
  declarations: [
    XaracterEquippedWeaponComponent,
    XaracterEquippedWeaponDetailComponent,
    XaracterEquippedWeaponUpdateComponent,
    XaracterEquippedWeaponDeleteDialogComponent,
  ],
  entryComponents: [XaracterEquippedWeaponDeleteDialogComponent],
})
export class XaracterEquippedWeaponModule {}
