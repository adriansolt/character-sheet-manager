import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICharacterEquippedArmor, getCharacterEquippedArmorIdentifier } from '../character-equipped-armor.model';

export type EntityResponseType = HttpResponse<ICharacterEquippedArmor>;
export type EntityArrayResponseType = HttpResponse<ICharacterEquippedArmor[]>;

@Injectable({ providedIn: 'root' })
export class CharacterEquippedArmorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/character-equipped-armors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(characterEquippedArmor: ICharacterEquippedArmor): Observable<EntityResponseType> {
    return this.http.post<ICharacterEquippedArmor>(this.resourceUrl, characterEquippedArmor, { observe: 'response' });
  }

  update(characterEquippedArmor: ICharacterEquippedArmor): Observable<EntityResponseType> {
    return this.http.put<ICharacterEquippedArmor>(
      `${this.resourceUrl}/${getCharacterEquippedArmorIdentifier(characterEquippedArmor) as number}`,
      characterEquippedArmor,
      { observe: 'response' }
    );
  }

  partialUpdate(characterEquippedArmor: ICharacterEquippedArmor): Observable<EntityResponseType> {
    return this.http.patch<ICharacterEquippedArmor>(
      `${this.resourceUrl}/${getCharacterEquippedArmorIdentifier(characterEquippedArmor) as number}`,
      characterEquippedArmor,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICharacterEquippedArmor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICharacterEquippedArmor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCharacterEquippedArmorToCollectionIfMissing(
    characterEquippedArmorCollection: ICharacterEquippedArmor[],
    ...characterEquippedArmorsToCheck: (ICharacterEquippedArmor | null | undefined)[]
  ): ICharacterEquippedArmor[] {
    const characterEquippedArmors: ICharacterEquippedArmor[] = characterEquippedArmorsToCheck.filter(isPresent);
    if (characterEquippedArmors.length > 0) {
      const characterEquippedArmorCollectionIdentifiers = characterEquippedArmorCollection.map(
        characterEquippedArmorItem => getCharacterEquippedArmorIdentifier(characterEquippedArmorItem)!
      );
      const characterEquippedArmorsToAdd = characterEquippedArmors.filter(characterEquippedArmorItem => {
        const characterEquippedArmorIdentifier = getCharacterEquippedArmorIdentifier(characterEquippedArmorItem);
        if (
          characterEquippedArmorIdentifier == null ||
          characterEquippedArmorCollectionIdentifiers.includes(characterEquippedArmorIdentifier)
        ) {
          return false;
        }
        characterEquippedArmorCollectionIdentifiers.push(characterEquippedArmorIdentifier);
        return true;
      });
      return [...characterEquippedArmorsToAdd, ...characterEquippedArmorCollection];
    }
    return characterEquippedArmorCollection;
  }
}
