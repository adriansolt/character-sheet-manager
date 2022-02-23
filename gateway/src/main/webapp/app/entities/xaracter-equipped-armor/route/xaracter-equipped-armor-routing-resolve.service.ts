import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IXaracterEquippedArmor, XaracterEquippedArmor } from '../xaracter-equipped-armor.model';
import { XaracterEquippedArmorService } from '../service/xaracter-equipped-armor.service';

@Injectable({ providedIn: 'root' })
export class XaracterEquippedArmorRoutingResolveService implements Resolve<IXaracterEquippedArmor> {
  constructor(protected service: XaracterEquippedArmorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IXaracterEquippedArmor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((xaracterEquippedArmor: HttpResponse<XaracterEquippedArmor>) => {
          if (xaracterEquippedArmor.body) {
            return of(xaracterEquippedArmor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new XaracterEquippedArmor());
  }
}
