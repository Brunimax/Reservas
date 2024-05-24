import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './foto-quarto.reducer';

export const FotoQuarto = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const fotoQuartoList = useAppSelector(state => state.fotoQuarto.entities);
  const loading = useAppSelector(state => state.fotoQuarto.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="foto-quarto-heading" data-cy="FotoQuartoHeading">
        <Translate contentKey="reservasApp.fotoQuarto.home.title">Foto Quartos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reservasApp.fotoQuarto.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/foto-quarto/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reservasApp.fotoQuarto.home.createLabel">Create new Foto Quarto</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {fotoQuartoList && fotoQuartoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="reservasApp.fotoQuarto.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('foto')}>
                  <Translate contentKey="reservasApp.fotoQuarto.foto">Foto</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('foto')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="reservasApp.fotoQuarto.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th>
                  <Translate contentKey="reservasApp.fotoQuarto.quarto">Quarto</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fotoQuartoList.map((fotoQuarto, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/foto-quarto/${fotoQuarto.id}`} color="link" size="sm">
                      {fotoQuarto.id}
                    </Button>
                  </td>
                  <td>
                    {fotoQuarto.foto ? (
                      <div>
                        {fotoQuarto.fotoContentType ? (
                          <a onClick={openFile(fotoQuarto.fotoContentType, fotoQuarto.foto)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {fotoQuarto.fotoContentType}, {byteSize(fotoQuarto.foto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{fotoQuarto.status ? 'true' : 'false'}</td>
                  <td>{fotoQuarto.quarto ? <Link to={`/quarto/${fotoQuarto.quarto.id}`}>{fotoQuarto.quarto.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/foto-quarto/${fotoQuarto.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/foto-quarto/${fotoQuarto.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/foto-quarto/${fotoQuarto.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="reservasApp.fotoQuarto.home.notFound">No Foto Quartos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FotoQuarto;
