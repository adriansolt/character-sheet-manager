import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrereqSkillOrAtribute, getPrereqSkillOrAtributeIdentifier } from '../prereq-skill-or-atribute.model';

export type EntityResponseType = HttpResponse<IPrereqSkillOrAtribute>;
export type EntityArrayResponseType = HttpResponse<IPrereqSkillOrAtribute[]>;

@Injectable({ providedIn: 'root' })
export class PrereqSkillOrAtributeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prereq-skill-or-atributes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prereqSkillOrAtribute: IPrereqSkillOrAtribute): Observable<EntityResponseType> {
    return this.http.post<IPrereqSkillOrAtribute>(this.resourceUrl, prereqSkillOrAtribute, { observe: 'response' });
  }

  update(prereqSkillOrAtribute: IPrereqSkillOrAtribute): Observable<EntityResponseType> {
    return this.http.put<IPrereqSkillOrAtribute>(
      `${this.resourceUrl}/${getPrereqSkillOrAtributeIdentifier(prereqSkillOrAtribute) as number}`,
      prereqSkillOrAtribute,
      { observe: 'response' }
    );
  }

  partialUpdate(prereqSkillOrAtribute: IPrereqSkillOrAtribute): Observable<EntityResponseType> {
    return this.http.patch<IPrereqSkillOrAtribute>(
      `${this.resourceUrl}/${getPrereqSkillOrAtributeIdentifier(prereqSkillOrAtribute) as number}`,
      prereqSkillOrAtribute,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrereqSkillOrAtribute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrereqSkillOrAtribute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrereqSkillOrAtributeToCollectionIfMissing(
    prereqSkillOrAtributeCollection: IPrereqSkillOrAtribute[],
    ...prereqSkillOrAtributesToCheck: (IPrereqSkillOrAtribute | null | undefined)[]
  ): IPrereqSkillOrAtribute[] {
    const prereqSkillOrAtributes: IPrereqSkillOrAtribute[] = prereqSkillOrAtributesToCheck.filter(isPresent);
    if (prereqSkillOrAtributes.length > 0) {
      const prereqSkillOrAtributeCollectionIdentifiers = prereqSkillOrAtributeCollection.map(
        prereqSkillOrAtributeItem => getPrereqSkillOrAtributeIdentifier(prereqSkillOrAtributeItem)!
      );
      const prereqSkillOrAtributesToAdd = prereqSkillOrAtributes.filter(prereqSkillOrAtributeItem => {
        const prereqSkillOrAtributeIdentifier = getPrereqSkillOrAtributeIdentifier(prereqSkillOrAtributeItem);
        if (
          prereqSkillOrAtributeIdentifier == null ||
          prereqSkillOrAtributeCollectionIdentifiers.includes(prereqSkillOrAtributeIdentifier)
        ) {
          return false;
        }
        prereqSkillOrAtributeCollectionIdentifiers.push(prereqSkillOrAtributeIdentifier);
        return true;
      });
      return [...prereqSkillOrAtributesToAdd, ...prereqSkillOrAtributeCollection];
    }
    return prereqSkillOrAtributeCollection;
  }
}
