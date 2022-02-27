import { INote } from 'app/entities/note/note.model';
import { ICharacterAttribute } from 'app/entities/character-attribute/character-attribute.model';
import { ICharacterSkill } from 'app/entities/character-skill/character-skill.model';
import { IItem } from 'app/entities/item/item.model';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';
import { IUser } from 'app/entities/user/user.model';
import { ICampaign } from 'app/entities/campaign/campaign.model';
import { Handedness } from 'app/entities/enumerations/handedness.model';

export interface ICharacter {
  id?: number;
  name?: string;
  weight?: number;
  height?: number;
  points?: number;
  pictureContentType?: string | null;
  picture?: string | null;
  handedness?: Handedness | null;
  active?: boolean | null;
  notes?: INote[] | null;
  characterAttributes?: ICharacterAttribute[] | null;
  characterSkills?: ICharacterSkill[] | null;
  items?: IItem[] | null;
  weapons?: IWeapon[] | null;
  armorPieces?: IArmorPiece[] | null;
  user?: IUser;
  campaign?: ICampaign | null;
}

export class Character implements ICharacter {
  constructor(
    public id?: number,
    public name?: string,
    public weight?: number,
    public height?: number,
    public points?: number,
    public pictureContentType?: string | null,
    public picture?: string | null,
    public handedness?: Handedness | null,
    public active?: boolean | null,
    public notes?: INote[] | null,
    public characterAttributes?: ICharacterAttribute[] | null,
    public characterSkills?: ICharacterSkill[] | null,
    public items?: IItem[] | null,
    public weapons?: IWeapon[] | null,
    public armorPieces?: IArmorPiece[] | null,
    public user?: IUser,
    public campaign?: ICampaign | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getCharacterIdentifier(character: ICharacter): number | undefined {
  return character.id;
}
