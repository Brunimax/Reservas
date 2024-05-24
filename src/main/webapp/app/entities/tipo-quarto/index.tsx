import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TipoQuarto from './tipo-quarto';
import TipoQuartoDetail from './tipo-quarto-detail';
import TipoQuartoUpdate from './tipo-quarto-update';
import TipoQuartoDeleteDialog from './tipo-quarto-delete-dialog';

const TipoQuartoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TipoQuarto />} />
    <Route path="new" element={<TipoQuartoUpdate />} />
    <Route path=":id">
      <Route index element={<TipoQuartoDetail />} />
      <Route path="edit" element={<TipoQuartoUpdate />} />
      <Route path="delete" element={<TipoQuartoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TipoQuartoRoutes;
