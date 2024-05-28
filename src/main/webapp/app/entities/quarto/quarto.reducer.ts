import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { ASC } from 'app/shared/util/pagination.constants';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IQuarto, defaultValue } from 'app/shared/model/quarto.model';

const initialState: EntityState<IQuarto> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/quartos';

// Actions

export const getEntities = createAsyncThunk('quarto/fetch_entity_list', async ({ sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IQuarto[]>(requestUrl);
});

export const getEntitiesList = createAsyncThunk('quarto/fetch_entity_list', async ({ query, page }: IQueryParams) => {
  const navaApiUrl = 'api/quartos/listaPage';
  const requestUrl = `${navaApiUrl}?${query ? `search=${query}&` : ''}&page=${page}&size=${10}&sort=ASCcacheBuster=${new Date().getTime()}`;
  return axios.get<IQuarto[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'quarto/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IQuarto>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'quarto/create_entity',
  async (entity: IQuarto, thunkAPI) => {
    const result = await axios.post<IQuarto>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'quarto/update_entity',
  async (entity: IQuarto, thunkAPI) => {
    const result = await axios.put<IQuarto>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'quarto/partial_update_entity',
  async (entity: IQuarto, thunkAPI) => {
    const result = await axios.patch<IQuarto>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'quarto/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IQuarto>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const QuartoSlice = createEntitySlice({
  name: 'quarto',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(action.payload.headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = QuartoSlice.actions;

// Reducer
export default QuartoSlice.reducer;
