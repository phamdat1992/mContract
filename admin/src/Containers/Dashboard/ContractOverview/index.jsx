import ContractList from '@Containers/Contract/ContractList';
import ContractService from '@Services/contract';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setNewContracts, setReadyForReloading } from '@Redux/Actions/NewContract';
function ContractOverview(props) {
  const { newContracts, isReadyForReloading } = useSelector(state => state.newContracts);
  const [isRequesting, setIsRequesting] = useState(false);
  const dispatch = useDispatch();
  function onUpdateReadStatus(status, contract = null) {
    const data = {
      contractIdList: [contract.id],
      status: status,
    };
    ContractService.updateWatched(data)
      .then(res => {
        dispatch(setNewContracts(res.data));
      });
  }

  const getNewContract = async () => {
    try {
      let params = {
        sortByDate: 'DESC',
        currentPage: 0,
        size: 5,
      };
      const res = await ContractService.getContract(params);
      dispatch(setNewContracts(res.data));
    } catch (err) {
      console.error(err);
    } finally {
      setIsRequesting(false);
      if (isReadyForReloading) {
        dispatch(setReadyForReloading(false));
      }
    }
  };
  
  useEffect(() => {
    setIsRequesting(true);
    getNewContract();
  }, []);

  useEffect(() => {
    if (isReadyForReloading) {
      getNewContract();
    }
  }, [isReadyForReloading]);

  return (
    <div className="contract_overview mt-4 shadow-sm">
      <div className="co_header p-3">
        <h4 className="mb-0">HỢP ĐỒNG MỚI</h4>
      </div>
      <div className="contract_area">
        <ContractList contracts={newContracts} showAction={false} isRequesting={isRequesting} showAvatar={true} showContractType={true} onUpdateReadStatus={onUpdateReadStatus} />
      </div>
    </div>
  );
}

export default ContractOverview;
