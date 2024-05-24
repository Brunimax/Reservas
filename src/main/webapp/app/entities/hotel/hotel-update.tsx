import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMunicipio } from 'app/shared/model/municipio.model';
import { getEntities as getMunicipios } from 'app/entities/municipio/municipio.reducer';
import { IPessoa } from 'app/shared/model/pessoa.model';
import { getEntities as getPessoas } from 'app/entities/pessoa/pessoa.reducer';
import { IHotel } from 'app/shared/model/hotel.model';
import { getEntity, updateEntity, createEntity, reset } from './hotel.reducer';

export const HotelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const municipios = useAppSelector(state => state.municipio.entities);
  const pessoas = useAppSelector(state => state.pessoa.entities);
  const hotelEntity = useAppSelector(state => state.hotel.entity);
  const loading = useAppSelector(state => state.hotel.loading);
  const updating = useAppSelector(state => state.hotel.updating);
  const updateSuccess = useAppSelector(state => state.hotel.updateSuccess);

  const handleClose = () => {
    navigate('/hotel');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMunicipios({}));
    dispatch(getPessoas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.quantidadeQuartos !== undefined && typeof values.quantidadeQuartos !== 'number') {
      values.quantidadeQuartos = Number(values.quantidadeQuartos);
    }
    if (values.vagas !== undefined && typeof values.vagas !== 'number') {
      values.vagas = Number(values.vagas);
    }
    if (values.classificacao !== undefined && typeof values.classificacao !== 'number') {
      values.classificacao = Number(values.classificacao);
    }

    const entity = {
      ...hotelEntity,
      ...values,
      municipio: municipios.find(it => it.id.toString() === values.municipio?.toString()),
      pessoa: pessoas.find(it => it.id.toString() === values.pessoa?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...hotelEntity,
          municipio: hotelEntity?.municipio?.id,
          pessoa: hotelEntity?.pessoa?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reservasApp.hotel.home.createOrEditLabel" data-cy="HotelCreateUpdateHeading">
            <Translate contentKey="reservasApp.hotel.home.createOrEditLabel">Create or edit a Hotel</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="hotel-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('reservasApp.hotel.nome')} id="hotel-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField
                label={translate('reservasApp.hotel.quantidadeQuartos')}
                id="hotel-quantidadeQuartos"
                name="quantidadeQuartos"
                data-cy="quantidadeQuartos"
                type="text"
              />
              <ValidatedField label={translate('reservasApp.hotel.vagas')} id="hotel-vagas" name="vagas" data-cy="vagas" type="text" />
              <ValidatedField
                label={translate('reservasApp.hotel.classificacao')}
                id="hotel-classificacao"
                name="classificacao"
                data-cy="classificacao"
                type="text"
              />
              <ValidatedField
                label={translate('reservasApp.hotel.status')}
                id="hotel-status"
                name="status"
                data-cy="status"
                check
                type="checkbox"
              />
              <ValidatedField label={translate('reservasApp.hotel.cep')} id="hotel-cep" name="cep" data-cy="cep" type="text" />
              <ValidatedField label={translate('reservasApp.hotel.bairro')} id="hotel-bairro" name="bairro" data-cy="bairro" type="text" />
              <ValidatedField
                label={translate('reservasApp.hotel.endereco')}
                id="hotel-endereco"
                name="endereco"
                data-cy="endereco"
                type="text"
              />
              <ValidatedField
                id="hotel-municipio"
                name="municipio"
                data-cy="municipio"
                label={translate('reservasApp.hotel.municipio')}
                type="select"
              >
                <option value="" key="0" />
                {municipios
                  ? municipios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="hotel-pessoa" name="pessoa" data-cy="pessoa" label={translate('reservasApp.hotel.pessoa')} type="select">
                <option value="" key="0" />
                {pessoas
                  ? pessoas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hotel" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default HotelUpdate;
