import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IXaracterSkill, getXaracterSkillIdentifier } from '../xaracter-skill.model';

export type EntityResponseType = HttpResponse<IXaracterSkill>;
export type EntityArrayResponseType = HttpResponse<IXaracterSkill[]>;

@Injectable({ providedIn: 'root' })
export class XaracterSkillService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/xaracter-skills');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(xaracterSkill: IXaracterSkill): Observable<EntityResponseType> {
    return this.http.post<IXaracterSkill>(this.resourceUrl, xaracterSkill, { observe: 'response' });
  }

  update(xaracterSkill: IXaracterSkill): Observable<EntityResponseType> {
    return this.http.put<IXaracterSkill>(`${this.resourceUrl}/${getXaracterSkillIdentifier(xaracterSkill) as number}`, xaracterSkill, {
      observe: 'response',
    });
  }

  partialUpdate(xaracterSkill: IXaracterSkill): Observable<EntityResponseType> {
    return this.http.patch<IXaracterSkill>(`${this.resourceUrl}/${getXaracterSkillIdentifier(xaracterSkill) as number}`, xaracterSkill, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IXaracterSkill>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IXaracterSkill[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addXaracterSkillToCollectionIfMissing(
    xaracterSkillCollection: IXaracterSkill[],
    ...xaracterSkillsToCheck: (IXaracterSkill | null | undefined)[]
  ): IXaracterSkill[] {
    const xaracterSkills: IXaracterSkill[] = xaracterSkillsToCheck.filter(isPresent);
    if (xaracterSkills.length > 0) {
      const xaracterSkillCollectionIdentifiers = xaracterSkillCollection.map(
        xaracterSkillItem => getXaracterSkillIdentifier(xaracterSkillItem)!
      );
      const xaracterSkillsToAdd = xaracterSkills.filter(xaracterSkillItem => {
        const xaracterSkillIdentifier = getXaracterSkillIdentifier(xaracterSkillItem);
        if (xaracterSkillIdentifier == null || xaracterSkillCollectionIdentifiers.includes(xaracterSkillIdentifier)) {
          return false;
        }
        xaracterSkillCollectionIdentifiers.push(xaracterSkillIdentifier);
        return true;
      });
      return [...xaracterSkillsToAdd, ...xaracterSkillCollection];
    }
    return xaracterSkillCollection;
  }
}
