import React from 'react';
import { Spinner } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
// import { setContract, updateContract, setCurrentSigner } from '@Redux/Actions/DetailContract';
import ContractItem from '../ContractItem';
const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
  <a
    href="javascript:void(0)"
    className="three_dots"
    id="dropdownMenuButton"
    ref={ref}
    onClick={e => {
      e.stopPropagation();
      onClick(e);
    }}
  >
    {children}
  </a>
));

const ContractList = ({ contracts, selectedContracts, checkContracts, onCheck, showAction = true, showAvatar = false, showContractType = false, onUpdateReadStatus, onDeleteContracts, onCancelContracts, onDownloadContracts, onAttachTagContracts, isRequesting, isCanceling }) => {
  const user = useSelector(state => state.auth.user);
  // const [isSigning, setIsSigning] = useState(false);
  const dispatch = useDispatch();
  const { isCreatingContract } = useSelector(state => state.createContract);

  const history = useHistory();

  const isContractCompleted = contract => {
    return contract.signerDtos.filter(signer => signer.isSigned).length == contract.signerDtos.length;
  };

  const isSigned = contract => {
    return contract.signerDtos.filter(signer => signer.isSigned).filter(signer => signer.email == user.email).length > 0;
  };

  const getSignersSigned = contract => {
    return contract.signerDtos.filter(signer => signer.isSigned);
  };
  function listContractTag(event, tag) {
    event.stopPropagation();
    const params = {
      tagId: tag.id,
    };
    Object.keys(params).forEach(key => {
      if (!params[key]) {
        delete params[key];
      }
    });
    history.push('/hop-dong-theo-the?' + new URLSearchParams(params).toString());
  }

  return (
    <>
      {isRequesting ? (
        <div className={`contract_item text-center p-3 mt-3`}>
          <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" className="mr-3" /> Đang tải dữ liệu...
        </div>
      ) : (
        <>
          {isCreatingContract && (
            <div className={`contract_item contract_item_sending text-center p-3 ${contracts && contracts.length > 0 ? '' : 'mt-3'}`} style={{ borderBottom: (contracts && contracts.length > 0) ? 0 : '1px solid #d9d9d9' }}>
              <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Đang tạo hợp đồng....
            </div>
          )}
          {contracts && contracts.length > 0 ? (
            contracts.map((contract, index) => (
              <ContractItem key={`contractItem_${contract.id}`} contract={contract} index={index} showAction={showAction} showAvatar={showAvatar} selectedContracts={selectedContracts} checkContracts={checkContracts} onCheck={onCheck} showContractType={showContractType} onUpdateReadStatus={onUpdateReadStatus} onDeleteContracts={onDeleteContracts} onCancelContracts={onCancelContracts} onDownloadContracts={onDownloadContracts} onAttachTagContracts={onAttachTagContracts} isRequesting={isRequesting} isCanceling={isCanceling} />
            ))
          ) : (
            <>
              {
                !isCreatingContract &&
                <div className="py-3" style={{ textAlign: 'center', color: 'black' }}>
                  Không tìm thấy hợp đồng nào
                </div>
              }
            </>
          )}
        </>
      )}
    </>
  );
};
export default ContractList;
