import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Municipio from './municipio';
import MunicipioDetail from './municipio-detail';
import MunicipioUpdate from './municipio-update';
import MunicipioDeleteDialog from './municipio-delete-dialog';

const MunicipioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Municipio />} />
    <Route path="new" element={<MunicipioUpdate />} />
    <Route path=":id">
      <Route index element={<MunicipioDetail />} />
      <Route path="edit" element={<MunicipioUpdate />} />
      <Route path="delete" element={<MunicipioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MunicipioRoutes;
