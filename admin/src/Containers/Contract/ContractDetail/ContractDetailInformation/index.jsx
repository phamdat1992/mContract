import { useSelector } from "react-redux";

const ContractDetailInformation = () => {
    const { contract } = useSelector(state => state.detailContract);
    const formatDate = (e) => {
        const d = new Date(e);
        return ('0' + d.getDate()).slice(-2) + '/' + ('0' + (d.getMonth() + 1)).slice(-2) + '/' + d.getFullYear();
    };

    const showExpired = () => {
        const createdDate = new Date(contract.createdTime);
        const expiredDate = new Date(contract.expirationTime);
        return (expiredDate - createdDate) / 86400000 < 91;
    }

    return (
        <>
            {showExpired() &&
                <div className="mb-3">
                    <div><b>Ngày hết hạn</b></div>
                    <div className="text-danger">{formatDate(contract.expirationTime)}</div>
                </div>
            }
            <div className="mb-3">
                <div><b>Tiêu đề</b></div>
                <div>{contract.title}</div>
            </div>
            <div className="mb-3">
                <div><b>Nội dung</b></div>
                <div className="text-editable" contentEditable="true" dangerouslySetInnerHTML={{__html: contract.content}}></div>
                {/* <div className="text-pre-line">{contract.content}</div> */}
            </div>
            <div className="mb-3">
                <div><b>Tập tin</b></div>
                <div className="d-inline align-middle">
                    <img className="mr-1" style={{ height: '22px' }} src="older/file_icon.svg" alt="" loading="lazy" onError={(e) => { e.target.onerror = null; e.target.remove() }} />
                    <span className="align-middle show-ellipsis">{contract.fileName}</span>
                </div>
            </div>
        </>
    )
}
export default ContractDetailInformation;