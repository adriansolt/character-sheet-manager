import { INote } from 'app/entities/note/note.model';
import { IXaracterAttribute } from 'app/entities/xaracter-attribute/xaracter-attribute.model';
import { IXaracterSkill } from 'app/entities/xaracter-skill/xaracter-skill.model';
import { IUser } from 'app/entities/user/user.model';
import { Handedness } from 'app/entities/enumerations/handedness.model';

export interface IXaracter {
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
  xaracterAttributes?: IXaracterAttribute[] | null;
  xaracterSkills?: IXaracterSkill[] | null;
  user?: IUser;
}

export class Xaracter implements IXaracter {
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
    public xaracterAttributes?: IXaracterAttribute[] | null,
    public xaracterSkills?: IXaracterSkill[] | null,
    public user?: IUser
  ) {
    this.active = this.active ?? false;
  }
}

export function getXaracterIdentifier(xaracter: IXaracter): number | undefined {
  return xaracter.id;
}
