import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICharacterAttribute, getCharacterAttributeIdentifier } from '../character-attribute.model';

export type EntityResponseType = HttpResponse<ICharacterAttribute>;
export type EntityArrayResponseType = HttpResponse<ICharacterAttribute[]>;

@Injectable({ providedIn: 'root' })
export class CharacterAttributeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/character-attributes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(characterAttribute: ICharacterAttribute): Observable<EntityResponseType> {
    return this.http.post<ICharacterAttribute>(this.resourceUrl, characterAttribute, { observe: 'response' });
  }

  update(characterAttribute: ICharacterAttribute): Observable<EntityResponseType> {
    return this.http.put<ICharacterAttribute>(
      `${this.resourceUrl}/${getCharacterAttributeIdentifier(characterAttribute) as number}`,
      characterAttribute,
      { observe: 'response' }
    );
  }

  partialUpdate(characterAttribute: ICharacterAttribute): Observable<EntityResponseType> {
    return this.http.patch<ICharacterAttribute>(
      `${this.resourceUrl}/${getCharacterAttributeIdentifier(characterAttribute) as number}`,
      characterAttribute,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICharacterAttribute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICharacterAttribute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCharacterAttributeToCollectionIfMissing(
    characterAttributeCollection: ICharacterAttribute[],
    ...characterAttributesToCheck: (ICharacterAttribute | null | undefined)[]
  ): ICharacterAttribute[] {
    const characterAttributes: ICharacterAttribute[] = characterAttributesToCheck.filter(isPresent);
    if (characterAttributes.length > 0) {
      const characterAttributeCollectionIdentifiers = characterAttributeCollection.map(
        characterAttributeItem => getCharacterAttributeIdentifier(characterAttributeItem)!
      );
      const characterAttributesToAdd = characterAttributes.filter(characterAttributeItem => {
        const characterAttributeIdentifier = getCharacterAttributeIdentifier(characterAttributeItem);
        if (characterAttributeIdentifier == null || characterAttributeCollectionIdentifiers.includes(characterAttributeIdentifier)) {
          return false;
        }
        characterAttributeCollectionIdentifiers.push(characterAttributeIdentifier);
        return true;
      });
      return [...characterAttributesToAdd, ...characterAttributeCollection];
    }
    return characterAttributeCollection;
  }
}
