import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './hotel.reducer';

export const Hotel = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const hotelList = useAppSelector(state => state.hotel.entities);
  const loading = useAppSelector(state => state.hotel.loading);

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
      <h2 id="hotel-heading" data-cy="HotelHeading">
        <Translate contentKey="reservasApp.hotel.home.title">Hotels</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reservasApp.hotel.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/hotel/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reservasApp.hotel.home.createLabel">Create new Hotel</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {hotelList && hotelList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="reservasApp.hotel.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="reservasApp.hotel.nome">Nome</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('nome')} />
                </th>
                <th className="hand" onClick={sort('quantidadeQuartos')}>
                  <Translate contentKey="reservasApp.hotel.quantidadeQuartos">Quantidade Quartos</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('quantidadeQuartos')} />
                </th>
                <th className="hand" onClick={sort('vagas')}>
                  <Translate contentKey="reservasApp.hotel.vagas">Vagas</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vagas')} />
                </th>
                <th className="hand" onClick={sort('classificacao')}>
                  <Translate contentKey="reservasApp.hotel.classificacao">Classificacao</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('classificacao')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="reservasApp.hotel.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('cep')}>
                  <Translate contentKey="reservasApp.hotel.cep">Cep</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('cep')} />
                </th>
                <th className="hand" onClick={sort('bairro')}>
                  <Translate contentKey="reservasApp.hotel.bairro">Bairro</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bairro')} />
                </th>
                <th className="hand" onClick={sort('endereco')}>
                  <Translate contentKey="reservasApp.hotel.endereco">Endereco</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endereco')} />
                </th>
                <th>
                  <Translate contentKey="reservasApp.hotel.municipio">Municipio</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reservasApp.hotel.pessoa">Pessoa</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hotelList.map((hotel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/hotel/${hotel.id}`} color="link" size="sm">
                      {hotel.id}
                    </Button>
                  </td>
                  <td>{hotel.nome}</td>
                  <td>{hotel.quantidadeQuartos}</td>
                  <td>{hotel.vagas}</td>
                  <td>{hotel.classificacao}</td>
                  <td>{hotel.status ? 'true' : 'false'}</td>
                  <td>{hotel.cep}</td>
                  <td>{hotel.bairro}</td>
                  <td>{hotel.endereco}</td>
                  <td>{hotel.municipio ? <Link to={`/municipio/${hotel.municipio.id}`}>{hotel.municipio.id}</Link> : ''}</td>
                  <td>{hotel.pessoa ? <Link to={`/pessoa/${hotel.pessoa.id}`}>{hotel.pessoa.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/hotel/${hotel.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/hotel/${hotel.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/hotel/${hotel.id}/delete`)}
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
              <Translate contentKey="reservasApp.hotel.home.notFound">No Hotels found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Hotel;
