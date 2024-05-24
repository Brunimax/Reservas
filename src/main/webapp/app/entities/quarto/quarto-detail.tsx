import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './quarto.reducer';

export const QuartoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const quartoEntity = useAppSelector(state => state.quarto.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="quartoDetailsHeading">
          <Translate contentKey="reservasApp.quarto.detail.title">Quarto</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{quartoEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="reservasApp.quarto.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{quartoEntity.nome}</dd>
          <dt>
            <span id="quantidadeHospedes">
              <Translate contentKey="reservasApp.quarto.quantidadeHospedes">Quantidade Hospedes</Translate>
            </span>
          </dt>
          <dd>{quartoEntity.quantidadeHospedes}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="reservasApp.quarto.status">Status</Translate>
            </span>
          </dt>
          <dd>{quartoEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <span id="classificacao">
              <Translate contentKey="reservasApp.quarto.classificacao">Classificacao</Translate>
            </span>
          </dt>
          <dd>{quartoEntity.classificacao}</dd>
          <dt>
            <Translate contentKey="reservasApp.quarto.hotel">Hotel</Translate>
          </dt>
          <dd>{quartoEntity.hotel ? quartoEntity.hotel.id : ''}</dd>
          <dt>
            <Translate contentKey="reservasApp.quarto.tipoQuarto">Tipo Quarto</Translate>
          </dt>
          <dd>{quartoEntity.tipoQuarto ? quartoEntity.tipoQuarto.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/quarto" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/quarto/${quartoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuartoDetail;
