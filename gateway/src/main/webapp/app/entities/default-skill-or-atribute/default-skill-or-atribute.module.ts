import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DefaultSkillOrAtributeComponent } from './list/default-skill-or-atribute.component';
import { DefaultSkillOrAtributeDetailComponent } from './detail/default-skill-or-atribute-detail.component';
import { DefaultSkillOrAtributeUpdateComponent } from './update/default-skill-or-atribute-update.component';
import { DefaultSkillOrAtributeDeleteDialogComponent } from './delete/default-skill-or-atribute-delete-dialog.component';
import { DefaultSkillOrAtributeRoutingModule } from './route/default-skill-or-atribute-routing.module';

@NgModule({
  imports: [SharedModule, DefaultSkillOrAtributeRoutingModule],
  declarations: [
    DefaultSkillOrAtributeComponent,
    DefaultSkillOrAtributeDetailComponent,
    DefaultSkillOrAtributeUpdateComponent,
    DefaultSkillOrAtributeDeleteDialogComponent,
  ],
  entryComponents: [DefaultSkillOrAtributeDeleteDialogComponent],
})
export class DefaultSkillOrAtributeModule {}
