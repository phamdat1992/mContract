import { OverlayTrigger, Tooltip } from "react-bootstrap";
import {
  IconRead,
  IconDelete,
  IconDownload,
  IconUnread,
  IconTag
} from "@Components/Icon";
import React, { useEffect, useState } from "react";
import { useSelector } from 'react-redux';

const Pagination = ({ data, onPageChanged }) => {
  const [arrPage, setArrPage] = useState([]);
  useEffect(() => {
    arrangPage();
  }, [data])
  const arrangPage = () => {
    if (data.totalPages <= 5) {
      let arr = [];
      for (let i = 1; i <= data.totalPages; i++) {
        arr.push(i);
      }
      setArrPage(arr);
      return arr;
    } else {
      if (data.currentPage <= 3) {
        setArrPage([1, 2, 3, 4, 5]);
      } else if (data.currentPage > data.totalPages - 3) {
        setArrPage([data.totalPages - 4, data.totalPages - 3, data.totalPages - 2, data.totalPages - 1, data.totalPages]);
      } else {
        setArrPage([data.currentPage - 2, data.currentPage - 1, data.currentPage, data.currentPage + 1, data.currentPage + 2]);
      }
    }
  }
  return <>
    {arrPage.map((item, i) => {
      return (
        <>
          <li key={i} className={`page-item ${data.currentPage === (item - 1) ? 'active' : ''}`}>
            <a
              className="page-link"
              style={{ cursor: 'pointer' }}
              onClick={() => onPageChanged(item - 1)}
            >
              {item}
            </a>
          </li>
        </>
      )
    })}
  </>
}

