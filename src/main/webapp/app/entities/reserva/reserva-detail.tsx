import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reserva.reducer';

export const ReservaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservaEntity = useAppSelector(state => state.reserva.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservaDetailsHeading">
          <Translate contentKey="reservasApp.reserva.detail.title">Reserva</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.id}</dd>
          <dt>
            <span id="dataChekin">
              <Translate contentKey="reservasApp.reserva.dataChekin">Data Chekin</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.dataChekin ? <TextFormat value={reservaEntity.dataChekin} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="dataChekout">
              <Translate contentKey="reservasApp.reserva.dataChekout">Data Chekout</Translate>
            </span>
          </dt>
          <dd>
            {reservaEntity.dataChekout ? <TextFormat value={reservaEntity.dataChekout} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dataReserva">
              <Translate contentKey="reservasApp.reserva.dataReserva">Data Reserva</Translate>
            </span>
          </dt>
          <dd>
            {reservaEntity.dataReserva ? <TextFormat value={reservaEntity.dataReserva} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="reservasApp.reserva.avaliacao">Avaliacao</Translate>
          </dt>
          <dd>{reservaEntity.avaliacao ? reservaEntity.avaliacao.id : ''}</dd>
          <dt>
            <Translate contentKey="reservasApp.reserva.quarto">Quarto</Translate>
          </dt>
          <dd>{reservaEntity.quarto ? reservaEntity.quarto.id : ''}</dd>
          <dt>
            <Translate contentKey="reservasApp.reserva.pessoa">Pessoa</Translate>
          </dt>
          <dd>{reservaEntity.pessoa ? reservaEntity.pessoa.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reserva" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reserva/${reservaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservaDetail;
