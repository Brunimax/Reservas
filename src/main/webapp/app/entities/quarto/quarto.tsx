import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './quarto.reducer';

export const Quarto = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const quartoList = useAppSelector(state => state.quarto.entities);
  const loading = useAppSelector(state => state.quarto.loading);

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
      <h2 id="quarto-heading" data-cy="QuartoHeading">
        <Translate contentKey="reservasApp.quarto.home.title">Quartos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reservasApp.quarto.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/quarto/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reservasApp.quarto.home.createLabel">Create new Quarto</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {quartoList && quartoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="reservasApp.quarto.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="reservasApp.quarto.nome">Nome</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('nome')} />
                </th>
                <th className="hand" onClick={sort('quantidadeHospedes')}>
                  <Translate contentKey="reservasApp.quarto.quantidadeHospedes">Quantidade Hospedes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('quantidadeHospedes')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="reservasApp.quarto.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('classificacao')}>
                  <Translate contentKey="reservasApp.quarto.classificacao">Classificacao</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('classificacao')} />
                </th>
                <th>
                  <Translate contentKey="reservasApp.quarto.hotel">Hotel</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reservasApp.quarto.tipoQuarto">Tipo Quarto</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {quartoList.map((quarto, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/quarto/${quarto.id}`} color="link" size="sm">
                      {quarto.id}
                    </Button>
                  </td>
                  <td>{quarto.nome}</td>
                  <td>{quarto.quantidadeHospedes}</td>
                  <td>{quarto.status ? 'true' : 'false'}</td>
                  <td>{quarto.classificacao}</td>
                  <td>{quarto.hotel ? <Link to={`/hotel/${quarto.hotel.id}`}>{quarto.hotel.id}</Link> : ''}</td>
                  <td>{quarto.tipoQuarto ? <Link to={`/tipo-quarto/${quarto.tipoQuarto.id}`}>{quarto.tipoQuarto.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/quarto/${quarto.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/quarto/${quarto.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/quarto/${quarto.id}/delete`)}
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
              <Translate contentKey="reservasApp.quarto.home.notFound">No Quartos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Quarto;
