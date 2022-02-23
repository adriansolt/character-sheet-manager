import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { XaracterSkillComponent } from './list/xaracter-skill.component';
import { XaracterSkillDetailComponent } from './detail/xaracter-skill-detail.component';
import { XaracterSkillUpdateComponent } from './update/xaracter-skill-update.component';
import { XaracterSkillDeleteDialogComponent } from './delete/xaracter-skill-delete-dialog.component';
import { XaracterSkillRoutingModule } from './route/xaracter-skill-routing.module';

@NgModule({
  imports: [SharedModule, XaracterSkillRoutingModule],
  declarations: [XaracterSkillComponent, XaracterSkillDetailComponent, XaracterSkillUpdateComponent, XaracterSkillDeleteDialogComponent],
  entryComponents: [XaracterSkillDeleteDialogComponent],
})
export class XaracterSkillModule {}
