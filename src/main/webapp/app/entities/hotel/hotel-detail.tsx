import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hotel.reducer';

export const HotelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hotelEntity = useAppSelector(state => state.hotel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hotelDetailsHeading">
          <Translate contentKey="reservasApp.hotel.detail.title">Hotel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="reservasApp.hotel.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.nome}</dd>
          <dt>
            <span id="quantidadeQuartos">
              <Translate contentKey="reservasApp.hotel.quantidadeQuartos">Quantidade Quartos</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.quantidadeQuartos}</dd>
          <dt>
            <span id="vagas">
              <Translate contentKey="reservasApp.hotel.vagas">Vagas</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.vagas}</dd>
          <dt>
            <span id="classificacao">
              <Translate contentKey="reservasApp.hotel.classificacao">Classificacao</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.classificacao}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="reservasApp.hotel.status">Status</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <span id="cep">
              <Translate contentKey="reservasApp.hotel.cep">Cep</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.cep}</dd>
          <dt>
            <span id="bairro">
              <Translate contentKey="reservasApp.hotel.bairro">Bairro</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.bairro}</dd>
          <dt>
            <span id="endereco">
              <Translate contentKey="reservasApp.hotel.endereco">Endereco</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.endereco}</dd>
          <dt>
            <Translate contentKey="reservasApp.hotel.municipio">Municipio</Translate>
          </dt>
          <dd>{hotelEntity.municipio ? hotelEntity.municipio.id : ''}</dd>
          <dt>
            <Translate contentKey="reservasApp.hotel.pessoa">Pessoa</Translate>
          </dt>
          <dd>{hotelEntity.pessoa ? hotelEntity.pessoa.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hotel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hotel/${hotelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HotelDetail;
