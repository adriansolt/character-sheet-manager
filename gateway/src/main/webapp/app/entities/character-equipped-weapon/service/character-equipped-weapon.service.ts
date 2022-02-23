import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICharacterEquippedWeapon, getCharacterEquippedWeaponIdentifier } from '../character-equipped-weapon.model';

export type EntityResponseType = HttpResponse<ICharacterEquippedWeapon>;
export type EntityArrayResponseType = HttpResponse<ICharacterEquippedWeapon[]>;

@Injectable({ providedIn: 'root' })
export class CharacterEquippedWeaponService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/character-equipped-weapons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(characterEquippedWeapon: ICharacterEquippedWeapon): Observable<EntityResponseType> {
    return this.http.post<ICharacterEquippedWeapon>(this.resourceUrl, characterEquippedWeapon, { observe: 'response' });
  }

  update(characterEquippedWeapon: ICharacterEquippedWeapon): Observable<EntityResponseType> {
    return this.http.put<ICharacterEquippedWeapon>(
      `${this.resourceUrl}/${getCharacterEquippedWeaponIdentifier(characterEquippedWeapon) as number}`,
      characterEquippedWeapon,
      { observe: 'response' }
    );
  }

  partialUpdate(characterEquippedWeapon: ICharacterEquippedWeapon): Observable<EntityResponseType> {
    return this.http.patch<ICharacterEquippedWeapon>(
      `${this.resourceUrl}/${getCharacterEquippedWeaponIdentifier(characterEquippedWeapon) as number}`,
      characterEquippedWeapon,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICharacterEquippedWeapon>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICharacterEquippedWeapon[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCharacterEquippedWeaponToCollectionIfMissing(
    characterEquippedWeaponCollection: ICharacterEquippedWeapon[],
    ...characterEquippedWeaponsToCheck: (ICharacterEquippedWeapon | null | undefined)[]
  ): ICharacterEquippedWeapon[] {
    const characterEquippedWeapons: ICharacterEquippedWeapon[] = characterEquippedWeaponsToCheck.filter(isPresent);
    if (characterEquippedWeapons.length > 0) {
      const characterEquippedWeaponCollectionIdentifiers = characterEquippedWeaponCollection.map(
        characterEquippedWeaponItem => getCharacterEquippedWeaponIdentifier(characterEquippedWeaponItem)!
      );
      const characterEquippedWeaponsToAdd = characterEquippedWeapons.filter(characterEquippedWeaponItem => {
        const characterEquippedWeaponIdentifier = getCharacterEquippedWeaponIdentifier(characterEquippedWeaponItem);
        if (
          characterEquippedWeaponIdentifier == null ||
          characterEquippedWeaponCollectionIdentifiers.includes(characterEquippedWeaponIdentifier)
        ) {
          return false;
        }
        characterEquippedWeaponCollectionIdentifiers.push(characterEquippedWeaponIdentifier);
        return true;
      });
      return [...characterEquippedWeaponsToAdd, ...characterEquippedWeaponCollection];
    }
    return characterEquippedWeaponCollection;
  }
}
