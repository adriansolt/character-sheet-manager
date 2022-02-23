import { ISkill } from 'app/entities/skill/skill.model';

export interface IPrereqSkillOrAtribute {
  id?: number;
  name?: string;
  level?: number;
  skill?: ISkill | null;
}

export class PrereqSkillOrAtribute implements IPrereqSkillOrAtribute {
  constructor(public id?: number, public name?: string, public level?: number, public skill?: ISkill | null) {}
}

export function getPrereqSkillOrAtributeIdentifier(prereqSkillOrAtribute: IPrereqSkillOrAtribute): number | undefined {
  return prereqSkillOrAtribute.id;
}
