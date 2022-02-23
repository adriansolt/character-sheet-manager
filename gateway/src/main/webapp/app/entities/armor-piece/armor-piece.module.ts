import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArmorPieceComponent } from './list/armor-piece.component';
import { ArmorPieceDetailComponent } from './detail/armor-piece-detail.component';
import { ArmorPieceUpdateComponent } from './update/armor-piece-update.component';
import { ArmorPieceDeleteDialogComponent } from './delete/armor-piece-delete-dialog.component';
import { ArmorPieceRoutingModule } from './route/armor-piece-routing.module';

@NgModule({
  imports: [SharedModule, ArmorPieceRoutingModule],
  declarations: [ArmorPieceComponent, ArmorPieceDetailComponent, ArmorPieceUpdateComponent, ArmorPieceDeleteDialogComponent],
  entryComponents: [ArmorPieceDeleteDialogComponent],
})
export class ArmorPieceModule {}
