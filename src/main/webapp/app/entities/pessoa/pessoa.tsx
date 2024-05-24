import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './pessoa.reducer';

export const Pessoa = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const pessoaList = useAppSelector(state => state.pessoa.entities);
  const loading = useAppSelector(state => state.pessoa.loading);

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
      <h2 id="pessoa-heading" data-cy="PessoaHeading">
        <Translate contentKey="reservasApp.pessoa.home.title">Pessoas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reservasApp.pessoa.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pessoa/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reservasApp.pessoa.home.createLabel">Create new Pessoa</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pessoaList && pessoaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="reservasApp.pessoa.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="reservasApp.pessoa.nome">Nome</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('nome')} />
                </th>
                <th className="hand" onClick={sort('sobrenome')}>
                  <Translate contentKey="reservasApp.pessoa.sobrenome">Sobrenome</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sobrenome')} />
                </th>
                <th className="hand" onClick={sort('cpf')}>
                  <Translate contentKey="reservasApp.pessoa.cpf">Cpf</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('cpf')} />
                </th>
                <th className="hand" onClick={sort('dataNascimento')}>
                  <Translate contentKey="reservasApp.pessoa.dataNascimento">Data Nascimento</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataNascimento')} />
                </th>
                <th className="hand" onClick={sort('telefone')}>
                  <Translate contentKey="reservasApp.pessoa.telefone">Telefone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('telefone')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="reservasApp.pessoa.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pessoaList.map((pessoa, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pessoa/${pessoa.id}`} color="link" size="sm">
                      {pessoa.id}
                    </Button>
                  </td>
                  <td>{pessoa.nome}</td>
                  <td>{pessoa.sobrenome}</td>
                  <td>{pessoa.cpf}</td>
                  <td>
                    {pessoa.dataNascimento ? <TextFormat type="date" value={pessoa.dataNascimento} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{pessoa.telefone}</td>
                  <td>{pessoa.email}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pessoa/${pessoa.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/pessoa/${pessoa.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/pessoa/${pessoa.id}/delete`)}
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
              <Translate contentKey="reservasApp.pessoa.home.notFound">No Pessoas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Pessoa;