import ap from '@/assets/images/ap.webp';
import infomation from '@/assets/images/infomation.webp';
import step_1 from '@/assets/images/step_1.webp';
import step_2 from '@/assets/images/step_2.webp';
import step_3 from '@/assets/images/step_3.webp';
import step_4 from '@/assets/images/step_4.webp';
import step_5 from '@/assets/images/step_5.webp';
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import SimpleBar from "simplebar-react";
import { IconFileCheckLarge, IconSupport } from '@Components/Icon';

PageSearch.propTypes = {

};

function PageSearch(props) {
    useEffect(() => {
        document.body.style.backgroundColor = "#fff";
        return () => {
            document.body.style.backgroundColor = '#eef5f9';
        };
    }, []);
    useEffect(() => {
        document.title = "MContract"
    }, []);
    return (
        <div className="search-policy">
            <nav id="navbar-example2" className="navbar navbar-light bg-light">
                <a className="navbar-brand" href="javascript:void(0)">TRA CỨU THÔNG TIN</a>
                <ul className="nav nav-pills">
                    <li className="nav-item">
                        <a className="nav-link" href="#policy">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                className="bi bi-journal-check" viewBox="0 0 16 16">
                                <path fillRule="evenodd"
                                    d="M10.854 6.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 8.793l2.646-2.647a.5.5 0 0 1 .708 0z" />
                                <path d="M3 0h10a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2v-1h1v1a1 1 0 0 0 1 1h10a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H3a1 1 0 0 0-1 1v1H1V2a2 2 0 0 1 2-2z" />
                                <path d="M1 5v-.5a.5.5 0 0 1 1 0V5h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1zm0 3v-.5a.5.5 0 0 1 1 0V8h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1zm0 3v-.5a.5.5 0 0 1 1 0v.5h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1z" />
                            </svg>
                            &nbsp;Chính sách</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="#info">
                            <IconSupport />
                            &nbsp;Câu hỏi thường gặp</a>
                    </li>
                </ul>
            </nav>
            <SimpleBar className="container-fluid px-24px" id="page_content">
                <div data-spy="scroll" data-target="#navbar-example2" data-offset="0">
                    <h4 id="policy">THÔNG TIN CHÍNH SÁCH</h4>
                    <div className="row">
                        <div className="col-12 col-md-6 mb-3 mb-sm-0">
                            <div className="card h-100">
                                <img src={ap} className="card-img-top" alt="..." loading="lazy" />
                                <div className="card-body">
                                    <h5 className="card-title">Điều khoản và điều kiện</h5>
                                    <p className="card-text">
                                        Hệ thống MContract thu thấp thông tin của khách hàng với các mục đích....
                                    </p>
                                    <Link to="/tra-cuu/dieu-khoan" className="btn btn-primary"
                                    >Xem chi tiết
                                    </Link>
                                </div>
                            </div>
                        </div>
                        <div className="col-12 col-md-6 mb-3 mb-sm-0">
                            <div className="card h-100">
                                <img src={infomation} className="card-img-top" alt="..." loading="lazy" />
                                <div className="card-body">
                                    <h5 className="card-title">Chính sách bảo mật</h5>
                                    <p className="card-text">Chính sách bảo mật nhằm bảo vệ thông tin của khách hàng khi sử dụng...</p>
                                    <Link to="/tra-cuu/chinh-sach" className="btn btn-primary" >Xem chi tiết
                                    </Link>
                                </div>
                            </div>
                        </div>

                    </div>
                    <h4 id="info">CÂU HỎI THƯỜNG GẶP</h4>
                    <div className="fqa pb-5 text-justify">
                        <div className="accordion" id="accordionExample">
                            <div className="card">
                                <div className="card-header" id="headingOne">
                                    <h2 className="mb-0">
                                        <button className="btn btn-link btn-block text-left question-custom" type="button"
                                            data-toggle="collapse" data-target="#collapseOne" aria-expanded="false"
                                            aria-controls="collapseOne">
                                            <div className="circle">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30"
                                                    fill="currentColor" className="bi bi-question" viewBox="0 0 16 16">
                                                    <path d="M5.255 5.786a.237.237 0 0 0 .241.247h.825c.138 0 .248-.113.266-.25.09-.656.54-1.134 1.342-1.134.686 0 1.314.343 1.314 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.003.217a.25.25 0 0 0 .25.246h.811a.25.25 0 0 0 .25-.25v-.105c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.267 0-2.655.59-2.75 2.286zm1.557 5.763c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z" />
                                                </svg>
                                            </div>
                                            <span>Tính pháp lý của hợp đồng ký số điện tử?</span>
                                        </button>
                                    </h2>
                                </div>

                                <div id="collapseOne" className="collapse" aria-labelledby="headingOne"
                                    data-parent="#accordionExample">
                                    <div className="card-body">
                                        <p>
                                            Tính pháp lý của Hợp đồng điện tử được căn cứ vào các văn bản pháp luật dưới
                                            đây:
                                        </p>
                                        <p>
                                            <b>Căn cứ vào Luật mẫu của UNCITRAL</b>
                                            (Ủy ban Luật Thương mại quốc tế của Liên
                                            hợp quốc) về Thương mại điện tử năm 1996 (sửa đổi năm 1998),
                                            Luật Giao dịch điện tử 2005:
                                        </p>

                                        <p>Thừa nhận tính pháp lý của Hợp đồng điện tử.</p>

                                        <p>
                                            <b>Căn cứ vào Nghị định 130/2018/NĐ-CP</b> quy định chi tiết thi hành Luật giao
                                            dịch điện tử về chữ ký số và dịch vụ chứng thực chữ ký số
                                        </p>

                                        <p>
                                            Giá trị của chữ ký số trên thông điệp dữ liệu là tương đương với chữ ký trên văn
                                            bản giấy.
                                        </p>

                                        <p>
                                            Giá trị của chữ ký số của cơ quan/tổ chức trên thông điệp dữ liệu là tương đương
                                            với con dấu.
                                        </p>

                                        <p>
                                            <b>Căn cứ vào Luật mẫu về Chữ ký điện tử năm 2001</b>: ngày 05 tháng 07 năm 2001,
                                            UNCITRAL đã thông qua Luật mẫu về Chữ ký điện tử và sách hướng dẫn về việc áp
                                            dụng.
                                        </p>
                                        <p>
                                            <b>Căn cứ Công ước Liên hợp quốc về sử dụng các giao dịch điện tử trong các hợp
                                                đồng quốc tế năm 2005</b>:
                                        </p>
                                        <p>
                                            Được bắt đầu từ năm 2001, ngay sau Luật mẫu về Chữ ký điện tử được ban hành. Việc
                                            xây dựng Công ước quốc tế về sử dụng các giao dịch điện tử trong các hợp đồng
                                            quốc tế đã hoàn tất vào ngày 23/11/2005 sau khi được Đại hội đồng Liên hợp quốc
                                            thông qua. Công ước này bao gồm các điều khoản quy định về những vấn đề liên
                                            quan đến hợp đồng điện tử nhằm mục đích tăng cường tính an toàn pháp lý và khả
                                            năng tiên lượng trong thương mại khi mà các giao dịch hợp đồng quốc tế được thực
                                            hiện thông qua các phương tiện điện tử.
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div className="card">
                                <div className="card-header" id="headingTwo">
                                    <h2 className="mb-0">
                                        <button className="btn btn-link btn-block text-left collapsed question-custom" type="button"
                                            data-toggle="collapse" data-target="#collapseTwo" aria-expanded="false"
                                            aria-controls="collapseTwo">
                                            <div className="circle">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30"
                                                    fill="currentColor" className="bi bi-hdd" viewBox="0 0 16 16">
                                                    <path d="M4.5 11a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1zM3 10.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0z" />
                                                    <path d="M16 11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V9.51c0-.418.105-.83.305-1.197l2.472-4.531A1.5 1.5 0 0 1 4.094 3h7.812a1.5 1.5 0 0 1 1.317.782l2.472 4.53c.2.368.305.78.305 1.198V11zM3.655 4.26 1.592 8.043C1.724 8.014 1.86 8 2 8h12c.14 0 .276.014.408.042L12.345 4.26a.5.5 0 0 0-.439-.26H4.094a.5.5 0 0 0-.44.26zM1 10v1a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-1a1 1 0 0 0-1-1H2a1 1 0 0 0-1 1z" />
                                                </svg>
                                            </div>
                                            <span>USB Token là gì? Nó được cung cấp ở đâu?</span>
                                        </button>
                                    </h2>
                                </div>
                                <div id="collapseTwo" className="collapse" aria-labelledby="headingTwo"
                                    data-parent="#accordionExample">
                                    <div className="card-body">
                                        <p>
                                            <b>Thiết bị USB Token</b> còn được gọi là Chữ ký số hay Chứng thư số, là một
                                            thiết bị chứa các dữ liệu mã hóa và thông tin của một doanh nghiệp, dùng để xác
                                            nhận thay cho chữ ký của doanh nghiệp đó trên các loại văn bản và tài liệu số
                                            được thực hiện trong các giao dịch điện tử qua mạng internet.
                                        </p>
                                        <p>
                                            <b>Thông tin có trong USB Token dành cho doanh nghiệp bao gồm: </b></p>

                                        <p> - Thông tin doanh nghiệp: tên doanh nghiệp, mã số thuế...</p>

                                        <p> - Số hiệu của chứng thư số (số seri).</p>

                                        <p> - Thời hạn có hiệu lực của chứng thư số.</p>

                                        <p>- Tên của tổ chức chứng thực chữ ký số (Ví dụ: VNPT-CA).</p>

                                        <p> - Chữ ký số của tổ chức chứng thực chữ ký số.</p>

                                        <p> - Các hạn chế về mục đích, phạm vi sử dụng của chữ ký số.</p>

                                        <p> - Các hạn chế về trách nhiệm của tổ chức cung cấp dịch vụ chứng thực chữ ký
                                            số.</p>

                                        <p> - Các nội dung cần thiết khác theo quy định của Bộ Thông Tin Truyền Thông.</p>

                                        <p> Như vậy, chữ ký số có giá trị pháp lý tương đương với chữ ký tay và con dấu.</p>
                                        <p>
                                            Hiện nay, doanh nghiệp có thể mua chữ ký số của các doanh nghiệp sau: <b>TẬP
                                                ĐOÀN VIETTEL, FPT, BKAV, CK, VINA, NEWTEL, NACENCOMM, SAFE… </b>Các nhà cung cấp
                                            này được phép cung cấp token cho doanh nghiệp theo quy định của pháp luật.
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div className="card">
                                <div className="card-header" id="headingThree">
                                    <h2 className="mb-0">
                                        <button className="btn btn-link btn-block text-left collapsed question-custom" type="button"
                                            data-toggle="collapse" data-target="#collapseThree" aria-expanded="false"
                                            aria-controls="collapseThree">
                                            <div className="circle">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30"
                                                    fill="currentColor" className="bi bi-bar-chart-steps" viewBox="0 0 16 16">
                                                    <path d="M.5 0a.5.5 0 0 1 .5.5v15a.5.5 0 0 1-1 0V.5A.5.5 0 0 1 .5 0zM2 1.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-4a.5.5 0 0 1-.5-.5v-1zm2 4a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-7a.5.5 0 0 1-.5-.5v-1zm2 4a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-6a.5.5 0 0 1-.5-.5v-1zm2 4a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-7a.5.5 0 0 1-.5-.5v-1z" />
                                                </svg>
                                            </div>
                                            <span>Các bước ký số điện tử với hệ thống MContract?</span>
                                        </button>
                                    </h2>
                                </div>
                                <div id="collapseThree" className="collapse" aria-labelledby="headingThree"
                                    data-parent="#accordionExample">
                                    <div className="card-body step">
                                        <p>Để thực hiện ký số điện tử với hệ thống MContract khách hàng cần thực hiện lần
                                            lượt <b>4 bước</b> đơn giản như sau:</p>
                                        <div className="row">
                                            <div className="col-12 col-md-4 pt-3">
                                                <p><b>Bước 1: Tải lên hợp đồng</b></p>
                                                <img src={step_1} alt="" loading="lazy" />
                                            </div>
                                            <div className="col-12 col-md-4 pt-3">
                                                <p><b>Hợp đồng đã được tải lên</b></p>
                                                <img src={step_2} alt="" loading="lazy" />
                                            </div>
                                            <div className="col-12 col-md-4 pt-3">
                                                <p><b>Bước 2: Chọn đối tác</b></p>
                                                <img src={step_3} alt="" loading="lazy" />
                                            </div>
                                            <div className="col-12 col-md-4 pt-3">
                                                <p><b>Bước 3: Thiết kế hợp đồng</b></p>
                                                <img src={step_4} alt="" loading="lazy" />
                                            </div>
                                            <div className="col-12 col-md-4 pt-3">
                                                <p><b>Bước 4: Gửi đi</b></p>
                                                <img src={step_5} alt="" loading="lazy" />
                                            </div>

                                        </div>

                                    </div>
                                </div>
                            </div>
                            <div className="card">
                                <div className="card-header" id="headingfour">
                                    <h2 className="mb-0">
                                        <button className="btn btn-link btn-block text-left collapsed question-custom" type="button"
                                            data-toggle="collapse" data-target="#collapsefour" aria-expanded="false"
                                            aria-controls="collapsefour">
                                            <div className="circle">
                                                <IconFileCheckLarge />
                                            </div>
                                            <span>Các định dạng hợp đồng hỗ trợ tải lên hệ thống MContract?</span>
                                        </button>
                                    </h2>
                                </div>
                                <div id="collapsefour" className="collapse" aria-labelledby="headingfour"
                                    data-parent="#accordionExample">
                                    <div className="card-body">
                                        <p>Hiện tại hệ thống MContract đang hỗ trợ hợp đồng tải lên theo các định dạng
                                            sau:</p>
                                        <p>- Định dạng đuôi <b>.doc, docx.</b></p>
                                        <p>- Định dạng đuôi <b>.pdf.</b></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </SimpleBar>
        </div>
    );
}

export { PageSearch };