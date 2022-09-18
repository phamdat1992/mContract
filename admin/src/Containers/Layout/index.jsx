import ContractCreateButtonMobile from '@Containers/Contract/ContractCreateButtonMobile';
import { TagModal } from '@Containers/Tag';
import { resetCreateContract, setCreatingContract, setReadyForCreating } from '@Redux/Actions/CreateContract';
import { setReadyForReloadStatistics, setStatistics, setTags, setSumUnreadNotification } from '@Redux/Actions/Data';
import ContractService from '@Services/contract';
import TagService from '@Services/tag';
import * as Axios from 'axios';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import DefaultHeader from './Header';
import NavFilter from './NavFilter';
import SideBar from './Sidebar';
import { setReadyForReloading as setReadyForReloadNewContract } from '@Redux/Actions/NewContract';
import { setReadyForReloading as setReadyForReloadListContract } from '@Redux/Actions/ListContract';
import SimpleBar from 'simplebar-react';
import { addToast } from '@Redux/Actions/AlertToast';
import AuthService from '@Services/auth';

const API_BASE_URL = 'https://mcontract.vn/api';

function DefaultLayout(props) {
  const { statistics, readyForReloadStatistics } = useSelector(state => state.data);
  const { file, signers, information, isReadyForCreating } = useSelector(state => state.createContract);
  const dispatch = useDispatch();

  const getBinaryFile = url => {
    return new Promise(async (resolve, reject) => {
      Axios.get(url, {
        responseType: 'blob',
        crossDomain: true,
        withCredentials: true,
      })
        .then(res => {
          const binary = new File([res.data], file.pdf.name, { type: 'application/pdf' });
          resolve(binary);
        })
        .catch(err => {
          reject(err);
        });
    });
  };

  const createContract = async () => {
    try {
      dispatch(setCreatingContract(true));
      const formData = new FormData();
      const binary = await getBinaryFile(file.pdf.url);
      formData.append('title', information.title);
      formData.append('content', information.content);
      formData.append('numberOFExpirationDate', information.numberOFExpirationDate);
      const signerData = signers.map(signer => {
        return {
          fullName: signer.fullName.trim(),
          email: (signer.email.trim()).toLowerCase(),
          taxCode: signer.taxCode.trim(),
          x: signer.position.percentX,
          y: signer.position.percentY,
          page: signer.position.page,
        };
      });
      formData.append('signers', new Blob([JSON.stringify(signerData)], { type: 'application/json' }));
      formData.append('file', binary);
      const res = await ContractService.addContract(formData);
      if (!res.data.isValidTaxcode) {
        dispatch(addToast({ id: res.data.id, contractName: res.data.title, color: 'purple', type: 'ERROR_AUTHOR', isCreateContract: true, message: 'Sai mã số thuế.' }));
      }
      dispatch(setReadyForReloadNewContract(true));
      dispatch(setReadyForReloadListContract(true));
      dispatch(setReadyForReloadStatistics(true));
      dispatch(setCreatingContract(false));
      dispatch(setReadyForCreating(false));
      dispatch(resetCreateContract());
    } catch (err) {
      console.error(err);
      dispatch(addToast({ id: Math.floor(Math.random() * 10000), color: 'red', type: err.errorCode }));
      dispatch(setCreatingContract(false));
    }
  };

  const getStatistics = async () => {
    try {
      const res = await ContractService.getStatisContract();
      dispatch(setStatistics(res.data));
    } catch (err) {
      console.error(err);
    } finally {
      dispatch(setReadyForReloadStatistics(false));
    }
  };

  const getTags = async () => {
    try {
      const res = await TagService.list();
      const data = res.data.sort((a, b) => {
        return b.id - a.id;
      });
      dispatch(setTags(data));
    } catch (err) {
      console.error(err);
    }
  };

  const getSumUnreadNotification = async () => {
    try {
      const res = await AuthService.getUnreadNotification({});
      dispatch(setSumUnreadNotification(res.totalUnread));
    } catch (err) {
      console.error(err);
    }

  }

  useEffect(() => {
    getTags();
    getStatistics();
    getSumUnreadNotification();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (isReadyForCreating) {
      createContract();
    }
  }, [isReadyForCreating]);

  useEffect(() => {
    if (readyForReloadStatistics) {
      getStatistics();
    }
  }, [readyForReloadStatistics]);
  return (
    <>
      <SimpleBar style={{ height: '100vh' }}>
        <DefaultHeader />
        <main id="site_content">
          <SideBar />

          <div id="page_wrapper">{props.children}</div>
        </main>
        <TagModal contracts={[]} />
        <ContractCreateButtonMobile />
        <NavFilter />
      </SimpleBar>
    </>
  );
}
export default DefaultLayout;
