import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tipo-quarto.reducer';

export const TipoQuartoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tipoQuartoEntity = useAppSelector(state => state.tipoQuarto.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tipoQuartoDetailsHeading">
          <Translate contentKey="reservasApp.tipoQuarto.detail.title">TipoQuarto</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tipoQuartoEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="reservasApp.tipoQuarto.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{tipoQuartoEntity.nome}</dd>
        </dl>
        <Button tag={Link} to="/tipo-quarto" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tipo-quarto/${tipoQuartoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TipoQuartoDetail;
