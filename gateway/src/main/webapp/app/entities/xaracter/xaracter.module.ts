import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { XaracterComponent } from './list/xaracter.component';
import { XaracterDetailComponent } from './detail/xaracter-detail.component';
import { XaracterUpdateComponent } from './update/xaracter-update.component';
import { XaracterDeleteDialogComponent } from './delete/xaracter-delete-dialog.component';
import { XaracterRoutingModule } from './route/xaracter-routing.module';

@NgModule({
  imports: [SharedModule, XaracterRoutingModule],
  declarations: [XaracterComponent, XaracterDetailComponent, XaracterUpdateComponent, XaracterDeleteDialogComponent],
  entryComponents: [XaracterDeleteDialogComponent],
})
export class XaracterModule {}
