import { setStep, updateSignerPosition } from '@Redux/Actions/CreateContract';
import interact from 'interactjs';
import { useEffect, useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import ColumnPDF from './ColumnPDF';
import ColumnPreview from './ColumnPreview';
import ColumnSigners from './ColumnSigners';

const ContractCreateFormDesign = () => {
  const {
    file,
    signers,
    currentStep,
    preview: { currentPage, totalPages },
  } = useSelector((state: any) => state.createContract);
  const dispatch = useDispatch();

  const pdfSimpleBarRef = useRef<any>();
  const signerSimpleBarRef = useRef<any>();

  const signersRef = useRef(signers);

  const onSubmit = () => {
    dispatch(setStep(4));
  };

  const [loadedPDF, setLoadedPDF] = useState(false);

  useEffect(() => {
    signersRef.current = signers;
  }, [signers]);

  return (
    <>
      <div className="new_contract_body ">
        <div className="ncbody_wrap m-3 my-4 m-md-5 text-center has_ncnav step3-wrap">
          <div className="top_border"></div>
          <div className="nc_body_content">
            <div className="container-fluid py-3 px-4 px-md-3">
              <div className="row helper_wrap">
                <ColumnSigners simplebarRef={signerSimpleBarRef} loadedPDF={loadedPDF} />
                <ColumnPDF simplebarRef={pdfSimpleBarRef} setLoadedPDF={setLoadedPDF} />
                <ColumnPreview />
              </div>
            </div>
          </div>
          <div className="lefttop_corner"></div>
          <div className="leftbottom_corner"></div>
          <div className="righttop_corner"></div>
          <div className="rightbottom_corner"></div>
        </div>
      </div>
      <div className="new_contract_nav text-center py-3">
        <button className="btn btn_outline_site mx-2 mx-sm-3" onClick={() => dispatch(setStep(2))}>
          QUAY LẠI
        </button>
        <button className="btn btn_site mx-2 mx-sm-3" onClick={() => onSubmit()} disabled={signers.filter((signer: any) => signer.position.page).length < signers.length}>
          TIẾP TỤC
        </button>
      </div>
    </>
  );
  return <></>;
};

export default ContractCreateFormDesign;
