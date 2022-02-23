import { IXaracterSkill } from 'app/entities/xaracter-skill/xaracter-skill.model';
import { IDefaultSkillOrAtribute } from 'app/entities/default-skill-or-atribute/default-skill-or-atribute.model';
import { IPrereqSkillOrAtribute } from 'app/entities/prereq-skill-or-atribute/prereq-skill-or-atribute.model';
import { SkillName } from 'app/entities/enumerations/skill-name.model';
import { Difficulty } from 'app/entities/enumerations/difficulty.model';

export interface ISkill {
  id?: number;
  name?: SkillName;
  difficulty?: Difficulty;
  xaracterSkills?: IXaracterSkill[] | null;
  defaultSkillOrAtributes?: IDefaultSkillOrAtribute[] | null;
  prereqSkillOrAtributes?: IPrereqSkillOrAtribute[] | null;
}

export class Skill implements ISkill {
  constructor(
    public id?: number,
    public name?: SkillName,
    public difficulty?: Difficulty,
    public xaracterSkills?: IXaracterSkill[] | null,
    public defaultSkillOrAtributes?: IDefaultSkillOrAtribute[] | null,
    public prereqSkillOrAtributes?: IPrereqSkillOrAtribute[] | null
  ) {}
}

export function getSkillIdentifier(skill: ISkill): number | undefined {
  return skill.id;
}
