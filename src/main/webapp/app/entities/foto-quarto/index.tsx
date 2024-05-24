import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FotoQuarto from './foto-quarto';
import FotoQuartoDetail from './foto-quarto-detail';
import FotoQuartoUpdate from './foto-quarto-update';
import FotoQuartoDeleteDialog from './foto-quarto-delete-dialog';

const FotoQuartoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FotoQuarto />} />
    <Route path="new" element={<FotoQuartoUpdate />} />
    <Route path=":id">
      <Route index element={<FotoQuartoDetail />} />
      <Route path="edit" element={<FotoQuartoUpdate />} />
      <Route path="delete" element={<FotoQuartoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FotoQuartoRoutes;
