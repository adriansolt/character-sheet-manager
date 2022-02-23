import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArmorPiece, ArmorPiece } from '../armor-piece.model';
import { ArmorPieceService } from '../service/armor-piece.service';

@Injectable({ providedIn: 'root' })
export class ArmorPieceRoutingResolveService implements Resolve<IArmorPiece> {
  constructor(protected service: ArmorPieceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArmorPiece> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((armorPiece: HttpResponse<ArmorPiece>) => {
          if (armorPiece.body) {
            return of(armorPiece.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ArmorPiece());
  }
}
