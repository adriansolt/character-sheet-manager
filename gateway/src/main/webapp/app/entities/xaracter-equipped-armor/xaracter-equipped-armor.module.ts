import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { XaracterEquippedArmorComponent } from './list/xaracter-equipped-armor.component';
import { XaracterEquippedArmorDetailComponent } from './detail/xaracter-equipped-armor-detail.component';
import { XaracterEquippedArmorUpdateComponent } from './update/xaracter-equipped-armor-update.component';
import { XaracterEquippedArmorDeleteDialogComponent } from './delete/xaracter-equipped-armor-delete-dialog.component';
import { XaracterEquippedArmorRoutingModule } from './route/xaracter-equipped-armor-routing.module';

@NgModule({
  imports: [SharedModule, XaracterEquippedArmorRoutingModule],
  declarations: [
    XaracterEquippedArmorComponent,
    XaracterEquippedArmorDetailComponent,
    XaracterEquippedArmorUpdateComponent,
    XaracterEquippedArmorDeleteDialogComponent,
  ],
  entryComponents: [XaracterEquippedArmorDeleteDialogComponent],
})
export class XaracterEquippedArmorModule {}
