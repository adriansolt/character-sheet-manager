import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IXaracterEquippedWeapon, XaracterEquippedWeapon } from '../xaracter-equipped-weapon.model';
import { XaracterEquippedWeaponService } from '../service/xaracter-equipped-weapon.service';

@Injectable({ providedIn: 'root' })
export class XaracterEquippedWeaponRoutingResolveService implements Resolve<IXaracterEquippedWeapon> {
  constructor(protected service: XaracterEquippedWeaponService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IXaracterEquippedWeapon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((xaracterEquippedWeapon: HttpResponse<XaracterEquippedWeapon>) => {
          if (xaracterEquippedWeapon.body) {
            return of(xaracterEquippedWeapon.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new XaracterEquippedWeapon());
  }
}
