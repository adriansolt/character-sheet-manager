import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICharacterEquippedArmor, CharacterEquippedArmor } from '../character-equipped-armor.model';
import { CharacterEquippedArmorService } from '../service/character-equipped-armor.service';

@Injectable({ providedIn: 'root' })
export class CharacterEquippedArmorRoutingResolveService implements Resolve<ICharacterEquippedArmor> {
  constructor(protected service: CharacterEquippedArmorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICharacterEquippedArmor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((characterEquippedArmor: HttpResponse<CharacterEquippedArmor>) => {
          if (characterEquippedArmor.body) {
            return of(characterEquippedArmor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CharacterEquippedArmor());
  }
}
