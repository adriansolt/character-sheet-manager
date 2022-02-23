import { ICharacterEquippedArmor } from 'app/entities/character-equipped-armor/character-equipped-armor.model';
import { ArmorLocation } from 'app/entities/enumerations/armor-location.model';

export interface IArmorPiece {
  id?: number;
  location?: ArmorLocation | null;
  defenseModifier?: number | null;
  characterEquippedArmors?: ICharacterEquippedArmor[] | null;
}

export class ArmorPiece implements IArmorPiece {
  constructor(
    public id?: number,
    public location?: ArmorLocation | null,
    public defenseModifier?: number | null,
    public characterEquippedArmors?: ICharacterEquippedArmor[] | null
  ) {}
}

export function getArmorPieceIdentifier(armorPiece: IArmorPiece): number | undefined {
  return armorPiece.id;
}
