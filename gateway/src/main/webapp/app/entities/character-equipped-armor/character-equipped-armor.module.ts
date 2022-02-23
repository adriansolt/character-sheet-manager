import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CharacterEquippedArmorComponent } from './list/character-equipped-armor.component';
import { CharacterEquippedArmorDetailComponent } from './detail/character-equipped-armor-detail.component';
import { CharacterEquippedArmorUpdateComponent } from './update/character-equipped-armor-update.component';
import { CharacterEquippedArmorDeleteDialogComponent } from './delete/character-equipped-armor-delete-dialog.component';
import { CharacterEquippedArmorRoutingModule } from './route/character-equipped-armor-routing.module';

@NgModule({
  imports: [SharedModule, CharacterEquippedArmorRoutingModule],
  declarations: [
    CharacterEquippedArmorComponent,
    CharacterEquippedArmorDetailComponent,
    CharacterEquippedArmorUpdateComponent,
    CharacterEquippedArmorDeleteDialogComponent,
  ],
  entryComponents: [CharacterEquippedArmorDeleteDialogComponent],
})
export class CharacterEquippedArmorModule {}
