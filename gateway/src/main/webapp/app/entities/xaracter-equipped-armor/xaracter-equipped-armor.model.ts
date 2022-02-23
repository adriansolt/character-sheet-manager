import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';

export interface IXaracterEquippedArmor {
  id?: number;
  xaracterId?: number | null;
  armorPiece?: IArmorPiece | null;
}

export class XaracterEquippedArmor implements IXaracterEquippedArmor {
  constructor(public id?: number, public xaracterId?: number | null, public armorPiece?: IArmorPiece | null) {}
}

export function getXaracterEquippedArmorIdentifier(xaracterEquippedArmor: IXaracterEquippedArmor): number | undefined {
  return xaracterEquippedArmor.id;
}
