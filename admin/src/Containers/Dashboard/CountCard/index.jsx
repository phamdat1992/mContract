import ContractStatus from '@Enums/contract-status';
import { useSelector } from 'react-redux';

function CountCard(props) {
    const statistics = useSelector(state => state.data.statistics);
    const statisStatus = (value) => {
        for (let i = 0; i < statistics.length; i++) {
            if (statistics[i].status === value) {
                return statistics[i].count;
            }
        }
        //chưa có hợp đồng nào -> trả về 0
        return 0;
    };
    return (
        <div className="row count_card">
            <div className="col-12 col-sm-6 col-md-4 mt-4 ">
                <div className="card-title completed">
                    <p>
                        {statistics && statisStatus(ContractStatus.FINISH)}
                        <br />
                        HOÀN THÀNH
                    </p>
                </div>
            </div>
            <div className="col-4 col-sm-6 col-md mt-4 ">
                <div className="card-title expire">
                    <p>
                        {statistics && statisStatus(ContractStatus.EXPIRED)}
                        <br />
                        HẾT HẠN
                    </p>
                </div>
            </div>
            <div className="col-4 col-sm-6 col-md mt-4">
                <div className="card-title wait">
                    <p>
                        {statistics && statisStatus(ContractStatus.IN_PROGRESS)}
                        <br />
                        CHỜ XỬ LÝ
                    </p>
                </div>
            </div>
            <div className="col-4 col-sm-6 col-md mt-4">
                <div className="card-title wait">
                    <p>
                        {statistics && statisStatus(ContractStatus.WAIT_PARTNER)}
                        <br />
                        CHỜ ĐỐI TÁC
                    </p>
                </div>
            </div>
        </div>
    );
}

export default CountCard;