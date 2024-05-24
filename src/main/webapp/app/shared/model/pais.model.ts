export interface IPais {
  id?: number;
  nome?: string | null;
  sigla?: string | null;
}

export const defaultValue: Readonly<IPais> = {};
