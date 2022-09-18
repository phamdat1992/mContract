import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import ContractTable from '@Containers/Contract/ContractTable';

function PageContracts({ title, status, search }) {
  const location = useLocation();
  const URLParams = new URLSearchParams(location.search);
  const query = {
    status: status ? status : '',
    type: URLParams.get('type') || '',
    partner: URLParams.get('partner') || '',
    search: URLParams.get('search') || '',
    topic: URLParams.get('topic') || '',
    fromDate: URLParams.get('fromDate') || '',
    toDate: URLParams.get('toDate') || '',
    tagId: URLParams.get('tagId') || null,
  };
  useEffect(() => {
    document.title = title;
    // eslint-disable-next-line
  }, []);

  return (
    <ContractTable query={query} />
  );
}

export { PageContracts };
