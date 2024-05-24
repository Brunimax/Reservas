import dayjs from 'dayjs';

export interface IPessoa {
  id?: number;
  nome?: string | null;
  sobrenome?: string | null;
  cpf?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
  telefone?: string | null;
  email?: string | null;
}

export const defaultValue: Readonly<IPessoa> = {};
