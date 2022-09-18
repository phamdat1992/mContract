import ContactForm from "@/components/ContactForm";
import Layout from "@/components/Layout";
import Head from "next/head";
import { Collapse, Button, Card, Accordion, useAccordionToggle } from 'react-bootstrap';
import React, { useState } from 'react';
import ModalVideo from '@/components/ModalVideo';
import { StepInstruction } from '../mocks/hoi-dap'
import Footer from "@/components/Footer";
import SuccessToast from '@/components/SuccessToast';

function CustomToggle({ children, eventKey }: any) {
    const decoratedOnClick = useAccordionToggle(eventKey, () =>
        console.log('totally custom!'),
    );
    return (
        <h2 className="mb-0">
            <button
                type="button"
                className="btn btn-link btn-block text-left question-custom"
                onClick={decoratedOnClick}
            >
                {children}
            </button>

        </h2>

    );
}
const QuestionAnswer = () => {
    const [showToastSucces, setShowToastSuccess] = useState(false);
    const changeForm = (status: boolean) => {
        setShowToastSuccess(status);
    }
    return (
        <Layout>
            <Head>
                <title>MContract</title>
            </Head>
            <style jsx>{`
                .link-to svg {
                    margin-right: 5px;
                }
            `}</style>
            <div className="info pt-4 pb-2 intro-extra">
                <div className="container">
                    <div className="row info" data-aos="fade-up">
                        <div className="col-12 col-lg-8">
                            <div className="text_info">
                                <div className="title mb-2">
                                    <p>Hỏi đáp</p>
                                    <h2>Hệ thống MContract cung cấp tới khách hàng</h2>
                                </div>
                                <p>
                                    - Video hướng dẫn sử dụng hệ thống MContract
                                    <br />
                                    - Các câu hỏi thường gặp khi sử dụng hệ thống MContract
                                    <br />
                                    - Form liên hệ gửi câu hỏi cho hệ thống
                                </p>
                            </div>
                        </div>
                        <div className="col-12 col-lg">
                            <div className="img_info mt-3 mt-lg-0 text-center">
                                <img className="img-fluid" src="/images/QA/QA.svg" alt="" loading="lazy" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="page_wrapper ">
                <div className="container px-24px" id="page_content">
                    <nav id="navbar-example2" className="navbar navbar-light bg-light intro-extra">
                        <ul className="nav nav-pills mx-auto">
                            <li className="nav-item nav-item_qa">
                                <a className="nav-link link-to" href="/hoi-dap#policy">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                        className="bi bi-journal-check" viewBox="0 0 16 16">
                                        <path fillRule="evenodd"
                                            d="M10.854 6.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 8.793l2.646-2.647a.5.5 0 0 1 .708 0z" />
                                        <path
                                            d="M3 0h10a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2v-1h1v1a1 1 0 0 0 1 1h10a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H3a1 1 0 0 0-1 1v1H1V2a2 2 0 0 1 2-2z" />
                                        <path
                                            d="M1 5v-.5a.5.5 0 0 1 1 0V5h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1zm0 3v-.5a.5.5 0 0 1 1 0V8h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1zm0 3v-.5a.5.5 0 0 1 1 0v.5h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1H1z" />
                                    </svg>
                                    Video hướng dẫn</a>
                            </li>
                            <li className="nav-item nav-item_qa">
                                <a className="nav-link link-to" href="/hoi-dap#info">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                        className="bi bi-question-circle" viewBox="0 0 16 16">
                                        <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
                                        <path
                                            d="M5.255 5.786a.237.237 0 0 0 .241.247h.825c.138 0 .248-.113.266-.25.09-.656.54-1.134 1.342-1.134.686 0 1.314.343 1.314 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.003.217a.25.25 0 0 0 .25.246h.811a.25.25 0 0 0 .25-.25v-.105c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.267 0-2.655.59-2.75 2.286zm1.557 5.763c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z" />
                                    </svg>
                                    Câu hỏi thường gặp</a>
                            </li>
                            <li className="nav-item nav-item_qa">
                                <a className="nav-link link-to active" href="/hoi-dap#contact">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                        className="bi bi-cursor" viewBox="0 0 16 16">
                                        <path
                                            d="M14.082 2.182a.5.5 0 0 1 .103.557L8.528 15.467a.5.5 0 0 1-.917-.007L5.57 10.694.803 8.652a.5.5 0 0 1-.006-.916l12.728-5.657a.5.5 0 0 1 .556.103zM2.25 8.184l3.897 1.67a.5.5 0 0 1 .262.263l1.67 3.897L12.743 3.52 2.25 8.184z" />
                                    </svg>
                                    Gửi câu hỏi</a>
                            </li>
                        </ul>
                    </nav>
                    <div data-spy="scroll" data-target="#navbar-example2" data-offset="0" id="instruct">
                        <h4 id="policy">VIDEO HƯỚNG DẪN SỬ DỤNG</h4>
                        <div className="row video">
                            {StepInstruction.map(item =>
                                <ModalVideo step={item} ></ModalVideo>)
                            }
                        </div>
                        <h4 id="info">CÂU HỎI THƯỜNG GẶP</h4>
                        <div className="fqa pb-5 text-justify intro-extra intro-extra">
                            <Accordion>
                                <Card>
                                    <Card.Header>
                                        <CustomToggle eventKey="0">
                                            <div className="circle">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30"
                                                    fill="currentColor" className="bi bi-question" viewBox="0 0 16 16">
                                                    <path
                                                        d="M5.255 5.786a.237.237 0 0 0 .241.247h.825c.138 0 .248-.113.266-.25.09-.656.54-1.134 1.342-1.134.686 0 1.314.343 1.314 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.003.217a.25.25 0 0 0 .25.246h.811a.25.25 0 0 0 .25-.25v-.105c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.267 0-2.655.59-2.75 2.286zm1.557 5.763c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z" />
                                                </svg>
                                            </div>
                                            <span>Tính pháp lý của hợp đồng ký số điện tử?</span>
                                        </CustomToggle>
                                    </Card.Header>
                                    <Accordion.Collapse eventKey="0">
                                        <Card.Body>
                                            <p>
                                                Tính pháp lý của Hợp đồng điện tử được căn cứ vào các văn bản pháp luật dưới
                                                đây:
                                            </p>
                                            <p>

                                                <b>Căn cứ vào Luật mẫu của UNCITRAL</b> (Ủy ban Luật Thương mại quốc tế của
                                                Liên
                                                hợp quốc) về Thương mại điện tử năm 1996 (sửa đổi năm 1998),
                                                Luật Giao dịch điện tử 2005:
                                            </p>

                                            <p>Thừa nhận tính pháp lý của Hợp đồng điện tử.</p>

                                            <p><b>Căn cứ vào Nghị định 130/2018/NĐ-CP</b> quy định chi tiết thi hành Luật
                                                giao
                                                dịch điện tử về chữ ký số và dịch vụ chứng thực chữ ký số.</p>

                                            <p>Giá trị của chữ ký số trên thông điệp dữ liệu là tương đương với chữ ký trên
                                                văn
                                                bản giấy.</p>

                                            <p>Giá trị của chữ ký số của cơ quan/tổ chức trên thông điệp dữ liệu là tương
                                                đương
                                                với con dấu.</p>

                                            <p><b>Căn cứ vào Luật mẫu về Chữ ký điện tử năm 2001</b>: ngày 05 tháng 07 năm
                                                2001,
                                                UNCITRAL đã thông qua Luật mẫu về Chữ ký điện tử và sách hướng dẫn về việc
                                                áp
                                                dụng.
                                                <p>
                                                    <p><b>Căn cứ Công ước Liên hợp quốc về sử dụng các giao dịch điện tử trong các
                                                        hợp
                                                        đồng quốc tế năm 2005</b>:</p>
                                                    <p>Được bắt đầu từ năm 2001, ngay sau Luật mẫu về Chữ ký điện tử được ban hành.
                                                        Việc
                                                        xây dựng Công ước quốc tế về sử dụng các giao dịch điện tử trong các hợp
                                                        đồng
                                                        quốc tế đã hoàn tất vào ngày 23/11/2005 sau khi được Đại hội đồng Liên hợp
                                                        quốc
                                                        thông qua. Công ước này bao gồm các điều khoản quy định về những vấn đề liên
                                                        quan đến hợp đồng điện tử nhằm mục đích tăng cường tính an toàn pháp lý và
                                                        khả
                                                        năng tiên lượng trong thương mại khi mà các giao dịch hợp đồng quốc tế được
                                                        thực
                                                        hiện thông qua các phương tiện điện tử.</p>
                                                </p>
                                            </p>
                                        </Card.Body>
                                    </Accordion.Collapse>
                                </Card>
                                <Card>
                                    <Card.Header>
                                        <CustomToggle eventKey="1">
                                            <div className="circle">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30"
                                                    fill="currentColor" className="bi bi-hdd" viewBox="0 0 16 16">
                                                    <path
                                                        d="M4.5 11a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1zM3 10.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0z" />
                                                    <path
                                                        d="M16 11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V9.51c0-.418.105-.83.305-1.197l2.472-4.531A1.5 1.5 0 0 1 4.094 3h7.812a1.5 1.5 0 0 1 1.317.782l2.472 4.53c.2.368.305.78.305 1.198V11zM3.655 4.26 1.592 8.043C1.724 8.014 1.86 8 2 8h12c.14 0 .276.014.408.042L12.345 4.26a.5.5 0 0 0-.439-.26H4.094a.5.5 0 0 0-.44.26zM1 10v1a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-1a1 1 0 0 0-1-1H2a1 1 0 0 0-1 1z" />
                                                </svg>
                                            </div>
                                            <span>USB Token là gì? Nó được cung cấp ở đâu?</span>
                                        </CustomToggle>
                                    </Card.Header>
                                    <Accordion.Collapse eventKey="1">
                                        <Card.Body>
                                            <p>
                                                <b>Thiết bị USB Token</b> còn được gọi là Chữ ký số hay Chứng thư số, là một
                                                thiết bị chứa các dữ liệu mã hóa và thông tin của một doanh nghiệp, dùng để
                                                xác
                                                nhận thay cho chữ ký của doanh nghiệp đó trên các loại văn bản và tài liệu số
                                                được thực hiện trong các giao dịch điện tử qua mạng internet.
                                            </p>
                                            <p>
                                                <b>Thông tin có trong USB Token dành cho doanh nghiệp bao gồm: </b>
                                            </p>

                                            <p> - Thông tin doanh nghiệp: tên doanh nghiệp, mã số thuế...</p>

                                            <p> - Số hiệu của chứng thư số (số seri).</p>

                                            <p> - Thời hạn có hiệu lực của chứng thư số.</p>

                                            <p>- Tên của tổ chức chứng thực chữ ký số (Ví dụ: VNPT-CA).</p>

                                            <p> - Chữ ký số của tổ chức chứng thực chữ ký số.</p>

                                            <p> - Các hạn chế về mục đích, phạm vi sử dụng của chữ ký số.</p>

                                            <p> - Các hạn chế về trách nhiệm của tổ chức cung cấp dịch vụ chứng thực chữ ký
                                                số.</p>

                                            <p> - Các nội dung cần thiết khác theo quy định của Bộ Thông Tin Truyền Thông.
                                            </p>

                                            <p> Như vậy, chữ ký số có giá trị pháp lý tương đương với chữ ký tay và con dấu.
                                            </p>
                                            <p>
                                                Hiện nay, doanh nghiệp có thể mua chữ ký số của các doanh nghiệp sau: <b>TẬP
                                                    ĐOÀN VIETTEL, FPT, BKAV, CK, VINA, NEWTEL, NACENCOMM, SAFE… </b>Các nhà
                                                cung cấp
                                                này được phép cung cấp token cho doanh nghiệp theo quy định của pháp luật.
                                            </p>
                                        </Card.Body>
                                    </Accordion.Collapse>
                                </Card>
                                <Card>
                                    <Card.Header>
                                        <CustomToggle eventKey="2">
                                            <div className="circle">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30"
                                                    fill="currentColor" className="bi bi-bar-chart-steps"
                                                    viewBox="0 0 16 16">
                                                    <path
                                                        d="M.5 0a.5.5 0 0 1 .5.5v15a.5.5 0 0 1-1 0V.5A.5.5 0 0 1 .5 0zM2 1.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-4a.5.5 0 0 1-.5-.5v-1zm2 4a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-7a.5.5 0 0 1-.5-.5v-1zm2 4a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-6a.5.5 0 0 1-.5-.5v-1zm2 4a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-7a.5.5 0 0 1-.5-.5v-1z" />
                                                </svg>
                                            </div>
                                            <span>Các bước ký số điện tử với hệ thống MContract?</span>
                                        </CustomToggle>
                                    </Card.Header>
                                    <Accordion.Collapse eventKey="2">
                                        <Card.Body>
                                            <p>Để thực hiện ký số điện tử với hệ thống MContract khách hàng cần thực hiện
                                                lần
                                                lượt <b>4 bước</b> đơn giản như sau:</p>
                                            <div className="row">
                                                <div className="col-12 col-md-4 pt-3">
                                                    <p><b>Bước 1: Tải lên hợp đồng</b></p>
                                                    <img src="/images/step_1.webp" className="w-100" alt="" loading="lazy" />
                                                </div>
                                                <div className="col-12 col-md-4 pt-3">
                                                    <p><b>Hợp đồng đã được tải lên</b></p>
                                                    <img src="/images/step_2.webp" className="w-100" alt="" loading="lazy" />
                                                </div>
                                                <div className="col-12 col-md-4 pt-3">
                                                    <p><b>Bước 2: Chọn đối tác</b></p>
                                                    <img src="/images/step_3.webp" className="w-100" alt="" loading="lazy" />
                                                </div>
                                                <div className="col-12 col-md-4 pt-3">
                                                    <p><b>Bước 3: Thiết kế hợp đồng</b></p>
                                                    <img src="/images/step_4.webp" className="w-100" alt="" loading="lazy" />
                                                </div>
                                                <div className="col-12 col-md-4 pt-3">
                                                    <p><b>Bước 4: Gửi đi</b></p>
                                                    <img src="/images/step_5.webp" className="w-100" alt="" loading="lazy" />
                                                </div>

                                            </div>
                                        </Card.Body>
                                    </Accordion.Collapse>
                                </Card>
                                <Card>
                                    <Card.Header>
                                        <CustomToggle eventKey="3">
                                            <div className="circle">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30"
                                                    fill="currentColor" className="bi bi-file-earmark-check"
                                                    viewBox="0 0 16 16">
                                                    <path
                                                        d="M10.854 7.854a.5.5 0 0 0-.708-.708L7.5 9.793 6.354 8.646a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z" />
                                                    <path
                                                        d="M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2zM9.5 3A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.5v2z" />
                                                </svg>
                                            </div>
                                            <span>Các định dạng hợp đồng hỗ trợ tải lên hệ thống MContract?</span>
                                        </CustomToggle>
                                    </Card.Header>
                                    <Accordion.Collapse eventKey="3">
                                        <Card.Body>
                                            <p>Hiện tại hệ thống MContract đang hỗ trợ hợp đồng tải lên theo các định dạng
                                                sau:</p>
                                            <p>- Định dạng đuôi <b>.doc, docx.</b></p>
                                            <p>- Định dạng đuôi <b>.pdf.</b></p>
                                        </Card.Body>
                                    </Accordion.Collapse>
                                </Card>
                            </Accordion>
                        </div>
                        <h4 id="contact">Gửi câu hỏi cho chúng tôi</h4>
                        <div className="box mb-5">
                            <div className="row">
                                <div className="col-12 col-md-7">
                                    <ContactForm changeForm={changeForm} ></ContactForm>
                                </div>
                                <div className="col-12 col-md-5">
                                    <div className="foot_logo">
                                        <img className="img-fluid" src="/images/QA/contact.webp" alt="" loading="lazy" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <Footer page="question" />
            {showToastSucces && <SuccessToast />}

        </Layout >
    )
}
export default QuestionAnswer