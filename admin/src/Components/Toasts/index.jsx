import Toast from '@Components/Toast';
import { removeToast } from '@Redux/Actions/AlertToast';
import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';

const Toasts = () => {
  const { toasts } = useSelector(state => state.alertToast);
  const [listToasts, setListToasts] = useState(toasts);
  const dispatch = useDispatch();

  const destroyToast = id => {
    dispatch(removeToast({ id: id }));
  };

  useEffect(() => {
    setListToasts(toasts);
  }, [toasts]);

  return (
    <>
      {listToasts.map(item =>
        <Toast key={item.id} id={item.id} contractName={item.contractName} type={item.type} color={item.color} message={item.message} onDestroy={destroyToast} isCreateContract={item.isCreateContract} />
      )}
    </>
  )
}
export default Toasts;
