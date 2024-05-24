import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pais from './pais';
import PaisDetail from './pais-detail';
import PaisUpdate from './pais-update';
import PaisDeleteDialog from './pais-delete-dialog';

const PaisRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pais />} />
    <Route path="new" element={<PaisUpdate />} />
    <Route path=":id">
      <Route index element={<PaisDetail />} />
      <Route path="edit" element={<PaisUpdate />} />
      <Route path="delete" element={<PaisDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaisRoutes;
