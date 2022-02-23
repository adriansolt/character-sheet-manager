import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICharacter, getCharacterIdentifier } from '../character.model';

export type EntityResponseType = HttpResponse<ICharacter>;
export type EntityArrayResponseType = HttpResponse<ICharacter[]>;

@Injectable({ providedIn: 'root' })
export class CharacterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/characters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(character: ICharacter): Observable<EntityResponseType> {
    return this.http.post<ICharacter>(this.resourceUrl, character, { observe: 'response' });
  }

  update(character: ICharacter): Observable<EntityResponseType> {
    return this.http.put<ICharacter>(`${this.resourceUrl}/${getCharacterIdentifier(character) as number}`, character, {
      observe: 'response',
    });
  }

  partialUpdate(character: ICharacter): Observable<EntityResponseType> {
    return this.http.patch<ICharacter>(`${this.resourceUrl}/${getCharacterIdentifier(character) as number}`, character, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICharacter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICharacter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCharacterToCollectionIfMissing(
    characterCollection: ICharacter[],
    ...charactersToCheck: (ICharacter | null | undefined)[]
  ): ICharacter[] {
    const characters: ICharacter[] = charactersToCheck.filter(isPresent);
    if (characters.length > 0) {
      const characterCollectionIdentifiers = characterCollection.map(characterItem => getCharacterIdentifier(characterItem)!);
      const charactersToAdd = characters.filter(characterItem => {
        const characterIdentifier = getCharacterIdentifier(characterItem);
        if (characterIdentifier == null || characterCollectionIdentifiers.includes(characterIdentifier)) {
          return false;
        }
        characterCollectionIdentifiers.push(characterIdentifier);
        return true;
      });
      return [...charactersToAdd, ...characterCollection];
    }
    return characterCollection;
  }
}
