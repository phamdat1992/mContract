import { IconCalendar, IconDropdownFilter } from '@Components/Icon';
import { useEffect, useRef } from 'react';
import { Button, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import { useHistory, useLocation } from 'react-router-dom';
import IconCancel from '@Components/Icon/IconCancel';
import { useState } from 'react';
const ContractFilter = props => {
  const location = useLocation();
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    getValues,
    reset,
    formState: { errors },
  } = useForm();
  const filter = useRef();
  const close_af = useRef();
  const history = useHistory();
  function showAdvanceFilter() {
    let extend_filter = document.getElementById('extend_filter');
    extend_filter.style.display = 'none';
    filter.current.classList.toggle('af_show');
  }

  const watchSearch = watch('search', '');

  useEffect(() => {
    window.addEventListener('mouseup', e => {
      let extend_filter = document.getElementById('extend_filter');
      let dp = document.getElementsByClassName('datepicker-dropdown')[0];
      let container = filter.current;
      if (container !== undefined && container !== null) {
        if (!container.contains(e.target)) {
          if (dp !== undefined && dp.contains(e.target)) {
          } else {
            container.classList.remove('af_show');
            extend_filter.style.display = 'block';
          }
        }
        if (close_af.current.contains(e.target)) {
          container.classList.remove('af_show');
          extend_filter.style.display = 'block';
        }
      }
    });
  }, []);

  function formatDate(value) {
    if (value) {
      const arr = value.split('/')
      return arr[2] + '-' + arr[1] + '-' + arr[0];
    }
    return '';
  }

  function onSubmit(value) {
    let type = '';
    if (value.contractSend) {
      type = 'CREATER';
    }
    if (value.contractReceive) {
      type = 'GUEST';
    }
    if (value.contractSend && value.contractReceive) {
      type = '';
    }
    const params = {
      search: value.search.trim(),
      partner: value.partner.trim(),
      topic: value.topic.trim(),
      fromDate: formatDate(document.getElementById('from-date').value),
      toDate: formatDate(document.getElementById('to-date').value),
      type: type,
    };
    Object.keys(params).forEach(key => {
      if (!params[key]) {
        delete params[key];
      }
    });
    history.push('/tim-kiem-hop-dong?' + new URLSearchParams(params).toString());
  }

  function deleteSearch(value) {
    setValue('search', '');
  }

  useEffect(() => {
    const { pathname } = location;
    if (location.pathname.indexOf('tim-kiem-hop-dong') == -1) {
      reset();
    }
    // eslint-disable-next-line
  }, [location.pathname]);

  return (
    <>
      <div className="filter_input">
        <form onSubmit={handleSubmit(onSubmit)} autoComplete="off">
          <OverlayTrigger overlay={<Tooltip> Tìm kiếm</Tooltip>} placement="bottom">
            {({ ref, ...triggerHandler }) => (
              <Button {...triggerHandler} type="submit" ref={ref} variant="light" className="position-absolute" id="gosearch_btn">
                <IconDropdownFilter />
              </Button>
            )}
          </OverlayTrigger>
          <input
            type="text"
            className="general_search form-control"
            name="search"
            placeholder="Tìm kiếm hợp đồng"
            aria-label="Recipient's username"
            aria-describedby="button-addon2"
            {...register('search')}
          />
          {watchSearch ? (
            <>
              <OverlayTrigger overlay={<Tooltip> Xóa tìm kiếm </Tooltip>} placement="bottom">
                {({ ref, ...triggerHandler }) => (
                  <Button
                    {...triggerHandler}
                    ref={ref}
                    variant=""
                    className="icon_delete"
                    onClick={() => {
                      deleteSearch();
                    }}
                  >
                    <IconCancel />
                  </Button>
                )}
              </OverlayTrigger>
            </>
          ) : (
            <></>
          )}
          <div className="right_gs">
            <OverlayTrigger overlay={<Tooltip> Tìm kiếm nâng cao </Tooltip>} placement="bottom">
              {({ ref, ...triggerHandler }) => (
                <Button {...triggerHandler} ref={ref} variant="" className="btn_more" id="extend_filter" onClick={showAdvanceFilter}>
                  &#9660;
                </Button>
              )}
            </OverlayTrigger>
          </div>
          {/* <!-- Advanced Filter --> */}
          <div id="advanced_filter" ref={filter} className="shadow animated-filter-grow-in">
            <div className="form-group row">
              <label htmlFor="partnerInput" className="col-auto col-form-label">
                Đối tác
              </label>
              <div className="col-sm">
                <input type="text" className="form-control" autoComplete="off" id="partnerInput" {...register('partner')} />
              </div>
            </div>
            <div className="form-group row">
              <label htmlFor="subjectInput" className="col-auto col-form-label">
                Chủ đề
              </label>
              <div className="col-sm">
                <input type="text" className="form-control" autoComplete="off" id="subjectInput" {...register('topic')} />
              </div>
            </div>
            <div className="form-group row input-daterange">
              <label htmlFor="subjectInput" className="col-auto col-form-label pr-0">
                Thời gian
              </label>
              <div className="col-sm pr-3 pr-sm-1 pr-md-3 mb-2 mb-sm-0">
                <div className="input-group">
                  <input
                    id="fromDate"
                    type="text"
                    className="form-control datepicker-date"
                    id="from-date"
                    placeholder="Từ ngày"
                    aria-label="Từ ngày"
                    aria-describedby="button-addon2"
                    {...register('fromDate')}
                  />
                  <div className="input-group-append">
                    <span className="input-group-text bg-transparent" id="basic-addon2">
                      <IconCalendar />
                    </span>
                  </div>
                </div>
              </div>
              <div className="col-sm pl-3 pl-sm-1 pl-md-3">
                <div className="input-group">
                  <input
                    type="text"
                    className="form-control datepicker-date"
                    id="to-date"
                    placeholder="Đến ngày"
                    aria-label="Đến ngày"
                    aria-describedby="button-addon22"
                    {...register('toDate')}
                  />
                  <div className="input-group-append">
                    <span className="input-group-text bg-transparent" id="basic-addon22">
                      <IconCalendar />
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col">
                <div className="custom-control custom-checkbox custom-control-inline mb-2 mb-sm-0" style={{ marginRight: '30px' }}>
                  <input type="checkbox" className="custom-control-input" id="customCheckFrom" {...register('contractReceive')} />
                  <label className="custom-control-label" htmlFor="customCheckFrom">
                    Hợp đồng đến
                  </label>
                </div>
                <div className="custom-control custom-checkbox custom-control-inline">
                  <input type="checkbox" className="custom-control-input" id="customCheckTo" {...register('contractSend')} />
                  <label className="custom-control-label" htmlFor="customCheckTo">
                    Hợp đồng đi
                  </label>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col text-right">
                <button type="submit" className="btn btn_new mt-2" ref={close_af}>
                  Tìm kiếm
                </button>
              </div>
            </div>
          </div>
          {/*  End Advanced Filter  */}
        </form>
      </div>
    </>
  );
};

export default ContractFilter;
