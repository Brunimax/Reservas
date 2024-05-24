import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPessoa } from 'app/shared/model/pessoa.model';
import { getEntity, updateEntity, createEntity, reset } from './pessoa.reducer';

export const PessoaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pessoaEntity = useAppSelector(state => state.pessoa.entity);
  const loading = useAppSelector(state => state.pessoa.loading);
  const updating = useAppSelector(state => state.pessoa.updating);
  const updateSuccess = useAppSelector(state => state.pessoa.updateSuccess);

  const handleClose = () => {
    navigate('/pessoa');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    values.dataNascimento = convertDateTimeToServer(values.dataNascimento);

    const entity = {
      ...pessoaEntity,
      ...values,
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
          dataNascimento: displayDefaultDateTime(),
        }
      : {
          ...pessoaEntity,
          dataNascimento: convertDateTimeFromServer(pessoaEntity.dataNascimento),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reservasApp.pessoa.home.createOrEditLabel" data-cy="PessoaCreateUpdateHeading">
            <Translate contentKey="reservasApp.pessoa.home.createOrEditLabel">Create or edit a Pessoa</Translate>
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
                  id="pessoa-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('reservasApp.pessoa.nome')} id="pessoa-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField
                label={translate('reservasApp.pessoa.sobrenome')}
                id="pessoa-sobrenome"
                name="sobrenome"
                data-cy="sobrenome"
                type="text"
              />
              <ValidatedField label={translate('reservasApp.pessoa.cpf')} id="pessoa-cpf" name="cpf" data-cy="cpf" type="text" />
              <ValidatedField
                label={translate('reservasApp.pessoa.dataNascimento')}
                id="pessoa-dataNascimento"
                name="dataNascimento"
                data-cy="dataNascimento"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('reservasApp.pessoa.telefone')}
                id="pessoa-telefone"
                name="telefone"
                data-cy="telefone"
                type="text"
              />
              <ValidatedField label={translate('reservasApp.pessoa.email')} id="pessoa-email" name="email" data-cy="email" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pessoa" replace color="info">
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

export default PessoaUpdate;
