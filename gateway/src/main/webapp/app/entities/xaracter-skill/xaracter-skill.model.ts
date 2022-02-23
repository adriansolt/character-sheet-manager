import { IXaracter } from 'app/entities/xaracter/xaracter.model';
import { ISkill } from 'app/entities/skill/skill.model';

export interface IXaracterSkill {
  id?: number;
  points?: number;
  skillModifier?: number | null;
  xaracterId?: IXaracter | null;
  skillId?: ISkill | null;
}

export class XaracterSkill implements IXaracterSkill {
  constructor(
    public id?: number,
    public points?: number,
    public skillModifier?: number | null,
    public xaracterId?: IXaracter | null,
    public skillId?: ISkill | null
  ) {}
}

export function getXaracterSkillIdentifier(xaracterSkill: IXaracterSkill): number | undefined {
  return xaracterSkill.id;
}
