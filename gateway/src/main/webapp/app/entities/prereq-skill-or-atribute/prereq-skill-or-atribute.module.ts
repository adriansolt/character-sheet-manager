import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrereqSkillOrAtributeComponent } from './list/prereq-skill-or-atribute.component';
import { PrereqSkillOrAtributeDetailComponent } from './detail/prereq-skill-or-atribute-detail.component';
import { PrereqSkillOrAtributeUpdateComponent } from './update/prereq-skill-or-atribute-update.component';
import { PrereqSkillOrAtributeDeleteDialogComponent } from './delete/prereq-skill-or-atribute-delete-dialog.component';
import { PrereqSkillOrAtributeRoutingModule } from './route/prereq-skill-or-atribute-routing.module';

@NgModule({
  imports: [SharedModule, PrereqSkillOrAtributeRoutingModule],
  declarations: [
    PrereqSkillOrAtributeComponent,
    PrereqSkillOrAtributeDetailComponent,
    PrereqSkillOrAtributeUpdateComponent,
    PrereqSkillOrAtributeDeleteDialogComponent,
  ],
  entryComponents: [PrereqSkillOrAtributeDeleteDialogComponent],
})
export class PrereqSkillOrAtributeModule {}
