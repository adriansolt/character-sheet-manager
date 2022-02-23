import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWeapon, Weapon } from '../weapon.model';
import { WeaponService } from '../service/weapon.service';

@Injectable({ providedIn: 'root' })
export class WeaponRoutingResolveService implements Resolve<IWeapon> {
  constructor(protected service: WeaponService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWeapon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((weapon: HttpResponse<Weapon>) => {
          if (weapon.body) {
            return of(weapon.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Weapon());
  }
}
