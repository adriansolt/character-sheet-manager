import { ICharacter } from 'app/entities/character/character.model';
import { ISkill } from 'app/entities/skill/skill.model';

export interface ICharacterSkill {
  id?: number;
  points?: number;
  skillModifier?: number;
  character?: ICharacter | null;
  skill?: ISkill | null;
}

export class CharacterSkill implements ICharacterSkill {
  constructor(
    public id?: number,
    public points?: number,
    public skillModifier?: number,
    public character?: ICharacter | null,
    public skill?: ISkill | null
  ) {}
}

export function getCharacterSkillIdentifier(characterSkill: ICharacterSkill): number | undefined {
  return characterSkill.id;
}
