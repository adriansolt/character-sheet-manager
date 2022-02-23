import { IXaracter } from 'app/entities/xaracter/xaracter.model';

export interface INote {
  id?: number;
  description?: string | null;
  xaracterId?: IXaracter | null;
}

export class Note implements INote {
  constructor(public id?: number, public description?: string | null, public xaracterId?: IXaracter | null) {}
}

export function getNoteIdentifier(note: INote): number | undefined {
  return note.id;
}
