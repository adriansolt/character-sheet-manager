import { ICharacter } from 'app/entities/character/character.model';

export interface INote {
  id?: number;
  description?: string | null;
  character?: ICharacter | null;
}

export class Note implements INote {
  constructor(public id?: number, public description?: string | null, public character?: ICharacter | null) {}
}

export function getNoteIdentifier(note: INote): number | undefined {
  return note.id;
}
