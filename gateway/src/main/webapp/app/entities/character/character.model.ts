import { INote } from 'app/entities/note/note.model';
import { ICharacterAttribute } from 'app/entities/character-attribute/character-attribute.model';
import { ICharacterSkill } from 'app/entities/character-skill/character-skill.model';
import { IUser } from 'app/entities/user/user.model';
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
  campaignId?: number | null;
  active?: boolean | null;
  notes?: INote[] | null;
  characterAttributes?: ICharacterAttribute[] | null;
  characterSkills?: ICharacterSkill[] | null;
  user?: IUser;
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
    public campaignId?: number | null,
    public active?: boolean | null,
    public notes?: INote[] | null,
    public characterAttributes?: ICharacterAttribute[] | null,
    public characterSkills?: ICharacterSkill[] | null,
    public user?: IUser
  ) {
    this.active = this.active ?? false;
  }
}

export function getCharacterIdentifier(character: ICharacter): number | undefined {
  return character.id;
}
