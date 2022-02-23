import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArmorPieceComponent } from '../list/armor-piece.component';
import { ArmorPieceDetailComponent } from '../detail/armor-piece-detail.component';
import { ArmorPieceUpdateComponent } from '../update/armor-piece-update.component';
import { ArmorPieceRoutingResolveService } from './armor-piece-routing-resolve.service';

const armorPieceRoute: Routes = [
  {
    path: '',
    component: ArmorPieceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArmorPieceDetailComponent,
    resolve: {
      armorPiece: ArmorPieceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArmorPieceUpdateComponent,
    resolve: {
      armorPiece: ArmorPieceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArmorPieceUpdateComponent,
    resolve: {
      armorPiece: ArmorPieceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(armorPieceRoute)],
  exports: [RouterModule],
})
export class ArmorPieceRoutingModule {}
