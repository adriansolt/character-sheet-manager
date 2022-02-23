import { ISkill } from 'app/entities/skill/skill.model';

export interface IDefaultSkillOrAtribute {
  id?: number;
  name?: string;
  modifier?: number;
  skill?: ISkill | null;
}

export class DefaultSkillOrAtribute implements IDefaultSkillOrAtribute {
  constructor(public id?: number, public name?: string, public modifier?: number, public skill?: ISkill | null) {}
}

export function getDefaultSkillOrAtributeIdentifier(defaultSkillOrAtribute: IDefaultSkillOrAtribute): number | undefined {
  return defaultSkillOrAtribute.id;
}
