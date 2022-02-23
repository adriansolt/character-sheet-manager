import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICharacterEquippedWeapon, CharacterEquippedWeapon } from '../character-equipped-weapon.model';
import { CharacterEquippedWeaponService } from '../service/character-equipped-weapon.service';

@Injectable({ providedIn: 'root' })
export class CharacterEquippedWeaponRoutingResolveService implements Resolve<ICharacterEquippedWeapon> {
  constructor(protected service: CharacterEquippedWeaponService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICharacterEquippedWeapon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((characterEquippedWeapon: HttpResponse<CharacterEquippedWeapon>) => {
          if (characterEquippedWeapon.body) {
            return of(characterEquippedWeapon.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CharacterEquippedWeapon());
  }
}
