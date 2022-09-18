import { IconCheckGreen } from '@Components/Icon';
import { IconEmailSign } from '@Components/Icon';
import { useSelector } from 'react-redux';
import GeneralService from '@Services/general';
const SignersInformation = () => {
  const { contract } = useSelector(state => state.detailContract);

  const countSigned = signers => {
    return signers.filter(s => s.isSigned).length;
  };

  const onClickSigner = (e, id) => {
    e.preventDefault();
    e.stopPropagation();
    const signPos = document.querySelector(`.signDetail_${id}`);
    if (signPos) {
      signPos.scrollIntoView({
        block: 'center',
      });
    }
  };

  return (
    <>
      {contract && contract.signerDtos && (
        <>
          {countSigned(contract.signerDtos) > 0 && (
            <div className="sign_progress text-center" style={{ width: `${(countSigned(contract.signerDtos) / contract.signerDtos.length) * 100}%` }}>
              {countSigned(contract.signerDtos)}/{contract.signerDtos.length} Đã ký
            </div>
          )}
          {contract.signerDtos.map(item => {
            return (
              <div className="signer_item" key={`signer_${item.id}`} onClick={e => onClickSigner(e, item.id)}>
                <div className={`row px-3 py-2 inforSigner_${item.id}`} >
                  <div className="col-auto pr-0 pl-2">
                    <div className="person_img_wrap">
                      {
                        item.avatarPath ?
                          <img
                            src={item.avatarPath ? item.avatarPath : ''}
                            className="rounded-circle person_img"
                            onError={e => {
                              e.target.onerror = null;
                              e.target.remove();
                            }}
                            alt=""
                            loading="lazy"
                          /> :
                          <div className="img_alt">
                            <span>{GeneralService.getLetterName(item.fullName)}</span>
                          </div>
                      }

                    </div>
                  </div>
                  <div className="col px-2 person_text">
                    <div className="person_name">
                      <span>{item.fullName}</span>
                      {item.isSigned && (
                        <span className="float-right done_icon">
                          <IconCheckGreen />
                        </span>
                      )}
                      <div className="clearfix"></div>
                    </div>
                    <div className="person_meta person_email">
                      <IconEmailSign />
                      <span title="nguyentranquynhphuong1235813@gmail.com">{item.email}</span>
                    </div>
                    {item.companyName ? (
                      <div className="person_meta">
                        <svg xmlns="http://www.w3.org/2000/svg" enableBackground="new 0 0 24 24" viewBox="0 0 24 24" fill="#777" width="18px" height="18px">
                          <g>
                            <rect fill="none" height="24" width="24" />
                            <rect fill="none" height="24" width="24" />
                          </g>
                          <g>
                            <path d="M17,11V5c0-1.1-0.9-2-2-2H9C7.9,3,7,3.9,7,5v2H5C3.9,7,3,7.9,3,9v10c0,1.1,0.9,2,2,2h5c0.55,0,1-0.45,1-1v-3h2v3 c0,0.55,0.45,1,1,1h5c1.1,0,2-0.9,2-2v-6c0-1.1-0.9-2-2-2H17z M7,19H5v-2h2V19z M7,15H5v-2h2V15z M7,11H5V9h2V11z M11,15H9v-2h2V15 z M11,11H9V9h2V11z M11,7H9V5h2V7z M15,15h-2v-2h2V15z M15,11h-2V9h2V11z M15,7h-2V5h2V7z M19,19h-2v-2h2V19z M19,15h-2v-2h2V15z" />
                          </g>
                        </svg>
                        <span>{item.companyName}</span>
                      </div>
                    ) : (
                      <></>
                    )}

                    <div className="person_meta">
                      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#777" width="18px" height="18px">
                        <path d="M0 0h24v24H0V0z" fill="none" />
                        <path d="M12.65 10C11.7 7.31 8.9 5.5 5.77 6.12c-2.29.46-4.15 2.29-4.63 4.58C.32 14.57 3.26 18 7 18c2.61 0 4.83-1.67 5.65-4H17v2c0 1.1.9 2 2 2s2-.9 2-2v-2c1.1 0 2-.9 2-2s-.9-2-2-2h-8.35zM7 14c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2z" />
                      </svg>
                      <span>MST: {item.taxCode}</span>
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </>
      )}
    </>
  );
};
export default SignersInformation;
