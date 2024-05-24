import dayjs from 'dayjs';
import { IAvaliacao } from 'app/shared/model/avaliacao.model';
import { IQuarto } from 'app/shared/model/quarto.model';
import { IPessoa } from 'app/shared/model/pessoa.model';

export interface IReserva {
  id?: number;
  dataChekin?: dayjs.Dayjs | null;
  dataChekout?: dayjs.Dayjs | null;
  dataReserva?: dayjs.Dayjs | null;
  avaliacao?: IAvaliacao | null;
  quarto?: IQuarto | null;
  pessoa?: IPessoa | null;
}

export const defaultValue: Readonly<IReserva> = {};
