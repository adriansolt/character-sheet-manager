import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDefaultSkillOrAtribute, getDefaultSkillOrAtributeIdentifier } from '../default-skill-or-atribute.model';

export type EntityResponseType = HttpResponse<IDefaultSkillOrAtribute>;
export type EntityArrayResponseType = HttpResponse<IDefaultSkillOrAtribute[]>;

@Injectable({ providedIn: 'root' })
export class DefaultSkillOrAtributeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/default-skill-or-atributes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(defaultSkillOrAtribute: IDefaultSkillOrAtribute): Observable<EntityResponseType> {
    return this.http.post<IDefaultSkillOrAtribute>(this.resourceUrl, defaultSkillOrAtribute, { observe: 'response' });
  }

  update(defaultSkillOrAtribute: IDefaultSkillOrAtribute): Observable<EntityResponseType> {
    return this.http.put<IDefaultSkillOrAtribute>(
      `${this.resourceUrl}/${getDefaultSkillOrAtributeIdentifier(defaultSkillOrAtribute) as number}`,
      defaultSkillOrAtribute,
      { observe: 'response' }
    );
  }

  partialUpdate(defaultSkillOrAtribute: IDefaultSkillOrAtribute): Observable<EntityResponseType> {
    return this.http.patch<IDefaultSkillOrAtribute>(
      `${this.resourceUrl}/${getDefaultSkillOrAtributeIdentifier(defaultSkillOrAtribute) as number}`,
      defaultSkillOrAtribute,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDefaultSkillOrAtribute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDefaultSkillOrAtribute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDefaultSkillOrAtributeToCollectionIfMissing(
    defaultSkillOrAtributeCollection: IDefaultSkillOrAtribute[],
    ...defaultSkillOrAtributesToCheck: (IDefaultSkillOrAtribute | null | undefined)[]
  ): IDefaultSkillOrAtribute[] {
    const defaultSkillOrAtributes: IDefaultSkillOrAtribute[] = defaultSkillOrAtributesToCheck.filter(isPresent);
    if (defaultSkillOrAtributes.length > 0) {
      const defaultSkillOrAtributeCollectionIdentifiers = defaultSkillOrAtributeCollection.map(
        defaultSkillOrAtributeItem => getDefaultSkillOrAtributeIdentifier(defaultSkillOrAtributeItem)!
      );
      const defaultSkillOrAtributesToAdd = defaultSkillOrAtributes.filter(defaultSkillOrAtributeItem => {
        const defaultSkillOrAtributeIdentifier = getDefaultSkillOrAtributeIdentifier(defaultSkillOrAtributeItem);
        if (
          defaultSkillOrAtributeIdentifier == null ||
          defaultSkillOrAtributeCollectionIdentifiers.includes(defaultSkillOrAtributeIdentifier)
        ) {
          return false;
        }
        defaultSkillOrAtributeCollectionIdentifiers.push(defaultSkillOrAtributeIdentifier);
        return true;
      });
      return [...defaultSkillOrAtributesToAdd, ...defaultSkillOrAtributeCollection];
    }
    return defaultSkillOrAtributeCollection;
  }
}
