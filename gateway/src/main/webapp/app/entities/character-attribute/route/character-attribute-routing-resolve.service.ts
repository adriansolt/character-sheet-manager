import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICharacterAttribute, CharacterAttribute } from '../character-attribute.model';
import { CharacterAttributeService } from '../service/character-attribute.service';

@Injectable({ providedIn: 'root' })
export class CharacterAttributeRoutingResolveService implements Resolve<ICharacterAttribute> {
  constructor(protected service: CharacterAttributeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICharacterAttribute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((characterAttribute: HttpResponse<CharacterAttribute>) => {
          if (characterAttribute.body) {
            return of(characterAttribute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CharacterAttribute());
  }
}
