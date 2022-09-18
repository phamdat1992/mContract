import { useEffect, useState } from 'react';
import ContractActions from '../ContractActions';
import ContractList from '../ContractList';
import { TagModal } from '@Containers/Tag';
import DefaultFooter from '@Containers/Layout/Footer';
import SimpleBar from 'simplebar-react';
import ContractService from '@Services/contract';
import { useSelector } from 'react-redux';
import { useDispatch } from 'react-redux';
import { setListContracts, setReadyForReloading, setIsCanceling } from '@Redux/Actions/ListContract';
import * as JSZip from 'jszip';
import { saveAs } from 'file-saver';
import JSZipUtils from 'jszip-utils';
import * as Axios from 'axios';
import { setReadyForReloadStatistics } from '@Redux/Actions/Data';
import ConfirmModal from '@Components/ConfirmModal';
import { addToast } from '@Redux/Actions/AlertToast';

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
  // let elements = [];
  // for (let i = 0; i < data.totalPages; i++) {
  //   elements.push(
  //     <li key={i} className={`page-item ${data.currentPage === i ? 'active' : ''}`}>
  //       <a
  //         className="page-link"
  //         style={{ cursor: 'pointer' }}
  //         onClick={() => onPageChanged(i)}
  //       >
  //         {i + 1}
  //       </a>
  //     </li>
  //   )
  // }
  // return <>{elements}</>
}
const ContractTable = ({ query, showAction = true, showTag = true }) => {
  const user = useSelector(state => state.auth.user);
  const { contracts, pageSize, totalPages, totalItems, currentPage, isReadyForReloading, isCanceling } = useSelector(state => state.listContracts);
  const dispatch = useDispatch();
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [isRequesting, setIsRequesting] = useState(false);
  const [selectedContracts, setSelectedContracts] = useState([]);
  const [checkContracts, setCheckContracts] = useState([]);
  const [showTagModal, setShowTagModal] = useState(false);
  const [isRowContract, setRowContract] = useState(false);

  const paginationData = {
    pageSize: pageSize,
    totalPages: totalPages,
    totalItems: totalItems,
    currentPage: currentPage
  }
  let zip = new JSZip();

  const getContracts = async (page = null) => {
    try {
      const params = {
        size: pageSize,
        currentPage: page == null ? currentPage : page,
        // sortByDate: 'DESC',
        ...query,
      };
      const res = await ContractService.getContract(params);
      if (page != null && page != currentPage) {
        setSelectedContracts([]);
        setCheckContracts([]);
      } else {
        setSelectedContracts(res.data.filter(c => selectedContracts.filter(sC => sC.id == c.id).length > 0));
        setCheckContracts(res.data.filter(c => checkContracts.filter(sC => sC.id == c.id).length > 0));
      }
      dispatch(
        setListContracts({
          contracts: res.data.map(item => {
            if (item.tagDtos && item.tagDtos.length > 0) {
              item.tagDtos = item.tagDtos.reverse();
            }
            return item;
          }),
          pageSize: pageSize,
          totalPages: res.totalPage,
          currentPage: res.pageIndex,
          totalItems: res.total,
        }),
      );
    } catch (err) {
      console.error(err);
    } finally {
      setIsRequesting(false);
      if (isReadyForReloading) {
        dispatch(setReadyForReloading(false));
      }
    }
  };

  const getFile = fileUrl => {
    return new Promise((resolve, reject) => {
      Axios.get(fileUrl, { responseType: 'blob' })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
        });
    });
  };

  const onDownloadContracts = (event, contract = null) => {
    if (contract) {
      event.stopPropagation();
    }
    const data = {
      ids: contract ? [contract.id] : selectedContracts.map(item => item.id),
    };

    if (data.ids.length == 0) {
      return false;
    }
    ContractService.dowloadContract(data)
      .then(async res => {
        let fileDownload = res.data.fileInformations;
        if (fileDownload.length == 1) {
          const blob = await getFile(fileDownload[0].path);
          const objectUrl = URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = objectUrl;
          link.download = fileDownload[0].name;
          link.click();
        } else {
          let count = 0;
          fileDownload.forEach(url => {
            JSZipUtils.getBinaryContent(url.path, function (err, data) {
              if (err) {
                throw err;
              }
              zip.file(`${count + 1}_${url.name}`, data, { binary: true });
              count++;
              if (count == fileDownload.length) {
                zip.generateAsync({ type: 'blob' }).then(function (content) {
                  saveAs(content, 'contracts.zip');
                });
              }
            });
          });
        }
      })
      .catch(err => {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
      });
  };

  /**
   * Xóa danh sách hợp đồng
   * @param {*} contract Hợp đồng cần xóa (Truyền khi chỉ muốn xóa một HĐ)
   * @returns
   */
  const onDeleteContracts = (contract = null) => {
    const data = {
      contractIdList: contract != null ? [contract.id] : selectedContracts.map(item => item.id),
    };

    if (data.contractIdList.length == 0) {
      return false;
    }
    ContractService.deleteContract(data)
      .then(res => {
        getContracts();
        dispatch(setReadyForReloadStatistics(true));
      })
      .catch(err => {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
      });
  };

  const onCancelContracts = (event, contract = null, isRow = false) => {
    if (contract) {
      event.stopPropagation();
    }
    if (isRow && contract) {
      setSelectedContracts([contract]);
    } else {
      if (!isRow && contract) {
        setSelectedContracts([contract]);
        setCheckContracts([contract]);
      }
    }
    if (!contract && selectedContracts.length == 0) {
      return false;
    }
    setShowConfirmModal(true);
  }

  // const checkHaveSigned = (contractList) => {
  //   for (let i = 0; i < contractList.length; i++) {
  //     if (contractList[i].status == 'EXPIRED' || contractList[i].status == 'COMPLETE' || contractList[i].status == 'AUTHENTICATIONFAIL' || contractList[i].status == 'CANCEL') {
  //       return true;
  //     }
  //     if (contractList[i].signerDtos.filter(item => (item.email == user.email && item.isSigned)).length > 0) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }

  /**
   * Hủy danh sách hợp đồng
   * @param {*} contract 
   * @returns 
   */
  const handelCancelContracts = () => {
    setShowConfirmModal(false);
    dispatch(setIsCanceling(true));
    const data = {
      contractList: selectedContracts.map(item => item.id),
    };

    ContractService.cancelContracts(data)
      .then(res => {
        getContracts();
        dispatch(setIsCanceling(false));
        dispatch(setReadyForReloadStatistics(true));
        dispatch(addToast({ id: 'cancelContract_123456', type: 'SUCCESS_CANCEL_CONTRACT' }));
      })
      .catch(err => {
        dispatch(addToast({ id: Math.floor(Math.random() * 10000), type: err.errorCode, color: 'red' }));
        dispatch(setIsCanceling(false));
      });
  };

  const onHandleCloseCancel = () => {
    dispatch(setIsCanceling(false));
    setShowConfirmModal(false);
  };

  /**
   * Cập nhật trạng thái đã đọc
   * @param {boolean} status Trạng thái đọc muốn set
   * @param {number} contractId ID của contract muốn cập nhật trạng thái đọc (Không truyền khi cập nhật cho nhiều contract đã chọn)
   */
  function onUpdateReadStatus(event = null, status, contract = null) {
    if (event && contract) {
      event.stopPropagation();
    }
    const data = {
      contractIdList: contract != null ? [contract.id] : selectedContracts.map(item => item.id),
      status: status,
    };
    if (data.contractIdList.length == 0) {
      return false;
    }

    ContractService.updateWatched(data)
      .then(res => {
        getContracts();
      });
  }

  const onAttachTagContracts = (event, contract = null, isRow = false) => {
    if (contract) {
      event.stopPropagation();
    }
    if (!isRow && contract) {
      setCheckContracts([contract])
      setSelectedContracts([contract]);
    } else {
      if (isRow && contract) {
        setSelectedContracts([contract]);
      }
    }
    setShowTagModal(true);
  };

  const onPageChanged = i => {
    if (i < totalPages) {
      getContracts(i);
    }
  };

  const onCheckAll = data => {
    const checked = data.value;
    const filter = data.filter;
    if (checked) {
      if (filter) {
        const arr = contracts.filter(item => item[filter.key] === filter.value);
        setSelectedContracts(arr);
        setCheckContracts(arr);
      } else {
        setCheckContracts(contracts);
        setSelectedContracts(contracts);
      }
    } else {
      setSelectedContracts([]);
      setCheckContracts([]);
    }
  };

  const onCheck = (event, contract, checked) => {
    event.stopPropagation();
    if (checked) {
      const arr = [...selectedContracts, ...[contract]];
      setSelectedContracts(arr);
      const checkArr = [...checkContracts, ...[contract]];
      setCheckContracts(checkArr);
    } else {
      const arr = selectedContracts.filter(item => item.id != contract.id);
      setSelectedContracts(arr);
      const checkArr = checkContracts.filter(item => item.id != contract.id);
      setCheckContracts(checkArr);
    }
  };

  const handleClose = () => {
    setShowTagModal(false);
  };

  useEffect(() => {
    if (isReadyForReloading) {
      getContracts();
    }
    // eslint-disable-next-line
  }, [isReadyForReloading]);

  useEffect(() => {
    setIsRequesting(true);
    getContracts(0);
    // eslint-disable-next-line
  }, [query]);

  return (
    <>
      {showAction && contracts && contracts.length > 0 && (
        <ContractActions
          contracts={contracts}
          paginationData={paginationData}
          selectedContracts={selectedContracts}
          checkContracts={checkContracts}
          onCheckAll={onCheckAll}
          onDownloadContracts={onDownloadContracts}
          onUpdateReadStatus={onUpdateReadStatus}
          onDeleteContracts={onDeleteContracts}
          onCancelContracts={onCancelContracts}
          onAttachTagContracts={onAttachTagContracts}
          onPageChanged={onPageChanged}
        />
      )}
      <SimpleBar id="page_content " className="page-content-doc">
        <div className={`contract_area ${showAction && 'contract_area_doc'} pr-2`}>
          <div className="container-fluid h-100 px-24px">
            <ContractList
              contracts={contracts}
              paginationData={paginationData}
              selectedContracts={selectedContracts}
              checkContracts={checkContracts}
              isRequesting={isRequesting}
              showCheckbox={showAction}
              showTag={showTag}
              onCheck={onCheck}
              onDownloadContracts={onDownloadContracts}
              onUpdateReadStatus={onUpdateReadStatus}
              onDeleteContracts={onDeleteContracts}
              onCancelContracts={onCancelContracts}
              onAttachTagContracts={onAttachTagContracts}
            />

          </div>
        </div>
        {
          paginationData.totalPages > 1 &&
          <div className="pt-3 pagination-mobile">
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
        }
        <DefaultFooter />
      </SimpleBar>
      <TagModal show={showTagModal} contracts={selectedContracts} getContracts={getContracts} onHide={handleClose} ></TagModal>
      <ConfirmModal show={showConfirmModal} contractsName={selectedContracts.map(item => item.title).join(', ')} userName={user.fullName} onHide={onHandleCloseCancel} onSubmit={handelCancelContracts} />
    </>
  );
};

export default ContractTable;
