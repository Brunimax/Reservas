import { faArrowLeft, faArrowRight, faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { nextPage, previusPage } from 'app/shared/util/miscs';
import React, { useMemo } from 'react';
import { Button, Col } from 'reactstrap';
import './pagination.scss';

interface IPageCounter {
  totalItens: number;
  page: number;
  setPage: React.Dispatch<React.SetStateAction<number>>;
}

const PageCounter = (props: IPageCounter) => {
  const countPage = useMemo(() => {
    return Math.ceil(props?.totalItens / 10);
  }, [props?.totalItens]);

  return (
    <>
      <Col className="container-pagination-buttons">
        <Button disabled={props.page === 0} onClick={() => previusPage(props.page, props.setPage)} className="button-pagination-left">
          <FontAwesomeIcon icon={faChevronLeft} />
        </Button>
        <span style={{ margin: '1.5em' }}>
          {countPage === 0 ? props.page : props.page + 1} de {countPage}
        </span>
        <Button
          disabled={props.page === countPage - 1 || countPage === 0}
          onClick={() => nextPage(props.page, props.setPage)}
          className="button-pagination-rigth"
        >
          <FontAwesomeIcon icon={faChevronRight} />
        </Button>
      </Col>
    </>
  );
};

export default PageCounter;
