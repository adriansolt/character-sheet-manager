import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArmorPiece, getArmorPieceIdentifier } from '../armor-piece.model';

export type EntityResponseType = HttpResponse<IArmorPiece>;
export type EntityArrayResponseType = HttpResponse<IArmorPiece[]>;

@Injectable({ providedIn: 'root' })
export class ArmorPieceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/armor-pieces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(armorPiece: IArmorPiece): Observable<EntityResponseType> {
    return this.http.post<IArmorPiece>(this.resourceUrl, armorPiece, { observe: 'response' });
  }

  update(armorPiece: IArmorPiece): Observable<EntityResponseType> {
    return this.http.put<IArmorPiece>(`${this.resourceUrl}/${getArmorPieceIdentifier(armorPiece) as number}`, armorPiece, {
      observe: 'response',
    });
  }

  partialUpdate(armorPiece: IArmorPiece): Observable<EntityResponseType> {
    return this.http.patch<IArmorPiece>(`${this.resourceUrl}/${getArmorPieceIdentifier(armorPiece) as number}`, armorPiece, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArmorPiece>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArmorPiece[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addArmorPieceToCollectionIfMissing(
    armorPieceCollection: IArmorPiece[],
    ...armorPiecesToCheck: (IArmorPiece | null | undefined)[]
  ): IArmorPiece[] {
    const armorPieces: IArmorPiece[] = armorPiecesToCheck.filter(isPresent);
    if (armorPieces.length > 0) {
      const armorPieceCollectionIdentifiers = armorPieceCollection.map(armorPieceItem => getArmorPieceIdentifier(armorPieceItem)!);
      const armorPiecesToAdd = armorPieces.filter(armorPieceItem => {
        const armorPieceIdentifier = getArmorPieceIdentifier(armorPieceItem);
        if (armorPieceIdentifier == null || armorPieceCollectionIdentifiers.includes(armorPieceIdentifier)) {
          return false;
        }
        armorPieceCollectionIdentifiers.push(armorPieceIdentifier);
        return true;
      });
      return [...armorPiecesToAdd, ...armorPieceCollection];
    }
    return armorPieceCollection;
  }
}
