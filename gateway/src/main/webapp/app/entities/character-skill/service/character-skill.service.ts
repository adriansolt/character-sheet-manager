import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICharacterSkill, getCharacterSkillIdentifier } from '../character-skill.model';

export type EntityResponseType = HttpResponse<ICharacterSkill>;
export type EntityArrayResponseType = HttpResponse<ICharacterSkill[]>;

@Injectable({ providedIn: 'root' })
export class CharacterSkillService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/character-skills');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(characterSkill: ICharacterSkill): Observable<EntityResponseType> {
    return this.http.post<ICharacterSkill>(this.resourceUrl, characterSkill, { observe: 'response' });
  }

  update(characterSkill: ICharacterSkill): Observable<EntityResponseType> {
    return this.http.put<ICharacterSkill>(`${this.resourceUrl}/${getCharacterSkillIdentifier(characterSkill) as number}`, characterSkill, {
      observe: 'response',
    });
  }

  partialUpdate(characterSkill: ICharacterSkill): Observable<EntityResponseType> {
    return this.http.patch<ICharacterSkill>(
      `${this.resourceUrl}/${getCharacterSkillIdentifier(characterSkill) as number}`,
      characterSkill,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICharacterSkill>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICharacterSkill[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCharacterSkillToCollectionIfMissing(
    characterSkillCollection: ICharacterSkill[],
    ...characterSkillsToCheck: (ICharacterSkill | null | undefined)[]
  ): ICharacterSkill[] {
    const characterSkills: ICharacterSkill[] = characterSkillsToCheck.filter(isPresent);
    if (characterSkills.length > 0) {
      const characterSkillCollectionIdentifiers = characterSkillCollection.map(
        characterSkillItem => getCharacterSkillIdentifier(characterSkillItem)!
      );
      const characterSkillsToAdd = characterSkills.filter(characterSkillItem => {
        const characterSkillIdentifier = getCharacterSkillIdentifier(characterSkillItem);
        if (characterSkillIdentifier == null || characterSkillCollectionIdentifiers.includes(characterSkillIdentifier)) {
          return false;
        }
        characterSkillCollectionIdentifiers.push(characterSkillIdentifier);
        return true;
      });
      return [...characterSkillsToAdd, ...characterSkillCollection];
    }
    return characterSkillCollection;
  }
}
