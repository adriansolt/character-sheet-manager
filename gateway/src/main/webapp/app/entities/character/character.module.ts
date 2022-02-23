import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CharacterComponent } from './list/character.component';
import { CharacterDetailComponent } from './detail/character-detail.component';
import { CharacterUpdateComponent } from './update/character-update.component';
import { CharacterDeleteDialogComponent } from './delete/character-delete-dialog.component';
import { CharacterRoutingModule } from './route/character-routing.module';

@NgModule({
  imports: [SharedModule, CharacterRoutingModule],
  declarations: [CharacterComponent, CharacterDetailComponent, CharacterUpdateComponent, CharacterDeleteDialogComponent],
  entryComponents: [CharacterDeleteDialogComponent],
})
export class CharacterModule {}
