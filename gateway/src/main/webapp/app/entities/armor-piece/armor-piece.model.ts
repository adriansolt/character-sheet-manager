import { ICharacterEquippedArmor } from 'app/entities/character-equipped-armor/character-equipped-armor.model';
import { ICampaign } from 'app/entities/campaign/campaign.model';
import { ICharacter } from 'app/entities/character/character.model';
import { ArmorLocation } from 'app/entities/enumerations/armor-location.model';

export interface IArmorPiece {
  id?: number;
  name?: string;
  description?: string | null;
  weight?: number;
  quality?: number;
  pictureContentType?: string | null;
  picture?: string | null;
  location?: ArmorLocation | null;
  defenseModifier?: number | null;
  characterEquippedArmors?: ICharacterEquippedArmor[] | null;
  campaign?: ICampaign | null;
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
    public campaign?: ICampaign | null,
    public character?: ICharacter | null
  ) {}
}

export function getArmorPieceIdentifier(armorPiece: IArmorPiece): number | undefined {
  return armorPiece.id;
}
