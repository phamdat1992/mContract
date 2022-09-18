import { configureStore } from '@reduxjs/toolkit';
import DataReducer from '@/Redux/Reducers/DataReducer';
import AuthReducer from '../Reducers/AuthReducer';
import MenuReducer from '@Redux/Reducers/MenuReducer';
import DetailContractReducer from '@Redux/Reducers/DetailContractReducer';
import NotificationReducer from '@Redux/Reducers/NotificationReducer';
import CreateContractReducer from '@Redux/Reducers/CreateContractReducer';
import NewContractReducer from '@Redux/Reducers/NewContractReducer';
import ListContractReducer from '@Redux/Reducers/ListContractReducer';
import AlertModalReducer from '@Redux/Reducers/AlertModalReducer';
import InstallToolModalReducer from '@Redux/Reducers/InstallToolModalReducer';
import AlertToastReducer from '@Redux/Reducers/AlertToastReducer';

const store = configureStore({
   reducer: {
      data: DataReducer,
      auth: AuthReducer,
      menu: MenuReducer,
      detailContract: DetailContractReducer,
      notification: NotificationReducer,
      createContract: CreateContractReducer,
      newContracts: NewContractReducer,
      listContracts: ListContractReducer,
      alertModal: AlertModalReducer,
      installToolModal: InstallToolModalReducer,
      alertToast: AlertToastReducer
   }
});

export {
   store
}