import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';

export interface ICharacterEquippedArmor {
  id?: number;
  armorPiece?: IArmorPiece | null;
}

export class CharacterEquippedArmor implements ICharacterEquippedArmor {
  constructor(public id?: number, public armorPiece?: IArmorPiece | null) {}
}

export function getCharacterEquippedArmorIdentifier(characterEquippedArmor: ICharacterEquippedArmor): number | undefined {
  return characterEquippedArmor.id;
}
