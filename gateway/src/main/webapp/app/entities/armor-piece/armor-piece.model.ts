import { ICharacterEquippedArmor } from 'app/entities/character-equipped-armor/character-equipped-armor.model';
import { ArmorLocation } from 'app/entities/enumerations/armor-location.model';
import { IItem } from '../item/item.model';

export interface IArmorPiece extends IItem {
  id?: number;
  location?: ArmorLocation | null;
  defenseModifier?: number | null;
  characterEquippedArmors?: ICharacterEquippedArmor[] | null;
}

export class ArmorPiece implements IArmorPiece {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public weight?: number,
    public quality?: number,
    public pictureContentType?: string | null,
    public picture?: string | null,
    public characterId?: number | null,
    public campaignId?: number | null,
    public location?: ArmorLocation | null,
    public defenseModifier?: number | null,
    public characterEquippedArmors?: ICharacterEquippedArmor[] | null
  ) {}
}

export function getArmorPieceIdentifier(armorPiece: IArmorPiece): number | undefined {
  return armorPiece.id;
}
