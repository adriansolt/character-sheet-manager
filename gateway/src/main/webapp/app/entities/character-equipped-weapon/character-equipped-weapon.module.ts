import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CharacterEquippedWeaponComponent } from './list/character-equipped-weapon.component';
import { CharacterEquippedWeaponDetailComponent } from './detail/character-equipped-weapon-detail.component';
import { CharacterEquippedWeaponUpdateComponent } from './update/character-equipped-weapon-update.component';
import { CharacterEquippedWeaponDeleteDialogComponent } from './delete/character-equipped-weapon-delete-dialog.component';
import { CharacterEquippedWeaponRoutingModule } from './route/character-equipped-weapon-routing.module';

@NgModule({
  imports: [SharedModule, CharacterEquippedWeaponRoutingModule],
  declarations: [
    CharacterEquippedWeaponComponent,
    CharacterEquippedWeaponDetailComponent,
    CharacterEquippedWeaponUpdateComponent,
    CharacterEquippedWeaponDeleteDialogComponent,
  ],
  entryComponents: [CharacterEquippedWeaponDeleteDialogComponent],
})
export class CharacterEquippedWeaponModule {}
