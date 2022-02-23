import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CharacterAttributeComponent } from './list/character-attribute.component';
import { CharacterAttributeDetailComponent } from './detail/character-attribute-detail.component';
import { CharacterAttributeUpdateComponent } from './update/character-attribute-update.component';
import { CharacterAttributeDeleteDialogComponent } from './delete/character-attribute-delete-dialog.component';
import { CharacterAttributeRoutingModule } from './route/character-attribute-routing.module';

@NgModule({
  imports: [SharedModule, CharacterAttributeRoutingModule],
  declarations: [
    CharacterAttributeComponent,
    CharacterAttributeDetailComponent,
    CharacterAttributeUpdateComponent,
    CharacterAttributeDeleteDialogComponent,
  ],
  entryComponents: [CharacterAttributeDeleteDialogComponent],
})
export class CharacterAttributeModule {}
