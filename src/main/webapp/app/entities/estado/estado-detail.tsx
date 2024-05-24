import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './estado.reducer';

export const EstadoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const estadoEntity = useAppSelector(state => state.estado.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="estadoDetailsHeading">
          <Translate contentKey="reservasApp.estado.detail.title">Estado</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{estadoEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="reservasApp.estado.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{estadoEntity.nome}</dd>
          <dt>
            <span id="sigla">
              <Translate contentKey="reservasApp.estado.sigla">Sigla</Translate>
            </span>
          </dt>
          <dd>{estadoEntity.sigla}</dd>
          <dt>
            <Translate contentKey="reservasApp.estado.pais">Pais</Translate>
          </dt>
          <dd>{estadoEntity.pais ? estadoEntity.pais.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/estado" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/estado/${estadoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EstadoDetail;
