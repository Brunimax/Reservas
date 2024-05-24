import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHotel } from 'app/shared/model/hotel.model';
import { getEntities as getHotels } from 'app/entities/hotel/hotel.reducer';
import { ITipoQuarto } from 'app/shared/model/tipo-quarto.model';
import { getEntities as getTipoQuartos } from 'app/entities/tipo-quarto/tipo-quarto.reducer';
import { IQuarto } from 'app/shared/model/quarto.model';
import { getEntity, updateEntity, createEntity, reset } from './quarto.reducer';

export const QuartoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hotels = useAppSelector(state => state.hotel.entities);
  const tipoQuartos = useAppSelector(state => state.tipoQuarto.entities);
  const quartoEntity = useAppSelector(state => state.quarto.entity);
  const loading = useAppSelector(state => state.quarto.loading);
  const updating = useAppSelector(state => state.quarto.updating);
  const updateSuccess = useAppSelector(state => state.quarto.updateSuccess);

  const handleClose = () => {
    navigate('/quarto');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHotels({}));
    dispatch(getTipoQuartos({}));
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
    if (values.quantidadeHospedes !== undefined && typeof values.quantidadeHospedes !== 'number') {
      values.quantidadeHospedes = Number(values.quantidadeHospedes);
    }
    if (values.classificacao !== undefined && typeof values.classificacao !== 'number') {
      values.classificacao = Number(values.classificacao);
    }

    const entity = {
      ...quartoEntity,
      ...values,
      hotel: hotels.find(it => it.id.toString() === values.hotel?.toString()),
      tipoQuarto: tipoQuartos.find(it => it.id.toString() === values.tipoQuarto?.toString()),
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
          ...quartoEntity,
          hotel: quartoEntity?.hotel?.id,
          tipoQuarto: quartoEntity?.tipoQuarto?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reservasApp.quarto.home.createOrEditLabel" data-cy="QuartoCreateUpdateHeading">
            <Translate contentKey="reservasApp.quarto.home.createOrEditLabel">Create or edit a Quarto</Translate>
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
                  id="quarto-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('reservasApp.quarto.nome')} id="quarto-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField
                label={translate('reservasApp.quarto.quantidadeHospedes')}
                id="quarto-quantidadeHospedes"
                name="quantidadeHospedes"
                data-cy="quantidadeHospedes"
                type="text"
              />
              <ValidatedField
                label={translate('reservasApp.quarto.status')}
                id="quarto-status"
                name="status"
                data-cy="status"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('reservasApp.quarto.classificacao')}
                id="quarto-classificacao"
                name="classificacao"
                data-cy="classificacao"
                type="text"
              />
              <ValidatedField id="quarto-hotel" name="hotel" data-cy="hotel" label={translate('reservasApp.quarto.hotel')} type="select">
                <option value="" key="0" />
                {hotels
                  ? hotels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="quarto-tipoQuarto"
                name="tipoQuarto"
                data-cy="tipoQuarto"
                label={translate('reservasApp.quarto.tipoQuarto')}
                type="select"
              >
                <option value="" key="0" />
                {tipoQuartos
                  ? tipoQuartos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/quarto" replace color="info">
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

export default QuartoUpdate;
