import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAvaliacao } from 'app/shared/model/avaliacao.model';
import { getEntities as getAvaliacaos } from 'app/entities/avaliacao/avaliacao.reducer';
import { IQuarto } from 'app/shared/model/quarto.model';
import { getEntities as getQuartos } from 'app/entities/quarto/quarto.reducer';
import { IPessoa } from 'app/shared/model/pessoa.model';
import { getEntities as getPessoas } from 'app/entities/pessoa/pessoa.reducer';
import { IReserva } from 'app/shared/model/reserva.model';
import { getEntity, updateEntity, createEntity, reset } from './reserva.reducer';

export const ReservaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const avaliacaos = useAppSelector(state => state.avaliacao.entities);
  const quartos = useAppSelector(state => state.quarto.entities);
  const pessoas = useAppSelector(state => state.pessoa.entities);
  const reservaEntity = useAppSelector(state => state.reserva.entity);
  const loading = useAppSelector(state => state.reserva.loading);
  const updating = useAppSelector(state => state.reserva.updating);
  const updateSuccess = useAppSelector(state => state.reserva.updateSuccess);

  const handleClose = () => {
    navigate('/reserva');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAvaliacaos({}));
    dispatch(getQuartos({}));
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
    values.dataChekin = convertDateTimeToServer(values.dataChekin);
    values.dataChekout = convertDateTimeToServer(values.dataChekout);
    values.dataReserva = convertDateTimeToServer(values.dataReserva);

    const entity = {
      ...reservaEntity,
      ...values,
      avaliacao: avaliacaos.find(it => it.id.toString() === values.avaliacao?.toString()),
      quarto: quartos.find(it => it.id.toString() === values.quarto?.toString()),
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
      ? {
          dataChekin: displayDefaultDateTime(),
          dataChekout: displayDefaultDateTime(),
          dataReserva: displayDefaultDateTime(),
        }
      : {
          ...reservaEntity,
          dataChekin: convertDateTimeFromServer(reservaEntity.dataChekin),
          dataChekout: convertDateTimeFromServer(reservaEntity.dataChekout),
          dataReserva: convertDateTimeFromServer(reservaEntity.dataReserva),
          avaliacao: reservaEntity?.avaliacao?.id,
          quarto: reservaEntity?.quarto?.id,
          pessoa: reservaEntity?.pessoa?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reservasApp.reserva.home.createOrEditLabel" data-cy="ReservaCreateUpdateHeading">
            <Translate contentKey="reservasApp.reserva.home.createOrEditLabel">Create or edit a Reserva</Translate>
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
                  id="reserva-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('reservasApp.reserva.dataChekin')}
                id="reserva-dataChekin"
                name="dataChekin"
                data-cy="dataChekin"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('reservasApp.reserva.dataChekout')}
                id="reserva-dataChekout"
                name="dataChekout"
                data-cy="dataChekout"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('reservasApp.reserva.dataReserva')}
                id="reserva-dataReserva"
                name="dataReserva"
                data-cy="dataReserva"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="reserva-avaliacao"
                name="avaliacao"
                data-cy="avaliacao"
                label={translate('reservasApp.reserva.avaliacao')}
                type="select"
              >
                <option value="" key="0" />
                {avaliacaos
                  ? avaliacaos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="reserva-quarto"
                name="quarto"
                data-cy="quarto"
                label={translate('reservasApp.reserva.quarto')}
                type="select"
              >
                <option value="" key="0" />
                {quartos
                  ? quartos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="reserva-pessoa"
                name="pessoa"
                data-cy="pessoa"
                label={translate('reservasApp.reserva.pessoa')}
                type="select"
              >
                <option value="" key="0" />
                {pessoas
                  ? pessoas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reserva" replace color="info">
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

export default ReservaUpdate;
