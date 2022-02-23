import { ISkill } from 'app/entities/skill/skill.model';

export interface IPrereqSkillOrAtribute {
  id?: number;
  name?: string;
  level?: number;
  skillId?: ISkill | null;
}

export class PrereqSkillOrAtribute implements IPrereqSkillOrAtribute {
  constructor(public id?: number, public name?: string, public level?: number, public skillId?: ISkill | null) {}
}

export function getPrereqSkillOrAtributeIdentifier(prereqSkillOrAtribute: IPrereqSkillOrAtribute): number | undefined {
  return prereqSkillOrAtribute.id;
}
