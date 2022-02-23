import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CharacterSkillComponent } from './list/character-skill.component';
import { CharacterSkillDetailComponent } from './detail/character-skill-detail.component';
import { CharacterSkillUpdateComponent } from './update/character-skill-update.component';
import { CharacterSkillDeleteDialogComponent } from './delete/character-skill-delete-dialog.component';
import { CharacterSkillRoutingModule } from './route/character-skill-routing.module';

@NgModule({
  imports: [SharedModule, CharacterSkillRoutingModule],
  declarations: [
    CharacterSkillComponent,
    CharacterSkillDetailComponent,
    CharacterSkillUpdateComponent,
    CharacterSkillDeleteDialogComponent,
  ],
  entryComponents: [CharacterSkillDeleteDialogComponent],
})
export class CharacterSkillModule {}
