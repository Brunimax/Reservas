import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Quarto from './quarto';
import QuartoDetail from './quarto-detail';
import QuartoUpdate from './quarto-update';
import QuartoDeleteDialog from './quarto-delete-dialog';

const QuartoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Quarto />} />
    <Route path="new" element={<QuartoUpdate />} />
    <Route path=":id">
      <Route index element={<QuartoDetail />} />
      <Route path="edit" element={<QuartoUpdate />} />
      <Route path="delete" element={<QuartoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuartoRoutes;
