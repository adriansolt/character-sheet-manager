import { ICharacterEquippedArmor } from 'app/entities/character-equipped-armor/character-equipped-armor.model';
import { ICharacter } from 'app/entities/character/character.model';
import { ArmorLocation } from 'app/entities/enumerations/armor-location.model';
import { IItem } from '../item/item.model';

export interface IArmorPiece extends IItem {
  location?: ArmorLocation | null;
  defenseModifier?: number | null;
  characterEquippedArmors?: ICharacterEquippedArmor[] | null;
  character?: ICharacter | null;
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
    public location?: ArmorLocation | null,
    public defenseModifier?: number | null,
    public characterEquippedArmors?: ICharacterEquippedArmor[] | null,
    public character?: ICharacter | null
  ) {}
}

export function getArmorPieceIdentifier(armorPiece: IArmorPiece): number | undefined {
  return armorPiece.id;
}