const ContractActions = ({
  contracts,
  paginationData,
  selectedContracts,
  checkContracts,
  onCheckAll,
  onDeleteContracts,
  onCancelContracts,
  onDownloadContracts,
  onUpdateReadStatus,
  onAttachTagContracts,
  onPageChanged,
}) => {
  const [isCheckedAll, setIsCheckAll] = useState(false);
  const user = useSelector(state => state.auth.user);
  const checkPermissionCancel = () => {
    for (let i = 0; i < checkContracts.length; i++) {
      if (checkContracts[i].status == 'EXPIRED' || checkContracts[i].status == 'COMPLETE' || checkContracts[i].status == 'AUTHENTICATIONFAIL' || checkContracts[i].status == 'CANCEL') {
        return true;
      }
      if (checkContracts[i].signerDtos.filter(item => (item.email == user.email && item.isSigned)).length > 0) {
        return true;
      }
    }
    return false;
  }

  useEffect(() => {
    setIsCheckAll(contracts && contracts.length > 0 && checkContracts.filter(c => contracts.map(c => c.id).indexOf(c.id) > -1).length == contracts.length);
    // eslint-disable-next-line
  }, [checkContracts])

  return (
    <div id="sub_header">
      <div className="container-fluid px-24px">
        <div className="row doc_pagi">
          <div className="col-auto">
            <div className="action_area d-flex align-items-center">
              <div className="custom-control custom-checkbox">
                <input
                  type="checkbox"
                  className="custom-control-input"
                  id="checkAll"
                  checked={isCheckedAll}
                  onChange={(e) => onCheckAll({ value: e.target.checked })}
                />
                <label className="custom-control-label" htmlFor="checkAll"></label>
              </div>
              <div className="dropdown d-inline-flex align-items-center select_more_btn mr-2">
                <a
                  style={{ cursor: 'pointer' }}
                  className="select_icon"
                  id="selectButton"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                >
                  &#9660;
                </a>
                <div className="dropdown-menu shadow" aria-labelledby="selectButton">
                  <a
                    className="dropdown-item"
                    style={{ cursor: "pointer" }}
                    onClick={() => onCheckAll({ value: true })}
                  >
                    Chọn tất cả
                  </a>
                  <a
                    className="dropdown-item"
                    style={{ cursor: "pointer" }}
                    onClick={() => onCheckAll({ value: false })}
                  >
                    Bỏ chọn tất cả
                  </a>
                  <a
                    className="dropdown-item"
                    style={{ cursor: "pointer" }}
                    onClick={() =>
                      onCheckAll({ value: true, filter: { key: "wathched", value: true } })
                    }
                  >
                    Hợp đồng đã đọc
                  </a>
                  <a
                    className="dropdown-item"
                    style={{ cursor: "pointer" }}
                    onClick={() =>
                      onCheckAll({ value: true, filter: { key: "wathched", value: false } })
                    }
                  >
                    Hợp đồng chưa đọc
                  </a>
                  <a
                    className="dropdown-item"
                    style={{ cursor: "pointer" }}
                    onClick={() =>
                      onCheckAll({ value: true, filter: { key: "type", value: 'GUEST' } })
                    }
                  >
                    Hợp đồng đến
                  </a>
                  <a
                    className="dropdown-item"
                    style={{ cursor: "pointer" }}
                    onClick={() =>
                      onCheckAll({ value: true, filter: { key: "type", value: 'CREATER' } })
                    }
                  >
                    Hợp đồng đi
                  </a>
                </div>
              </div>
              {checkContracts.length > 0 && <>
                {!checkPermissionCancel() &&
                  <OverlayTrigger overlay={<Tooltip> Hủy hợp đồng </Tooltip>} placement="top">
                    {({ ref, ...triggerHandler }) => (
                      <a
                        style={{ cursor: 'pointer' }}
                        {...triggerHandler}
                        ref={ref}
                        className="action_icon"
                        onClick={(e) => onCancelContracts(e)}
                      >
                        <IconDelete />
                      </a>
                    )}
                  </OverlayTrigger>}
                <OverlayTrigger overlay={<Tooltip> Tải hợp đồng </Tooltip>} placement="top">
                  {({ ref, ...triggerHandler }) => (
                    <a
                      style={{ cursor: 'pointer' }}
                      {...triggerHandler}
                      ref={ref}
                      className="action_icon"
                      onClick={(e) => onDownloadContracts(e)}
                    >
                      <IconDownload />
                    </a>
                  )}
                </OverlayTrigger>
                <OverlayTrigger overlay={<Tooltip>Đánh dấu là đã đọc</Tooltip>} placement="top">
                  {({ ref, ...triggerHandler }) => (
                    <a
                      style={{ cursor: 'pointer' }}
                      {...triggerHandler}
                      ref={ref}
                      className="action_icon"
                      onClick={(e) => onUpdateReadStatus(e, true)}
                    >
                      <IconRead />
                    </a>
                  )}
                </OverlayTrigger>
                <OverlayTrigger overlay={<Tooltip>Đánh dấu là chưa đọc</Tooltip>} placement="top">
                  {({ ref, ...triggerHandler }) => (
                    <a
                      style={{ cursor: 'pointer' }}
                      {...triggerHandler}
                      ref={ref}
                      className="action_icon"
                      onClick={(e) => onUpdateReadStatus(e, false)}
                    >
                      <IconUnread />
                    </a>
                  )}
                </OverlayTrigger>
                <OverlayTrigger overlay={<Tooltip>Gắn thẻ</Tooltip>} placement="top">
                  {({ ref, ...triggerHandler }) => (
                    <>
                      <span>
                        <a
                          style={{ cursor: 'pointer' }}
                          {...triggerHandler}
                          ref={ref}
                          className="action_icon"
                          onClick={(e) => onAttachTagContracts(e)}
                        >
                          <IconTag />
                        </a>
                      </span>
                    </>
                  )}
                </OverlayTrigger>
              </>
              }
            </div>
          </div>

          <div className="col d-none d-sm-block">
            <div className="pagination_area text-right">
              <ul className="pagination mb-0 float-right">
                {paginationData.totalPages > 1 && (paginationData.currentPage + 1) > 1 ?
                  <li className="page-item">
                    <a
                      className="page-link"
                      style={{ cursor: 'pointer' }}
                      onClick={() => onPageChanged(paginationData.currentPage - 1)}
                    >
                      ←
                    </a>
                  </li>
                  : ''
                }
                <Pagination data={paginationData} onPageChanged={onPageChanged} />
                {paginationData.totalPages > 1 && (paginationData.currentPage + 1) < paginationData.totalPages ?
                  <li className="page-item">
                    <a
                      className="page-link"
                      style={{ cursor: 'pointer' }}
                      onClick={() => onPageChanged(paginationData.currentPage + 1)}
                    >
                      →
                    </a>
                  </li>
                  : ''
                }
              </ul>
              <span className="float-right d-none d-md-block">1 - {paginationData.pageSize} trong tổng số {paginationData.totalItems}</span>
              <div className="clearfix"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContractActions;
