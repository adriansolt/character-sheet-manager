import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { XaracterAttributeComponent } from './list/xaracter-attribute.component';
import { XaracterAttributeDetailComponent } from './detail/xaracter-attribute-detail.component';
import { XaracterAttributeUpdateComponent } from './update/xaracter-attribute-update.component';
import { XaracterAttributeDeleteDialogComponent } from './delete/xaracter-attribute-delete-dialog.component';
import { XaracterAttributeRoutingModule } from './route/xaracter-attribute-routing.module';

@NgModule({
  imports: [SharedModule, XaracterAttributeRoutingModule],
  declarations: [
    XaracterAttributeComponent,
    XaracterAttributeDetailComponent,
    XaracterAttributeUpdateComponent,
    XaracterAttributeDeleteDialogComponent,
  ],
  entryComponents: [XaracterAttributeDeleteDialogComponent],
})
export class XaracterAttributeModule {}
