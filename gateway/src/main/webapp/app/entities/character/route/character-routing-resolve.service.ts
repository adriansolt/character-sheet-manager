import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICharacter, Character } from '../character.model';
import { CharacterService } from '../service/character.service';

@Injectable({ providedIn: 'root' })
export class CharacterRoutingResolveService implements Resolve<ICharacter> {
  constructor(protected service: CharacterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICharacter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((character: HttpResponse<Character>) => {
          if (character.body) {
            return of(character.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Character());
  }
}
